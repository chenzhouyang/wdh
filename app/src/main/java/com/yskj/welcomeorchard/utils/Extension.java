package com.yskj.welcomeorchard.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created with Android Studio.
 * 作者: 陈宙洋
 * 日期: 2016/8/12 10:53
 * 分享时随机生成的文字介绍
 */
public class Extension {
   private static   String[] CONTENT = new String[]{"我有19名朋友在这里抢购，邀请你一起来，注册还有大礼包哦", "全民提前抢尖货,一折秒杀倒计时1:53:02", "选你所购,\"爱\"痛快,大牌快到碗里来,一大波福利向你涌来"};
    public static String tocontent(){
        int index=(int)(Math.random()*CONTENT.length);
       String content = CONTENT[index];
        return content;
    }

    /**
     * 判断是否安装微信
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

}
