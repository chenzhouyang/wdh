package com.yskj.welcomeorchard.ui.qrcode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.WriterException;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.utils.EncodingHandlers;
import com.yskj.welcomeorchard.utils.LoadingCaches;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.path;

/**
 * Created by YSKJ-02 on 2017/1/15.
 */

public class QrCodeActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.img_erweima)
    ImageView imgErweima;
    @Bind(R.id.login_btn)
    Button loginBtn;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private UserInfoEntity userInfoEntity;
    private String spreadCode;
    private boolean issave = true;
    private Bitmap mBitmap;
    private String shareurl="";
    private Bitmap bitmap = null;
    // 图片宽度的一般
    private static final int IMAGE_HALFWIDTH = 30;
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(context, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(context, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(context, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
    private Handler handler=null;
    private Runnable runnableUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);
        txtTitle.setText("我的二维码");
        imageRight.setVisibility(View.VISIBLE);
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
        }.getType());
        spreadCode = userInfoEntity.data.userVo.spreadCode+"";
        //创建属于主线程的handler
        handler=new Handler();
         runnableUi=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                if(bitmap!=null&&imgErweima!=null){
                    imgErweima.setImageBitmap(bitmap);
                }

            }

        };

        new Thread(new Runnable(){
            @Override
            public void run() {
                if(userInfoEntity.data.userVo.avatar ==null||userInfoEntity.data.userVo.avatar.length()==0){
                    mBitmap = ((BitmapDrawable) getResources().getDrawable( R.mipmap.default_image)).getBitmap();
                }else {
                    try {
                        mBitmap = Glide.with(context)
                                .load(userInfoEntity.data.userVo.avatar)
                                .asBitmap() //必须
                                .centerCrop()
                                .into(500, 500)
                                .get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                SharedPreferences share2    = getSharedPreferences("mobile", 0);
                String mobile  = share2.getString("mobile", "null");
                shareurl = Ips.PHPURL+"/index.php?m=Mobile&c=User&a=reg&spreader="+spreadCode+"&QrCodeSHARE#"+mobile;//二维码中封装的字符串
                inits(shareurl,mBitmap);
                handler.post(runnableUi);
            }
        }).start();
// 构建Runnable对象，在runnable中更新界面


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//销毁线程
        handler.removeCallbacks(runnableUi);

    }
    @OnClick({R.id.img_back, R.id.image_right, R.id.img_erweima, R.id.login_btn})
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.image_right:
                break;
            case R.id.img_erweima:
                new AlertDialog.Builder(QrCodeActivity.this).setTitle("系统提示")//设置对话框标题
                        .setMessage("保存二维码？")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                if(issave){
                                    Bitmap bitmap = createViewBitmap(view);
                                    saveImageToGallery(context,bitmap);
                                }else {
                                    showToast("您已经保存过了");
                                }
                               issave = false;
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                    public void onClick(DialogInterface dialog, int which) {//响应事件

                        dialog.dismiss();
                    }
                }).show();//在按键响应事件中显示此对话框
                break;
            case R.id.login_btn:
                new ShareAction((Activity) context).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                        .withTitle("唯多惠注册")
//                        .withText("注册成为唯多惠会员，送10元本地生活消费大礼包，分享他人注册，再送5元/人。惠惠在这里等你哦！")
                        .withText("注册唯多惠让您身边的商家火起来")
                        .withMedia(new UMImage(context, R.mipmap.lsk_icon))
                        .withTargetUrl(Config.SHAREURL)
                        .setCallback(umShareListener)
                        .open();
                break;
        }
    }

    public Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }


    private void inits(String url,Bitmap mBitmap) {
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getHeight();
        m.setScale(sx, sy);
        // 重新构造一个40*40的图片
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
                mBitmap.getHeight(), m, false);

        try {
            bitmap = EncodingHandlers.cretaeBitmap(new String(url.getBytes(), "UTF-8"),mBitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 保存到本地
     * @param context
     * @param bmp
     */
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "/wdh");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpeg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
        Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
    }
    /**
     * 加载网络图片
     * @param url
     * @return
     */
    public Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        mBitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                mBitmap = BitmapFactory.decodeStream(is);
                is.close();
            }else{
                mBitmap = ((BitmapDrawable) getResources().getDrawable( R.mipmap.default_image)).getBitmap();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mBitmap;
    }
}
