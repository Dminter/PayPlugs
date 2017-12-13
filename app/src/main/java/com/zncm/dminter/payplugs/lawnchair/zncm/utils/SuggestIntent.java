package com.zncm.dminter.payplugs.lawnchair.zncm.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.zncm.dminter.payplugs.R;
import com.zncm.dminter.payplugs.lawnchair.zncm.data.CardInfo;
import com.zncm.dminter.payplugs.lawnchair.zncm.data.Constant;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 建议活动的grid+cmd
 */
public class SuggestIntent {

    public static final String wx = "com.tencent.mm";
    public static final String zfb = "com.eg.android.AlipayGphone";
    public static final String wc = "com.wacai365";
    public static final String tb = "com.taobao.taobao";
    public static final String bili = "tv.danmaku.bili";
    public static final String dianping = "com.dianping.v1";
    public static final String youdaonote = "com.youdao.note";
    public static final String jingdong = "com.jingdong.app.mall";
    public static final String cloudmusic = "com.netease.cloudmusic";
    public static final String coolapk = "com.coolapk.market";
    public static final String douban = "com.douban.frodo";


    /**
     * 付款码
     */
    public static String zfb_fkm = "alipays://platformapi/startapp?appId=20000056&source=shortcut#Intent;component=com.eg.android.AlipayGphone/.FastStartActivity;B.directly=true;B.fromDesktop=true;end";
    /**
     * 扫一扫
     */
    public static String zfb_sys_short = "alipays://platformapi/startapp?appId=10000007&sourceId=shortcut&source=shortcut#Intent;component=com.eg.android.AlipayGphone/com.alipay.mobile.quinox.LauncherActivity.alias;B.directly=true;B.fromDesktop=true;end";
    /**
     * 收款码
     */
    public static String zfb_skm = "alipays://platformapi/startapp?appId=20000123&sourceId=shortcut&source=shortcut#Intent;component=com.eg.android.AlipayGphone/com.alipay.mobile.quinox.LauncherActivity.alias;B.directly=true;B.fromDesktop=true;end";
    public static String wx_sys = "#Intent;action=com.tencent.mm.action.BIZSHORTCUT;launchFlags=0x4000000;B.LauncherUI.From.Scaner.Shortcut=true;end";
    public static String zfb_ds = "alipays://zfb_ds";
    public static String zfb_url_code = "alipays://zfb_url_code//";
    public static String zfb_url_code_author = "alipays://zfb_url_code//c1x086063vy7pad3iuae9b5";
    public static String wx_dl = "wx://";
    public static String browser_dl = "browser://";
    public static String cmd_dl = "cmd://";
    public static String quick_dl = "quick://";

    public static List<CardInfo> initIntent() {
        List<CardInfo> cardInfos = new ArrayList<>();
        try {
            String pkgName = Constant.pkg_name;
            /**
             *缓存
             */
            String infoStr = SPHelper.getSuggestIntentByPkName(pkgName);
            if (Xutils.isNotEmptyOrNull(infoStr)) {
                cardInfos = JSON.parseArray(infoStr, CardInfo.class);
            }

            if (!Xutils.listNotNull(cardInfos)) {
                cardInfos.add(new CardInfo(pkgName,R.drawable.zfb_hb, zfb_url_code_author, "领红包"));
                cardInfos.add(new CardInfo(pkgName,R.drawable.zfb_scan, zfb_sys_short, "扫一扫"));
                cardInfos.add(new CardInfo(pkgName,R.drawable.zfb_payment, zfb_fkm, "付款码"));
                cardInfos.add(new CardInfo(pkgName,R.drawable.zfb_transfer, zfb_skm, "收款码"));
                cardInfos.add(new CardInfo(pkgName,R.drawable.zfb_yuebao, zfb_ds, "打赏作者"));
                cardInfos.add(new CardInfo(pkgName,R.drawable.weixin_sys, wx_sys, "扫一扫"));
            }


            /**
             *云端数据，缓存到缓存里面
             */
            BmobQuery query = new BmobQuery("CardInfo");
//            query.addWhereEqualTo("packageName", pkgName);
            query.addWhereNotEqualTo("order", -1);
            query.setLimit(50);
            query.order("-order");
            final String finalPkgName = pkgName;
            query.findObjects(new FindListener<CardInfo>() {
                @Override
                public void done(List<CardInfo> object, BmobException e) {
                    if (e == null) {
                        Log.i("bmob", "查询成功：共" + object.size() + "条数据。");
                        if (Xutils.listNotNull(object)) {
                            SPHelper.setSuggestIntentByPkName(finalPkgName, JSON.toJSONString(object));
                        }
                    } else {
                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cardInfos;

    }


    public static List<CardInfo> getSuggestIntentByPkName(String pkName) {
        List<CardInfo> cardInfos = new ArrayList();


        allInit(pkName, cardInfos);

        return cardInfos;
    }


    public static void allInit(String pkName, List<CardInfo> cardInfos) {
        /**
         *wx
         *
         * com.tencent.mm,com.tencent.mm.plugin.card.ui.CardHomePageUI,卡包
         com.tencent.mm,com.tencent.mm.plugin.luckymoney.f2f.ui.LuckyMoneyF2FQRCodeUI,面对面红包
         com.tencent.mm,com.tencent.mm.plugin.wallet.balance.ui.WalletBalanceManagerUI,零钱
         com.tencent.mm,com.tencent.mm.plugin.mall.ui.MallIndexUI,我的钱包
         com.tencent.mm,com.tencent.mm.plugin.favorite.ui.FavoriteIndexUI,我的收藏
         com.tencent.mm,com.tencent.mm.plugin.setting.ui.setting.SelfQRCodeUI,二维码名片
         com.tencent.mm,com.tencent.mm.plugin.appbrand.ui.AppBrandLauncherUI,小程序
         com.tencent.mm,com.tencent.mm.plugin.shake.ui.ShakeReportUI,摇一摇
         com.tencent.mm,com.tencent.mm.plugin.sns.ui.SnsMsgUI,朋友圈消息
         com.tencent.mm,com.tencent.mm.plugin.sns.ui.En_424b8e16,朋友圈
         com.tencent.mm,com.tencent.mm.plugin.brandservice.ui.BrandServiceIndexUI,公众号
         com.tencent.mm,com.tencent.mm.ui.contact.ChatroomContactUI,群聊
         com.tencent.mm,com.tencent.mm.ui.conversation.BizConversationUI,订阅号
         com.tencent.mm,com.tencent.mm.plugin.collect.ui.CollectMainUI,我要收款
         com.tencent.mm,com.tencent.mm.plugin.subapp.ui.pluginapp.AddMoreFriendsUI,添加朋友
         com.tencent.mm,com.tencent.mm.plugin.pwdgroup.ui.FacingCreateChatRoomAllInOneUI,面对面建群
         com.tencent.mm,com.tencent.mm.plugin.search.ui.FTSMainUI,微信搜索
         com.tencent.mm,com.tencent.mm.plugin.offline.ui.WalletOfflineCoinPurseUI,付款码
         com.tencent.mm,com.tencent.mm.plugin.scanner.ui.BaseScanUI,扫一扫
         */
        /**
         *String packageName, String className, String appName
         */
//        cardInfos = new ArrayList<>();
        if (SuggestAc.wx.equals(pkName)) {
            ArrayList<CardInfo> wxCard = new ArrayList<>();
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.scanner.ui.BaseScanUI", "扫一扫"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.offline.ui.WalletOfflineCoinPurseUI", "付款码"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.collect.ui.CollectMainUI", "我要收款"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.sns.ui.En_424b8e16", "朋友圈"));


            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.setting.ui.setting.SelfQRCodeUI", "二维码名片"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.mall.ui.MallIndexUI", "我的钱包"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.wallet.balance.ui.WalletBalanceManagerUI", "零钱"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.card.ui.CardHomePageUI", "卡包"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.luckymoney.f2f.ui.LuckyMoneyF2FQRCodeUI", "面对面红包"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.sns.ui.SnsMsgUI", "朋友圈消息"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.search.ui.FTSMainUI", "微信搜索"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.favorite.ui.FavoriteIndexUI", "我的收藏"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.appbrand.ui.AppBrandLauncherUI", "小程序"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.shake.ui.ShakeReportUI", "摇一摇"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.brandservice.ui.BrandServiceIndexUI", "公众号"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.ui.conversation.BizConversationUI", "订阅号"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.subapp.ui.pluginapp.AddMoreFriendsUI", "添加朋友"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.ui.contact.ChatroomContactUI", "群聊"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.pwdgroup.ui.FacingCreateChatRoomAllInOneUI", "面对面建群"));


            cardInfos.addAll(wxCard);
        } else if (SuggestAc.zfb.equals(pkName)) {
            ArrayList<CardInfo> zfbCard = new ArrayList<>();
            /**
             *   com.eg.android.AlipayGphone,com.alipay.android.phone.wealth.tally.activitys.TallyMainActivity_,记账本
             com.eg.android.AlipayGphone,com.alipay.mobile.stock.ui.MainActivity,股票
             com.eg.android.AlipayGphone,com.alipay.mobile.pubsvc.ui.PPServiceActivity,生活号
             com.eg.android.AlipayGphone,com.alipay.mobile.transferapp.ui.TransferHomeActivity_,转账
             com.eg.android.AlipayGphone,com.alipay.mobile.alipassapp.ui.list.activity.v2.OffersEntryActivity,卡包
             com.eg.android.AlipayGphone,com.alipay.mobile.bill.list.ui.BillListActivity_,账单
             com.eg.android.AlipayGphone,com.alipay.mobile.payee.ui.PayeeQRActivity_,收钱
             com.eg.android.AlipayGphone,com.alipay.mobile.security.accountmanager.AccountInfo.ui.SecurityAccountQrCodeActivity_,我的二维码
             com.eg.android.AlipayGphone,com.alipay.mobile.contactsapp.ui.AddFriendActivity_,添加朋友
             com.eg.android.AlipayGphone,com.alipay.mobile.socialcontactsdk.contact.ui.ContactMainPageActivity,通讯录
             com.eg.android.AlipayGphone,com.alipay.android.phone.businesscommon.globalsearch.ui.MainSearchActivity,搜索
             com.eg.android.AlipayGphone,com.alipay.android.phone.voiceassistant.ui.VoiceAssistantActivity,语音搜索
             com.eg.android.AlipayGphone,com.alipay.android.phone.personalapp.favorite.activity.FavoriteNewHomeActivity,我的收藏
             com.eg.android.AlipayGphone,com.alipay.mobile.bill.list.ui.BillSearchActivity_,账单搜索
             com.eg.android.AlipayGphone,com.alipay.mobile.fund.ui.FundMainNewActivity_,余额宝
             com.eg.android.AlipayGphone,com.alipay.mobile.onsitepay9.payer.OspTabHostActivity,付款码
             com.eg.android.AlipayGphone,com.alipay.mobile.scan.as.main.MainCaptureActivity,扫一扫
             */


//            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.onsitepay9.payer.OspTabHostActivity", "付款码"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.bill.list.ui.BillListActivity_", "账单"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.security.accountmanager.AccountInfo.ui.SecurityAccountQrCodeActivity_", "我的二维码"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.android.phone.businesscommon.globalsearch.ui.MainSearchActivity", "搜索"));

            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.android.phone.wealth.tally.activitys.TallyMainActivity_", "记账本"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.pubsvc.ui.PPServiceActivity", "生活号"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.transferapp.ui.TransferHomeActivity_", "转账"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.alipassapp.ui.list.activity.v2.OffersEntryActivity", "卡包"));
//            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.payee.ui.PayeeQRActivity_", "收钱"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.contactsapp.ui.AddFriendActivity_", "添加朋友"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.socialcontactsdk.contact.ui.ContactMainPageActivity", "通讯录"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.android.phone.personalapp.favorite.activity.FavoriteNewHomeActivity", "我的收藏"));
//            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.bill.list.ui.BillSearchActivity_", "账单搜索"));
//            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.fund.ui.FundMainNewActivity_", "余额宝"));

//            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.scan.as.main.MainCaptureActivity", "扫一扫"));
            cardInfos.addAll(zfbCard);
        } else if (SuggestAc.tb.equals(pkName)) {
            /**
             **com.taobao.taobao,com.taobao.tao.rate.ui.myrate.MyRateActivity,我的评价
             com.taobao.taobao,com.tmall.market.plugin.main.MainTabActivity,天猫超市
             com.taobao.taobao,com.taobao.ju.android.ui.main.TabMainActivity,聚划算
             com.taobao.taobao,com.taobao.takeout.TakeoutMainActivity,淘宝外卖
             com.taobao.taobao,com.taobao.search.searchdoor.SearchDoorActivity,搜索
             com.taobao.taobao,com.etao.feimagesearch.FEISCaptureActivity,拍立淘
             //             com.taobao.taobao,com.taobao.taobao.scancode.gateway.activity.ScancodeGatewayActivity,扫一扫
             com.taobao.taobao,com.taobao.android.trade.cart.CartActivity,购物车
             com.taobao.taobao,com.taobao.order.list.OrderListActivity,我的订单
             com.taobao.taobao,com.taobao.favorites.NewFavoriteGoodsActivity,我的收藏
             com.taobao.taobao,com.taobao.tao.mytaobao.MyTaoBaoActivity,我的淘宝
             com.taobao.taobao,com.taobao.tao.homepage.MainActivity3,淘宝
             */
            ArrayList<CardInfo> card = new ArrayList<>();
//            card.add(new CardInfo(SuggestAc.tb, "com.taobao.tao.homepage.MainActivity3", "淘宝"));
//            card.add(new CardInfo(SuggestAc.tb, "com.taobao.taobao.scancode.gateway.activity.ScancodeGatewayActivity", "扫一扫"));
            card.add(new CardInfo(SuggestAc.tb, "com.taobao.tao.mytaobao.MyTaoBaoActivity", "我的淘宝"));
            card.add(new CardInfo(SuggestAc.tb, "com.taobao.order.list.OrderListActivity", "我的订单"));
            card.add(new CardInfo(SuggestAc.tb, "com.taobao.favorites.NewFavoriteGoodsActivity", "我的收藏"));
            card.add(new CardInfo(SuggestAc.tb, "com.taobao.android.trade.cart.CartActivity", "购物车"));
            card.add(new CardInfo(SuggestAc.tb, "com.taobao.search.searchdoor.SearchDoorActivity", "搜索"));
            card.add(new CardInfo(SuggestAc.tb, "com.etao.feimagesearch.FEISCaptureActivity", "拍立淘"));
            card.add(new CardInfo(SuggestAc.tb, "com.taobao.tao.rate.ui.myrate.MyRateActivity", "我的评价"));
            card.add(new CardInfo(SuggestAc.tb, "com.tmall.market.plugin.main.MainTabActivity", "天猫超市"));
            card.add(new CardInfo(SuggestAc.tb, "com.taobao.ju.android.ui.main.TabMainActivity", "聚划算"));
            card.add(new CardInfo(SuggestAc.tb, "com.taobao.takeout.TakeoutMainActivity", "淘宝外卖"));
            cardInfos.addAll(card);
        } else if (SuggestAc.wc.equals(pkName)) {
            /**
             com.wacai365,com.wacai365.account.AccountManagerActivity,账户
             com.wacai365,com.wacai365.statement.StatChart,报表
             com.wacai365,com.wacai365.detail.DetailsSummary,明细
             com.wacai365,com.wacai365.InputTrade,记账
             com.wacai365,com.wacai365.HomeActivity,挖财
             */
            ArrayList<CardInfo> card = new ArrayList<>();
//            card.add(new CardInfo(SuggestAc.wc, "com.wacai365.HomeActivity", "挖财"));
            card.add(new CardInfo(SuggestAc.wc, "com.wacai365.InputTrade", "记账"));
            card.add(new CardInfo(SuggestAc.wc, "com.wacai365.detail.DetailsSummary", "明细"));
//            card.add(new CardInfo(SuggestAc.wc, "com.wacai365.account.AccountManagerActivity", "账户"));
//            card.add(new CardInfo(SuggestAc.wc, "com.wacai365.statement.StatChart", "报表"));
            cardInfos.addAll(card);
        } else if (SuggestAc.dianping.equals(pkName)) {
            /**
             com.dianping.v1,com.dianping.search.history.HistoryActivity,浏览历史
             com.dianping.v1,com.dianping.user.favorite.FavoriteBaseActivity,我的收藏
             com.dianping.v1,com.dianping.v1.NovaMainActivity,大众点评
             */
            ArrayList<CardInfo> card = new ArrayList<>();
//            card.add(new CardInfo(SuggestAc.dianping, "com.dianping.v1.NovaMainActivity", "大众点评"));
            card.add(new CardInfo(SuggestAc.dianping, "com.dianping.search.history.HistoryActivity", "浏览历史"));
            cardInfos.addAll(card);
        } else if (SuggestAc.bili.equals(pkName)) {
            /**
             tv.danmaku.bili,tv.danmaku.bili.MainActivity,哔哩哔哩
             tv.danmaku.bili,tv.danmaku.bili.ui.rank.RankPagerActivity,排行榜
             tv.danmaku.bili,tv.danmaku.bili.ui.videodownload.VideoDownloadListActivity,我的下载
             */
            ArrayList<CardInfo> card = new ArrayList<>();
//            card.add(new CardInfo(SuggestAc.bili, "tv.danmaku.bili.MainActivity", "哔哩哔哩"));
            card.add(new CardInfo(SuggestAc.bili, "tv.danmaku.bili.ui.rank.RankPagerActivity", "排行榜"));
            card.add(new CardInfo(SuggestAc.bili, "tv.danmaku.bili.ui.videodownload.VideoDownloadListActivity", "我的下载"));
            cardInfos.addAll(card);
        } else if (SuggestAc.jingdong.equals(pkName)) {
            /**
             com.jingdong.app.mall,com.jd.lib.favourites.FavoListFragmentActivity,我的收藏
             com.jingdong.app.mall,com.jd.lib.ordercenter.myGoodsOrderList.view.activity.MyOrderListActivity,我的订单
             com.jingdong.app.mall,com.jd.lib.search.view.Activity.SearchActivity,搜索
             com.jingdong.app.mall,com.jd.lib.scan.lib.zxing.client.android.CaptureActivity,扫一扫
             com.jingdong.app.mall,com.jingdong.app.mall.MainFrameActivity,京东
             */
            ArrayList<CardInfo> card = new ArrayList<>();
//            card.add(new CardInfo(SuggestAc.jingdong, "com.jingdong.app.mall.MainFrameActivity", "京东"));
            card.add(new CardInfo(SuggestAc.jingdong, "com.jd.lib.ordercenter.myGoodsOrderList.view.activity.MyOrderListActivity", "我的订单"));
            card.add(new CardInfo(SuggestAc.jingdong, "com.jd.lib.search.view.Activity.SearchActivity", "搜索"));
            card.add(new CardInfo(SuggestAc.jingdong, "com.jd.lib.scan.lib.zxing.client.android.CaptureActivity", "扫一扫"));
            card.add(new CardInfo(SuggestAc.jingdong, "com.jd.lib.favourites.FavoListFragmentActivity", "我的收藏"));
            cardInfos.addAll(card);
        } else if (SuggestAc.youdaonote.equals(pkName)) {
            /**
             com.youdao.note,com.youdao.note.activity2.YDocGlobalSearchAcitivity,搜索
             com.youdao.note,com.youdao.note.activity2.TextNoteActivity,记笔记
             com.youdao.note,com.youdao.note.activity2.YDocShorthandActivity,语音笔记
             com.youdao.note,com.youdao.note.activity2.MainActivity,有道云笔记
             */
            ArrayList<CardInfo> card = new ArrayList<>();
//            card.add(new CardInfo(SuggestAc.youdaonote, "com.youdao.note.activity2.MainActivity", "有道云笔记"));
            card.add(new CardInfo(SuggestAc.youdaonote, "com.youdao.note.activity2.TextNoteActivity", "记笔记"));
            card.add(new CardInfo(SuggestAc.youdaonote, "com.youdao.note.activity2.YDocGlobalSearchAcitivity", "搜索"));
            card.add(new CardInfo(SuggestAc.youdaonote, "com.youdao.note.activity2.YDocShorthandActivity", "语音笔记"));
            cardInfos.addAll(card);
        } else if (SuggestAc.cloudmusic.equals(pkName)) {
            /**
             com.netease.cloudmusic,com.netease.cloudmusic.activity.MyRecentMusicActivity,最近播放
             com.netease.cloudmusic,com.netease.cloudmusic.activity.MainActivity,网易云音乐
             com.netease.cloudmusic,com.netease.cloudmusic.activity.SearchActivity,搜索
             com.netease.cloudmusic,com.netease.cloudmusic.activity.ArtistCategoryActivity,歌手分类
             com.netease.cloudmusic,com.netease.cloudmusic.activity.ScanMusicActivity,本地音乐
             com.netease.cloudmusic,com.netease.cloudmusic.activity.PlayerRadioActivity,私人FM
             com.netease.cloudmusic,com.netease.cloudmusic.activity.DailyRcmdMusicActivity,每日推荐
             */
            ArrayList<CardInfo> card = new ArrayList<>();
//            card.add(new CardInfo(SuggestAc.cloudmusic, "com.netease.cloudmusic.activity.MainActivity", "网易云音乐"));
            card.add(new CardInfo(SuggestAc.cloudmusic, "com.netease.cloudmusic.activity.ScanMusicActivity", "本地音乐"));
            card.add(new CardInfo(SuggestAc.cloudmusic, "com.netease.cloudmusic.activity.DailyRcmdMusicActivity", "每日推荐"));
            card.add(new CardInfo(SuggestAc.cloudmusic, "com.netease.cloudmusic.activity.PlayerRadioActivity", "私人FM"));
            card.add(new CardInfo(SuggestAc.cloudmusic, "com.netease.cloudmusic.activity.MyRecentMusicActivity", "最近播放"));
            card.add(new CardInfo(SuggestAc.cloudmusic, "com.netease.cloudmusic.activity.SearchActivity", "搜索"));
            card.add(new CardInfo(SuggestAc.cloudmusic, "com.netease.cloudmusic.activity.ArtistCategoryActivity", "歌手分类"));
            cardInfos.addAll(card);
        } else if (SuggestAc.coolapk.equals(pkName)) {
            /**
             com.coolapk.market,com.coolapk.market.view.main.MainActivity,酷安
             com.coolapk.market,com.coolapk.market.view.user.UserHistoryListActivity,浏览历史
             com.coolapk.market,com.coolapk.market.view.collection.FavoriteActivity,我的收藏
             com.coolapk.market,com.coolapk.market.view.user.UserFollowAppActivity,我的关注
             com.coolapk.market,com.coolapk.market.view.appmanager.AppManagerActivity,应用管理
             com.coolapk.market,com.coolapk.market.view.notification.NotificationActivity,通知
             com.coolapk.market,com.coolapk.market.view.search.SearchExtendActivity,搜索
             */
            ArrayList<CardInfo> card = new ArrayList<>();
//            card.add(new CardInfo(SuggestAc.coolapk, "com.coolapk.market.view.main.MainActivity", "酷安"));
            card.add(new CardInfo(SuggestAc.coolapk, "com.coolapk.market.view.appmanager.AppManagerActivity", "应用管理"));
            card.add(new CardInfo(SuggestAc.coolapk, "com.coolapk.market.view.search.SearchExtendActivity", "搜索"));
            card.add(new CardInfo(SuggestAc.coolapk, "com.coolapk.market.view.notification.NotificationActivity", "通知"));
            card.add(new CardInfo(SuggestAc.coolapk, "com.coolapk.market.view.user.UserFollowAppActivity", "我的关注"));
            card.add(new CardInfo(SuggestAc.coolapk, "com.coolapk.market.view.collection.FavoriteActivity", "我的收藏"));
            card.add(new CardInfo(SuggestAc.coolapk, "com.coolapk.market.view.user.UserHistoryListActivity", "浏览历史"));
            cardInfos.addAll(card);
        } else if (SuggestAc.douban.equals(pkName)) {
            /**
             com.douban.frodo,com.douban.frodo.search.activity.SearchActivity,搜索
             com.douban.frodo,com.douban.frodo.search.activity.CaptureActivity,扫一扫
             com.douban.frodo,com.douban.frodo.MainActivity,豆瓣
             */
            ArrayList<CardInfo> card = new ArrayList<>();
//            card.add(new CardInfo(SuggestAc.douban, "com.douban.frodo.MainActivity", "豆瓣"));
            card.add(new CardInfo(SuggestAc.douban, "com.douban.frodo.search.activity.SearchActivity", "搜索"));
            card.add(new CardInfo(SuggestAc.douban, "com.douban.frodo.search.activity.CaptureActivity", "扫一扫"));
            cardInfos.addAll(card);
        }

    }


}
