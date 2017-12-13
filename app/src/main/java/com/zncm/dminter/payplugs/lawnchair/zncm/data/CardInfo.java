package com.zncm.dminter.payplugs.lawnchair.zncm.data;


import com.alibaba.fastjson.JSON;

import cn.bmob.v3.BmobObject;

/**
 * Created by dminter on 2016/7/22.
 * 活动实体
 */

public class CardInfo extends BmobObject {
    private String packageName;//包名
    private String className;//类名
    private String title;//标题
    private String iconUrl;
    private int iconRes;

    public CardInfo() {
    }
    public CardInfo(String packageName,int iconRes, String className, String title) {
        this.packageName = packageName;
        this.iconRes = iconRes;
        this.className = className;
        this.title = title;
    }


    public CardInfo(String packageName, String className, String title) {
        this.packageName = packageName;
        this.className = className;
        this.title = title;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String toString() {
        return JSON.toJSONString(this);
    }
}
