package com.yskj.welcomeorchard.ui.payment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.WriterException;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.utils.EncodingHandlers;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.zxing.activity.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by YSKJ-02 on 2017/2/25.
 */

public class PayMentActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.payment_num)
    ImageView paymentNum;
    @Bind(R.id.payment_capital)
    TextView paymentCapital;
    @Bind(R.id.payment_code)
    TextView paymentCode;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private boolean ispaycode = false;
    private TimeCount time;
    private Bitmap mBitmap;
    private Bitmap bitmap;
    private UserInfoEntity userInfoEntity;
    // 图片宽度的一般
    private static final int IMAGE_HALFWIDTH = 30;
    private Handler handler=null;
    private String qrcodetrue;
    private Runnable runnableUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        txtTitle.setText("付款");
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
        }.getType());
        handler=new Handler();
        initview();
    }

    public void initview() {
        time.start();
        OkHttpUtils.get().url(Urls.QRCODEFK)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("codeType", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                stopMyDialog();
            }

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                startMyDialog();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {

                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if (code == 0) {
                    String qrcode = (String) map.get("data");
                   qrcodetrue = "payCode#" + qrcode;
                    handler=new Handler();
                      runnableUi=new  Runnable(){
                        @Override
                        public void run() {
                            //更新界面
                            if(bitmap!=null&&paymentNum!=null){
                                paymentNum.setImageBitmap(bitmap);
                            }

                        }

                    };

                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            if(userInfoEntity.data.userVo.avatar ==null||userInfoEntity.data.userVo.avatar.length()==0){
                                mBitmap = ((BitmapDrawable) getResources().getDrawable( R.mipmap.default_image)).getBitmap();
                            }else {
                                mBitmap =  returnBitMap(userInfoEntity.data.userVo.avatar);
                            }
                            inits(qrcodetrue,mBitmap);
                            handler.post(runnableUi);
                        }
                    }).start();
                }
            }
        });
    }



    @OnClick({R.id.img_back, R.id.payment_capital,R.id.payment_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                time.cancel();
                AppManager.getInstance().killActivity(PayMentActivity.class);
                break;
            case R.id.payment_capital:
               startActivity(new Intent(context, CaptureActivity.class));
                break;
            case R.id.payment_code:
                    initview();
                break;
        }
    }
    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发.
            initview();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableUi);
        time.cancel();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        time.cancel();
        AppManager.getInstance().killActivity(PayMentActivity.class);
        return super.onKeyDown(keyCode, event);

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
    private void inits(String url,Bitmap mBitmap) {
        if(mBitmap!=null){
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


    }
}
