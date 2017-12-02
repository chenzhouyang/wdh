package com.yskj.welcomeorchard.ui.localserver;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.LocalServerDetailMealAdapter;
import com.yskj.welcomeorchard.adapter.LocalServerDetailRecommendAadpter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.HintLoginDialog;
import com.yskj.welcomeorchard.entity.LocalServerDetailBean;
import com.yskj.welcomeorchard.entity.LocalServerPurchaseNoteBean;
import com.yskj.welcomeorchard.ui.order.OrderPayActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.StringUtils;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.yskj.welcomeorchard.widget.SimplexToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.yskj.welcomeorchard.R.id.img_share;


/**
 * Created by Administrator on 2016/11/8 0008.
 */
public class LocalServerDetailActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.image_del)
    ImageView imageDel;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(img_share)
    ImageView imgShare;
    @Bind(R.id.ll_title)
    LinearLayout llTitle;
    @Bind(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_des)
    TextView tvDes;
    @Bind(R.id.ll_head)
    LinearLayout llHead;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_send)
    TextView tvSend;
    @Bind(R.id.tv_buy)
    TextView tvBuy;
    @Bind(R.id.ll_group)
    LinearLayout llGroup;
    @Bind(R.id.ll_one)
    LinearLayout llOne;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv_sell)
    TextView tvSell;
    @Bind(R.id.tv_name1)
    TextView tvName1;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_distance)
    TextView tvDistance;
    @Bind(R.id.image_button_tel)
    ImageButton imageButtonTel;
    @Bind(R.id.listView)
    NoScrollListView listView;
    @Bind(R.id.ll_meal)
    LinearLayout llMeal;
    @Bind(R.id.tv_note)
    TextView tvNote;
    @Bind(R.id.tv_buy_notice)
    TextView tvBuyNotice;
    @Bind(R.id.tv_recommend)
    TextView tvRecommend;
    @Bind(R.id.listView_recommend)
    NoScrollListView listViewRecommend;
    @Bind(R.id.scrollView)
    PullToRefreshScrollView scrollView;
    @Bind(R.id.ll_two)
    LinearLayout llTwo;
    @Bind(R.id.convenientBanner_img)
    ConvenientBanner convenientBannerImg;
    @Bind(R.id.local_server_ll)
    LinearLayout localServerLl;
    private ArrayList image;
    private String phoneno = "";
    private String lifeId, sell, distance;
    private LocalServerDetailBean.DataBean ret_data;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private UMShareListener umShareListener = new UMShareListener() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ThemeColor.setTranslucentStatus(this, R.color.local_server_title);
        setContentView(R.layout.activity_local_server_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        lifeId = intent.getStringExtra("life_id");
        sell = intent.getStringExtra("sell");
        distance = intent.getStringExtra("distance");
        imgShare.setVisibility(View.VISIBLE);
        initScrollView();
        initData();
        initListener();

    }

    private void initListener() {
        imageDel.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        tvBuy.setOnClickListener(this);
        imageButtonTel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_del:
                finish();
                break;
            case R.id.img_share:
                String url = "null";
//                if (caches.get("access_token").equals("null")) {
//                    url = Ips.PHPURL + "/App/Local/goods_info/id/" + lifeId + "/spreader/";
//                } else {
//                    url = Ips.PHPURL + "/App/Local/goods_info/id/" + lifeId + "/spreader/" + caches.get("spreadCode");
//                }
                new ShareAction((Activity) context).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                        .withTitle("唯多惠最牛店主")
                        .withText(ret_data.name)
                        .withMedia(new UMImage(context, ret_data.cover))
                        .withTargetUrl(Config.SHAREURL)
                        .setCallback(umShareListener)
                        .open();
                break;
            case R.id.tv_buy:
                if (caches.get("access_token").equals("null")) {
                    HintLoginDialog dialog = new HintLoginDialog(LocalServerDetailActivity.this);
                    dialog.show();
                } else {
                    Intent intent = new Intent(LocalServerDetailActivity.this, OrderPayActivity.class);
                    intent.putExtra("type", "1");
                    intent.putExtra("name", ret_data.name);
                    intent.putExtra("price", ret_data.teamBuyPrice + "");
                    intent.putExtra("cloudOffset", ret_data.cloudOffset + "");
                    intent.putExtra("lifeId", ret_data.id + "");
                    startActivity(intent);
                }

                break;
            case R.id.image_button_tel:
                if (phoneno == null || "".equals(phoneno.trim())) {
                    Toast.makeText(getApplicationContext(), "暂时没用电话号码",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent0 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneno));
                    startActivity(intent0);
                }
                break;
        }
    }

    private void initData() {
        OkHttpUtils.get().url(Urls.LOCALSERVERDETAIL + lifeId).build().execute(new LocalServerDetailCallBack());
    }

    private class LocalServerDetailCallBack extends Callback<LocalServerDetailBean> {
        @Override
        public void onBefore(Request request, int id) {
            startMyDialog();
            super.onBefore(request, id);
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            stopMyDialog();
            scrollView.onRefreshComplete();
        }

        @Override
        public LocalServerDetailBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.d(s);
            LocalServerDetailBean localServerDetailBean = new Gson().fromJson(s, new TypeToken<LocalServerDetailBean>() {
            }.getType());
            return localServerDetailBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
            stopMyDialog();
            scrollView.onRefreshComplete();
        }

        @Override
        public void onResponse(LocalServerDetailBean response, int id) {
            if(response.code == 0){
                initUI(response);
            }else {
                showdialog(MessgeUtil.geterr_code(response.code));
            }

        }
    }

    private void initUI(LocalServerDetailBean localServerDetailBean) {
        image = new ArrayList();
        ret_data = localServerDetailBean.data;
        image.add(ret_data.cover);
        //设置轮播图
        getShuffingImage();
        tvName.setText(ret_data.name);
        tvDes.setText(ret_data.profile);
        tvPrice.setText("￥" + StringUtils.getStringtodouble(ret_data.teamBuyPrice));
        tvSell.setText("已售" + sell);
        if (ret_data.cloudOffset == 0) {
            tvSend.setVisibility(View.GONE);
        } else {
            tvSend.setVisibility(View.VISIBLE);
            tvSend.setText("抵" + StringUtils.getStringtodouble(ret_data.cloudOffset) + "积分");
        }
        tvName1.setText(ret_data.localShop.shopName);
        tvAddress.setText(ret_data.localShop.detailAddress);
        if (distance == null || distance.length() == 0 || distance.equals("")) {
            tvDistance.setVisibility(View.GONE);
        } else {
            tvDistance.setText(distance);
        }
        phoneno = ret_data.localShop.mobile;
        //套餐
        listView.setFocusable(false);
        if (ret_data.setMealList == null || ret_data.setMealList.size() == 0) {
            llMeal.setVisibility(View.GONE);
        } else {
            llMeal.setVisibility(View.VISIBLE);
            listView.setAdapter(new LocalServerDetailMealAdapter(context, ret_data.setMealList));
        }
        tvNote.setText("备注：" + ret_data.localShop.profile);
        String validity = "<font color=\"#f08e1f\">有效期</font>" + "<br />\u3000\u3000" + ret_data.onlineTime + " 至 " + ret_data.offlineTime;
        String useTime = "<font color=\"#f08e1f\">使用时间</font>" + "<br />\u3000\u3000" + ret_data.consumeStartTime + ":00" + "至" +
                ret_data.consumeEndTime + ":00";
        String useRule = "<font color=\"#f08e1f\">使用规则</font>";
        String purchaseNote = ret_data.purchaseNote;
        StringBuilder purchase = new StringBuilder(useRule);
        ArrayList<LocalServerPurchaseNoteBean> useRuleList = new Gson().fromJson(purchaseNote, new TypeToken<ArrayList<LocalServerPurchaseNoteBean>>() {
        }.getType());
        if (useRuleList != null && useRuleList.size() != 0) {
            localServerLl.setVisibility(View.VISIBLE);
            for (LocalServerPurchaseNoteBean note : useRuleList) {
                purchase.append("<br />\u3000\u3000" + note.title + "　　 " + note.content.replace("#{val}", note.val));
            }
        }else {
            localServerLl.setVisibility(View.GONE);
        }
        tvBuyNotice.setText(Html.fromHtml(validity + "<br />" + useTime + "<br />" + purchase));
        if (ret_data.otherLifes == null || ret_data.otherLifes.size() == 0) {
            tvRecommend.setVisibility(View.GONE);
            listViewRecommend.setVisibility(View.GONE);
        } else {
            tvRecommend.setVisibility(View.VISIBLE);
            listViewRecommend.setVisibility(View.VISIBLE);
            listViewRecommend.setFocusable(false);
            listViewRecommend.setAdapter(new LocalServerDetailRecommendAadpter(context, ret_data.otherLifes, ret_data.name));
            listViewRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    lifeId = ret_data.otherLifes.get(i).lifeId + "";
                    sell = ret_data.otherLifes.get(i).saleCount + "";
                    initData();
                    scrollView.getRefreshableView().scrollTo(0, 0);
                }
            });
        }
    }

    private void getShuffingImage() {
        //设置播放的图片
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView(1);
                    }
                }, image)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.dot_dark, R.drawable.dot_red})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showImg();
            }
        });
    }

    void showImg() {
        convenientBannerImg.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new NetworkCenterImgHolderView();
            }
        }, image)//设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.dot_dark, R.drawable.dot_red})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        convenientBannerImg.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        convenientBannerImg.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                convenientBannerImg.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initScrollView() {
        scrollView.setMode(PullToRefreshBase.Mode.BOTH);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                scrollView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                scrollView.onRefreshComplete();
            }
        });
        scrollView.setScrollListener(new PullToRefreshScrollView.ScrollListener() {
            @Override
            public void onScroll(ScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y >= llHead.getHeight()) {
                    if (llGroup.getParent() != llTwo) {
                        llOne.removeView(llGroup);
                        llTwo.addView(llGroup);
                    }
                } else {
                    if (llGroup.getParent() != llOne) {
                        llTwo.removeView(llGroup);
                        llOne.addView(llGroup);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //开始自动翻页
        convenientBanner.startTurning(3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();
    }
}
