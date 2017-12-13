package com.zncm.dminter.payplugs.lawnchair.zncm.data;

/**
 * Created by dminter on 2016/7/23.
 * 枚举类型
 */

public class EnumInfo {
    /**
     *活动类型
     */
    public enum cType {
        WX(1, "微信聊天"), TO_ACTIVITY(2, "TO_ACTIVITY"), JUST_TIPS(3, "JUST_TIPS"), START_APP(4, "START_APP"), QQ(5, "QQ聊天"), URL(6, "书签"), CMD(7, "CMD"), SHORT_CUT_SYS(8, "快捷方式");

        private int value;
        public String strName;

        private cType(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }


    }
}
