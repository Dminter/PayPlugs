package com.zncm.dminter.payplugs;

import android.app.Activity;
import android.os.Bundle;

import com.kenumir.materialsettings.MaterialSettings;
import com.kenumir.materialsettings.items.CheckboxItem;
import com.kenumir.materialsettings.items.DividerItem;
import com.kenumir.materialsettings.items.TextItem;
import com.kenumir.materialsettings.storage.StorageInterface;
import com.zncm.dminter.payplugs.lawnchair.zncm.data.Constant;
import com.zncm.dminter.payplugs.lawnchair.zncm.utils.SPHelper;
import com.zncm.dminter.payplugs.lawnchair.zncm.utils.Xutils;

/**
 * Created by jiaomx on 2017/12/14.
 */

public class SettingActivity extends MaterialSettings {
    private Activity ctx;

    @Override
    public StorageInterface initStorageInterface() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        addItem(new DividerItem(ctx));
        addItem(new CheckboxItem(this, "").setTitle("雪花").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {

            @Override
            public void onCheckedChange(CheckboxItem checkboxItem, boolean b) {
                SPHelper.setIsSnowFall(ctx, b);
            }
        }).setDefaultValue(SPHelper.isSnowFall(ctx)));

        addItem(new DividerItem(ctx));
        addItem(new CheckboxItem(this, "").setTitle("Root工作模式").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {

            @Override
            public void onCheckedChange(CheckboxItem checkboxItem, boolean b) {
                SPHelper.setIsRootMode(ctx, b);
            }
        }).setDefaultValue(SPHelper.isRootMode(ctx)));
        addItem(new TextItem(ctx, "").setTitle("更新").setSubtitle("当前版本：" + Xutils.getAppVersion(ctx.getPackageName())).setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                Xutils.openUrl(Constant.update_url);
            }
        }));

    }
}
