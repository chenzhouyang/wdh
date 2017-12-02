package com.yskj.welcomeorchard.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.PrizeNoticeEntity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

public class NoticeView extends LinearLayout {
    private static final String TAG = "LILITH";
    private Context mContext;
    private ViewFlipper viewFlipper;
    private View scrollTitleView;
    private ArrayList<PrizeNoticeEntity.ListBean> articleArrayList = new ArrayList();
    private boolean flag = true;

    /**
     * 构造
     *
     * @param context
     */
    public NoticeView(Context context) {
        super(context);
        mContext = context;
        init();
    }


    public NoticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();

    }

    /**
     * 网络请求后返回公告内容进行适配
     */
    protected void bindNotices() {
        // TODO Auto-generated method stub
        viewFlipper.removeAllViews();
        int i = 0;
        String notice = "";
        Logger.d("======" + articleArrayList.size());
        while (i < articleArrayList.size()) {
            String gotDes = "";
            if (i % 4 != 3) {
                gotDes = articleArrayList.get(i).nickname.toString() + articleArrayList.get(i).awardName.toString() + "\n";
            } else {
                gotDes = articleArrayList.get(i).nickname.toString() + articleArrayList.get(i).awardName.toString();
            }
            notice += gotDes;
            if (i % 4 == 3) {
                TextView textView = new TextView(mContext);
//                textView.setMaxLines(1);
//                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setTextColor(Color.parseColor("#ffffff"));
                textView.setPadding(6, 0, 0, 0);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);

                textView.setText(notice);
                LayoutParams lp = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                viewFlipper.addView(textView, lp);
                notice = "";
            }
            i++;
        }
    }


    private void init() {
        bindLinearLayout();
    }

    /**
     * 初始化自定义的布局
     */
    public void bindLinearLayout() {
        scrollTitleView = LayoutInflater.from(mContext).inflate(
                R.layout.notice_title_activity, null);
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(scrollTitleView, layoutParams);

        viewFlipper = (ViewFlipper) scrollTitleView
                .findViewById(R.id.flipper_scrollTitle);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.layout_in_from_top));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.layout_out_to_bottom));
        viewFlipper.startFlipping();
        View v = viewFlipper.getCurrentView();
    }

    public void getNotice() {
        OkHttpUtils.get().url(Urls.PRIZEGOT).build().execute(new MainNoticeCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(PrizeNoticeEntity response, int id) {
                if (response.errorCode.equals("000")) {
                    articleArrayList.clear();
                    articleArrayList = response.list;
                    bindNotices();
                    if (flag) {
                        if (articleArrayList.size() < 3) {
                            new Thread(
                                    new Runnable() {
                                        public void run() {
                                            try {
                                                Thread.sleep(5000);
                                                getNotice();
                                            } catch (InterruptedException e) {
                                                getNotice();
                                            }
                                        }
                                    }).start();

                        } else {
                            new Thread(
                                    new Runnable() {
                                        public void run() {
                                            try {
                                                System.out.println(articleArrayList.size() * 2500);
                                                Thread.sleep(articleArrayList.size() * 2500);
                                                getNotice();
                                            } catch (InterruptedException e) {
                                                getNotice();
                                            }
                                        }
                                    }).start();
                        }

                    }
                }
            }
        });
    }

    public abstract class MainNoticeCallBack extends Callback<PrizeNoticeEntity> {
        @Override
        public PrizeNoticeEntity parseNetworkResponse(Response response, int id) throws Exception {
            String string = response.body().string();
            PrizeNoticeEntity prizeNoticeEntity = new Gson().fromJson(string, PrizeNoticeEntity.class);
            return prizeNoticeEntity;
        }
    }

    /**
     * 公告title监听
     *
     * @author Nono
     */
    class NoticeTitleOnClickListener implements OnClickListener {
        private Context context;
        private String titleid;

        public NoticeTitleOnClickListener(Context context, String whichText) {
            this.context = context;
            this.titleid = whichText;
        }

        public void onClick(View v) {
            // TODO Auto-generated method stub
            disPlayNoticeContent(context, titleid);
        }

    }

    /**
     * 显示notice的具体内容
     *
     * @param context
     * @param titleid
     */
    public void disPlayNoticeContent(Context context, String titleid) {
        // TODO Auto-generated method stub
//        Toast.makeText(context, titleid, Toast.LENGTH_SHORT).show();
    }

    public void onStart() {
        flag = true;
    }

    public void onStop() {
        flag = false;
    }


}
