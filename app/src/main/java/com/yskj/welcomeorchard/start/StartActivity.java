package com.yskj.welcomeorchard.start;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AppIntroEntity;
import com.yskj.welcomeorchard.home.HomeActivity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.widget.CountDownView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by YSKJ-02 on 2017/1/19.
 */

public class StartActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @Bind(R.id.countDownView)
    CountDownView countDownView;
    private ArrayList image;
    private AppIntroEntity convenientBannerEntities;
    private boolean guide = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        countDownView.start();
        //获取引导图
        getCB();
        //广告倒计时
        getdownview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }

    private void getdownview() {

        countDownView.setCountDownTimerListener(new CountDownView.CountDownTimerListener() {
            @Override
            public void onStartCount() {
            }

            @Override
            public void onFinishCount() {
                if (!guide){
                    startActivity(new Intent(context, HomeActivity.class));
                }
            }
        });
        countDownView.setOnClickListener(this);
    }

    private void getCB() {
        OkHttpUtils.get().url(Urls.APPINTRO).build().execute(new ConvenientBannerCallBack());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.countDownView:
                guide = true;
                startActivity(new Intent(context, HomeActivity.class));
                break;
        }
    }

    public class ConvenientBannerCallBack extends Callback<AppIntroEntity> {
        @Override
        public AppIntroEntity parseNetworkResponse(Response response, int id) throws Exception {
            String string = response.body().string();
            convenientBannerEntities = new Gson().fromJson(string, new TypeToken<AppIntroEntity>() {
            }.getType());
            return convenientBannerEntities;
        }

        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(AppIntroEntity response, int id) {
            //添加轮播图
            image = new ArrayList();
            ArrayList<AppIntroEntity.AdListBean> adList = response.adList;
            if(adList!=null&&adList.size()!=0){
                for (int i = 0; i< adList.size(); i++){
                    image.add(adList.get(i).adCode);
                }
            }else {
                return;
            }

            //初始化首页轮播图
            initCB();
        }
    }


    private void initCB() {
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, image)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.dot_dark, R.drawable.dot_red})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        //开始自动翻页
        convenientBanner.startTurning(6000/image.size());
    }

    //轮播图适配图片
    public class NetworkImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            imageView.setImageResource(R.mipmap.img_error);
            GlideImage.loadImage(context,imageView,data,R.mipmap.img_error);
        }
    }
}
