package com.zncm.dminter.payplugs.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.jetradarmobile.snowfall.SnowfallView;
import com.zncm.dminter.payplugs.R;
import com.zncm.dminter.payplugs.lawnchair.zncm.data.CardInfo;
import com.zncm.dminter.payplugs.lawnchair.zncm.utils.SPHelper;
import com.zncm.dminter.payplugs.lawnchair.zncm.utils.Xutils;

import java.util.List;


/**
 * Created by jiaomx on 2017/4/10.
 */


//底部弹出菜单
public abstract class BottomSheetDlg {


    public BottomSheetDlg(Activity activity, List<CardInfo> cardInfos, boolean isSys) {

        final Dialog dialog = new Dialog(activity, R.style.MaterialDialogSheet);
        View view = activity.getLayoutInflater().inflate(R.layout.bottom_gridview, null);
        if (Xutils.listNotNull(cardInfos) && cardInfos.size() >= 16) {
            view = activity.getLayoutInflater().inflate(R.layout.bottom_gridview_half, null);
        }
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        RelativeLayout popupWindow = (RelativeLayout) view.findViewById(R.id.popupWindow);
        SnowfallView mSnowfallView = (SnowfallView) view.findViewById(R.id.mSnowfallView);

        if (SPHelper.isSnowFall(activity)){
            mSnowfallView.setVisibility(View.VISIBLE);
        }

        popupWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        MyGridAdapter myGridAdapter = new MyGridAdapter(activity);
        gridView.setAdapter(myGridAdapter);
        myGridAdapter.setItem(cardInfos);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onGridItemClickListener(position);
                dialog.dismiss();
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onGridItemLongClickListener(position);
                dialog.dismiss();
                return true;
            }
        });

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
//        checkSysDlg(activity, dialog);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                onOutClickListener();
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    public static void checkSysDlg(Context activity, Dialog dialog) {
//        if ((activity instanceof OpenInentActivity)) {
//            try {
//                if (!SettingsCompat.canDrawOverlays(activity)) {
//                    activity.startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }

    public abstract void onGridItemClickListener(int position);

    public abstract void onGridItemLongClickListener(int position);

    public abstract void onOutClickListener();


}
