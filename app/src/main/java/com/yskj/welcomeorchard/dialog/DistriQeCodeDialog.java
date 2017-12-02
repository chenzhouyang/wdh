package com.yskj.welcomeorchard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.baidu.mapapi.common.Logger;
import com.bumptech.glide.Glide;
import com.google.zxing.WriterException;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.utils.EncodingHandlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * 作者： chen
 * 时间： 2017/9/1
 * 描述：分销生成的二维码
 */

public class DistriQeCodeDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private String ugsid,num;
    private ImageView img_del,img_erweima;
    private static final int IMAGE_HALFWIDTH = 30;
    private Bitmap mBitmap;
    private Bitmap bitmap;
    private    Handler handler;
    private String qrcodetrue;
    private Runnable runnableUi;
    private String imageurl;

    public DistriQeCodeDialog(Context context,String ugsid,String num,String imageurl) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.num = num;
        this.ugsid = ugsid;
        this.imageurl = imageurl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_distriqecode);
        iniview();
    }

    private void iniview() {
         handler = new Handler();
        img_del = (ImageView) findViewById(R.id.img_del);
        img_erweima = (ImageView) findViewById(R.id.img_erweima);
        img_del.setOnClickListener(this);
        qrcodetrue = "Sales#" + ugsid+"#"+num;
        handler=new Handler();
        runnableUi=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                if(bitmap!=null&&img_erweima!=null){
                    img_erweima.setImageBitmap(bitmap);
                }
            }

        };
        new Thread(new Runnable(){
            @Override
            public void run() {
                if(imageurl!=null&&imageurl.length()!=0&&imageurl.contains("http")){
                    try {
                        mBitmap = Glide.with(context)
                                .load(imageurl)
                                .asBitmap() //必须
                                .centerCrop()
                                .into(500, 500)
                                .get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }else {
                    mBitmap = ((BitmapDrawable) context.getResources().getDrawable(R.mipmap.img_error)).getBitmap();
                }
                com.orhanobut.logger.Logger.d(mBitmap+"");
                inits(qrcodetrue,mBitmap);
                handler.post(runnableUi);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_del:
                dismiss();
                break;
        }
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
