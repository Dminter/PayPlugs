package com.zncm.dminter.payplugs;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zncm.dminter.payplugs.lawnchair.zncm.data.CardInfo;
import com.zncm.dminter.payplugs.lawnchair.zncm.data.Constant;
import com.zncm.dminter.payplugs.lawnchair.zncm.utils.SPHelper;
import com.zncm.dminter.payplugs.lawnchair.zncm.utils.SuggestIntent;
import com.zncm.dminter.payplugs.lawnchair.zncm.utils.Xutils;
import com.zncm.dminter.payplugs.view.BottomSheetDlg;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Activity ctx;
    private List<CardInfo> cardInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);
        ctx = this;
        initPlugs();
    }


    public void initPlugs() {
        List<CardInfo> temp = SuggestIntent.initIntent();
        if (Xutils.listNotNull(temp)) {
            if (!SPHelper.isRootMode(ctx)) {
                for (CardInfo cardInfo : temp
                        ) {
                    if (Xutils.isEmptyOrNull(cardInfo.getPackageName()) || cardInfo.getPackageName().equals(Constant.pkg_name)) {
                        cardInfos.add(cardInfo);
                    }
                }
            } else {
                cardInfos.addAll(temp);
            }
        }
        cardInfos.add(new CardInfo(Constant.pkg_name, R.drawable.settings, SuggestIntent.activity_setting, "设置"));
        new BottomSheetDlg(ctx, cardInfos, false) {
            @Override
            public void onGridItemClickListener(int position) {
                Xutils.runCard(cardInfos.get(position));
                finish();
            }

            @Override
            public void onGridItemLongClickListener(int position) {

            }

            @Override
            public void onOutClickListener() {
                finish();

            }
        };


    }
}
