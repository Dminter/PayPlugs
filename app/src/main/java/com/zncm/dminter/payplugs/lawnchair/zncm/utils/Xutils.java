package com.zncm.dminter.payplugs.lawnchair.zncm.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.ClipboardManager;
import android.util.Log;
import android.widget.Toast;

import com.zncm.dminter.payplugs.App;
import com.zncm.dminter.payplugs.lawnchair.zncm.autocommand.AndroidCommand;
import com.zncm.dminter.payplugs.lawnchair.zncm.data.CardInfo;
import com.zncm.dminter.payplugs.lawnchair.zncm.data.Constant;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.List;


/**
 * Created by jiaomx on 2017/7/14.
 */

public class Xutils {


    public static void runCard(CardInfo cardInfo) {
        if (cardInfo == null) {
            return;
        }

        String cmd = cardInfo.getClassName();

        if (Xutils.isEmptyOrNull(cmd)) {
            return;
        }
        if (cmd.startsWith("alipays://")) {
            String urlCode = "";
            if (cmd.startsWith(SuggestIntent.zfb_ds)) {
                urlCode = "aex02461t5uptlcygocfsbc";
            } else if (cmd.startsWith(SuggestIntent.zfb_url_code)) {
                urlCode = cmd.substring(SuggestIntent.zfb_url_code.length());

            }
            if (Xutils.isNotEmptyOrNull(urlCode)) {
                try {
                    Intent intent = Intent.parseUri(AlipayZeroSdk.INTENT_URL_FORMAT.replace("{urlCode}", urlCode), Intent.URI_INTENT_SCHEME);
                    App.getInstance().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            quick(cardInfo, cmd);
            return;
        }

        if (cmd.startsWith("#Intent;")) {
            quick(cardInfo, cmd);
            return;
        }


        if (cmd.startsWith(SuggestIntent.wx_dl)) {
            cmd = cmd.substring(SuggestIntent.wx_dl.length());
            cmd = "am start -n com.tencent.mm/com.tencent.mm.ui.chatting.En_5b8fbb1e -e Chat_User " + cmd;
            Xutils.cmdExe(cmd);
            return;
        }

        if (cmd.startsWith(SuggestIntent.browser_dl)) {
            cmd = cmd.substring(SuggestIntent.browser_dl.length());
            Xutils.openUrl(cmd);
            return;
        }


        if (cmd.startsWith(SuggestIntent.cmd_dl)) {
            cmd = cmd.substring(SuggestIntent.cmd_dl.length());
            final ShellUtils.CommandResult result = ShellUtils.execCommand(cmd, true);
            if (result != null) {
                if (Xutils.isNotEmptyOrNull(result.successMsg)) {
                    Xutils.copyText(App.getInstance().getBaseContext(), result.successMsg);
                    Xutils.tShort(result.successMsg);
                } else {
                    Xutils.tShort("找不到该命令~");
                }
                return;
            }
        }


        if (cmd.startsWith(SuggestIntent.quick_dl)) {
            cmd = cmd.substring(SuggestIntent.quick_dl.length());
            quick(cardInfo, cmd);
            return;
        }


        Xutils.RunOpenActivity runOpenActivity = new Xutils.RunOpenActivity(cardInfo, Constant.open_ac_attempt);
        runOpenActivity.execute();
    }

    public static void quick(CardInfo cardInfo, String cmd) {
        try {
            App.getInstance().startActivity(Intent.parseUri(cmd, 0));
        } catch (Exception e) {
            try {
                String pkgName = cardInfo.getPackageName();
                if (Xutils.isNotEmptyOrNull(cmd)) {
                    if (cmd.contains("component")) {
                        pkgName = cmd.substring(1 + (cmd.indexOf("component") + "component".length()), cmd.length());
                        /**
                         *兼容支付宝
                         * alipays://platformapi/startapp?appId=10000007&sourceId=shortcut&source=shortcut#Intent;
                         * component=com.eg.android.AlipayGphone/com.alipay.mobile.quinox.LauncherActivity.alias;B.directly=true;B.fromDesktop=true;end
                         */
                        if (cmd.contains("/")) {
                            pkgName = pkgName.substring(0, pkgName.indexOf("/"));
                        }
                    } else if (cmd.contains("package")) {
                        pkgName = cmd.substring(1 + (cmd.indexOf("package") + "package".length()), cmd.length());
                        if (pkgName.contains(";")) {
                            pkgName = pkgName.substring(0, pkgName.indexOf(";"));
                        }
                    }
                    if (Xutils.isNotEmptyOrNull(pkgName)) {
                        Xutils.exec("pm enable " + pkgName);
                        App.getInstance().startActivity(Intent.parseUri(cmd, 0));
                    } else {
                        Xutils.tShort("快捷方式错误~");
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                Xutils.tShort("快捷方式错误~");
            }
        }
    }


    public static void debug(Object object) {
        try {
            Log.d("payplugs", String.valueOf(object));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void copyText(Context ctx, String text) {
        ClipboardManager cbm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        cbm.setText(text);
    }

    public static void openUrl(String url) {
        try {
            if (isNotEmptyOrNull(url) && !url.startsWith("http")) {
                url = "http://" + url;
            }
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.getInstance().getApplicationContext().startActivity(it);
        } catch (Exception e) {
            Xutils.tShort("请检查是否安装浏览器~");
        }
    }

    public static class RunOpenActivity extends AsyncTask<Void, Integer, Integer> {
        CardInfo info;
        int attempt;

        public RunOpenActivity(CardInfo info, int attempt) {
            this.info = info;
            this.attempt = attempt;
        }

        protected Integer doInBackground(Void... params) {
            int ret = AndroidCommand.noRoot;
            try {
                ret = Xutils.cmdExe(Constant.common_am_pre + info.getPackageName() + Constant.common_am_div + info.getClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer ret) {
            super.onPostExecute(ret);
            if (ret == AndroidCommand.appDisable) {
                if (attempt > 0) {
                    Xutils.exec(Constant.common_pm_e_p + info.getPackageName());
                    RunOpenActivity runOpenActivity = new RunOpenActivity(info, --attempt);
                    runOpenActivity.execute();
                } else {
                    Xutils.tShort(Constant.no_open);
                }
            } else if (ret == AndroidCommand.noRoot) {
                Xutils.tShort(Constant.no_root);
            }

        }
    }


    public static <T> boolean listNotNull(List<T> t) {
        if (t != null && t.size() > 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = App.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = App.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static boolean isNotEmptyOrNull(String string) {
        return !isEmptyOrNull(string);

    }

    public static boolean isEmptyOrNull(String string) {

        return string == null || string.trim().length() == 0 || string.equals("null");


    }

    public static int cmdExe(String cmdEnd) {
        String commands = cmdEnd;
        int ret = AndroidCommand.noRoot;
        if (Xutils.isNotEmptyOrNull(commands)) {
            ret = AndroidCommand.execRooted(commands);
            if (ret == AndroidCommand.noRoot) {
                try {
                    commands = commands.substring(0, commands.lastIndexOf("/"));
                    commands = commands.replaceAll("am start -n ", "");
                    startAppByPackageName(App.getInstance().getApplicationContext(), commands, Constant.attempt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }


    /**
     * 通过packagename启动应用
     */
    public static void startAppByPackageName(Context context, String packagename, int attempt) {
        try {
            Intent intent = isExit(context, packagename);
            context.startActivity(intent);
        } catch (Exception e) {
            exec(Constant.common_pm_e_p + packagename);
            if (attempt > 0) {
                startAppByPackageName(context, packagename, --attempt);
            } else {
                Xutils.tShort(Constant.no_open);
            }
        }

    }


    public static void tShort(String msg) {
        if (isEmptyOrNull(msg)) {
            return;
        }
        Toast.makeText(App.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    public static String exec(String command) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();

            process.waitFor();

            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Intent isExit(Context context, String pk_name) {
        if (isEmptyOrNull(pk_name)) {
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        Intent it = packageManager.getLaunchIntentForPackage(pk_name);
        return it;
    }

}
