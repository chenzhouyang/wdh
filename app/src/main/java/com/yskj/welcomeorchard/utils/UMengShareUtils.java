package com.yskj.welcomeorchard.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.widget.SimplexToast;

/**
 * 创建日期 2017/5/9on 17:46.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public  class UMengShareUtils  {
    public static void uMengShare(final Context context, final String umurl, final String goodsName, final String imgUrl){

        final UMShareListener umShareListener = new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA platform) {
                if (platform.name().equals("WEIXIN_FAVORITE")) {
                    SimplexToast.show(context, platform + " 收藏成功啦");
                } else {
                    SimplexToast.show(context, platform + " 分享成功啦");
                }
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                SimplexToast.show(context, platform + " 分享失败啦");
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                SimplexToast.show(context, platform + " 分享取消了");
            }
        };

        ShareBoardlistener shareBoardlistener = new  ShareBoardlistener() {

            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                if (share_media==null){
                    if (snsPlatform.mKeyword.equals(R.string.umeng_sharebutton_custom+"")){
                        //复制链接
                        // 从API11开始android推荐使用android.content.ClipboardManager
                        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        // 将文本内容放到系统剪贴板里。
                        cm.setText("复制整段文字,打开唯多惠App,查看此商品:"+goodsName+" \n" +
                                ",(未安装的用户可以点击"+umurl+"查看)");
                        Toast.makeText(context,"复制成功",Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    if (!TextUtils.isEmpty(imgUrl)){
                        new ShareAction((Activity) context).setPlatform(share_media).setCallback(umShareListener)
                                .withTitle("唯多惠精品")
                                .withText(goodsName+"\n"+context.getResources().getString(R.string.share_content))
                                .withMedia(new UMImage(context, Ips.PHPURL + "/" + imgUrl))
                                .withTargetUrl(umurl)
                                .share();
                    }else {
                        new ShareAction((Activity) context).setPlatform(share_media).setCallback(umShareListener)
                                .withTitle("唯多惠精品")
                                .withText(goodsName+"\n"+context.getResources().getString(R.string.share_content))
                                .withMedia(new UMImage(context, R.mipmap.img_error))
                                .withTargetUrl(umurl)
                                .share();
                    }

                }
            }
        };

        if (!TextUtils.isEmpty(imgUrl)){
            new ShareAction((Activity) context).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                    .addButton(R.string.umeng_sharebutton_custom+"",R.string.umeng_sharebutton_custom+"",R.mipmap.img_copy_url+"",R.mipmap.img_copy_url+"")
                    .setShareboardclickCallback(shareBoardlistener)
                    .withTitle("唯多惠精品")
                    .withText(goodsName+"\n"+context.getResources().getString(R.string.share_content))
                    .withMedia(new UMImage(context, Ips.PHPURL + "/" + imgUrl))
                    .withTargetUrl(umurl)
                    .setCallback(umShareListener)
                    .open();
        }else {
            new ShareAction((Activity) context).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                    .addButton(R.string.umeng_sharebutton_custom+"",R.string.umeng_sharebutton_custom+"",R.mipmap.img_copy_url+"",R.mipmap.img_copy_url+"")
                    .setShareboardclickCallback(shareBoardlistener)
                    .withTitle("唯多惠精品")
                    .withText(goodsName + "\n" + context.getResources().getString(R.string.share_content))
                    .withMedia(new UMImage(context, R.mipmap.img_error))
                    .withTargetUrl(umurl)
                    .setCallback(umShareListener)
                    .open();
        }

    }
}
