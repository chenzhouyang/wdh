package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.dialog.ActivityDialog;
import com.yskj.welcomeorchard.entity.AllFragmentListTimeEntity;
import com.yskj.welcomeorchard.ui.commoditydetails.CommodityDetailsActiviy;
import com.yskj.welcomeorchard.ui.web.MainImageActivity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.widget.BetterRecyclerView;
import com.yskj.welcomeorchard.widget.CountDownClock;

import java.util.ArrayList;

/**
 * 创建日期 2017/4/12on 10:51.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AllFragmentHeadListTimeAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AllFragmentListTimeEntity.PromotionListBean> arrayList;
    private StaggeredGridLayoutManager layoutManager;

    public AllFragmentHeadListTimeAdapter(Context context, ArrayList<AllFragmentListTimeEntity.PromotionListBean> allFragmentListTimeEntityArrayList) {
        this.context = context;
        this.arrayList = allFragmentListTimeEntityArrayList;
    }

    @Override
    public int getCount() {
        if (arrayList == null || arrayList.size() == 0)
            return 0;
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_all_fragment_head_list_time, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AllFragmentListTimeEntity.PromotionListBean promotionListBean = arrayList.get(position);
        holder.tvName.setText(promotionListBean.positionInfo.positionName);
        GlideImage.loadImage(context, holder.imgIcon, promotionListBean.positionInfo.thumb, R.mipmap.img_error);
        GlideImage.loadImage(context, holder.imgBig, promotionListBean.adInfo.get(0).adCode, R.mipmap.img_error);
        holder.imgBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //type 0:不跳  1：商品 2：文章  3：其他
                if (promotionListBean.adInfo.get(0).adType.equals("0")) {
                    //Toast.makeText(context,"敬请期待...",Toast.LENGTH_SHORT).show();
                } else if (promotionListBean.adInfo.get(0).adType.equals("1")) {
                    context.startActivity(new Intent(context, CommodityDetailsActiviy.class).putExtra("goodid", promotionListBean.adInfo.get(0).adLink));
                } else if (promotionListBean.adInfo.get(0).adType.equals("2")) {
                    context.startActivity(new Intent(context, MainImageActivity.class).putExtra("url", promotionListBean.adInfo.get(0).adLink));
                }
            }
        });
        if (arrayList.get(position).activity_code != null && !arrayList.get(position).activity_code.equals("n_to_buy")) {
            holder.item_all_head_time.setVisibility(View.VISIBLE);
            holder.countDownClock.setTimes(Long.parseLong(promotionListBean.goodsList.get(0).restTimeToEnd));
        } else {
            holder.item_all_head_time.setVisibility(View.GONE);
        }
        //已经在倒计时的时候不再开启计时
        if (!holder.countDownClock.isRun()) {
            holder.countDownClock.beginRun();
        }
        if (promotionListBean.goodsList == null) {
            holder.recyclerView.setVisibility(View.GONE);
        } else {
            holder.recyclerView.setVisibility(View.VISIBLE);
            AllFragmentHoriListAdapter adapter = new AllFragmentHoriListAdapter(context, promotionListBean.activity_code, promotionListBean.goodsList);
            holder.recyclerView.setHasFixedSize(true);
            int spanCount = 1; // 只显示一行
            layoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new AllFragmentHoriListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int pos) {
                    AllFragmentListTimeEntity.PromotionListBean.GoodsListBean goodsListBean = promotionListBean.goodsList.get(pos);
                    if (promotionListBean.activity_code != null && promotionListBean.activity_code.equals("n_to_buy")) {
                        if (goodsListBean.is_open != null && goodsListBean.is_end.equals("1")) {
                            ActivityDialog dialog = new ActivityDialog(context, "商品已售完，是否查看获奖名单？", "0", goodsListBean.id);
                            dialog.show();
                            return;
                        }
                        if (goodsListBean.is_open != null && goodsListBean.is_open.equals("0")) {
                            ActivityDialog dialog = new ActivityDialog(context, "暂未开启，敬请期待", "1", goodsListBean.id);
                            dialog.show();
                            return;
                        }

                    } else if (goodsListBean.is_end.equals("2")) {
                        ActivityDialog dialog = new ActivityDialog(context, "商品已售完", "1", goodsListBean.id);
                        dialog.show();
                        return;
                    }
                    context.startActivity(new Intent(context, CommodityDetailsActiviy.class)
                            .putExtra("goodid", goodsListBean.goodsId));
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        ImageView imgIcon, imgBig;
        TextView tvName;
        CountDownClock countDownClock;
        BetterRecyclerView recyclerView;
        LinearLayout item_all_head_time;

        public ViewHolder(View itemview) {
            tvName = (TextView) itemview.findViewById(R.id.tv_name);
            imgBig = (ImageView) itemview.findViewById(R.id.img_big);
            imgIcon = (ImageView) itemview.findViewById(R.id.img_icon);
            countDownClock = (CountDownClock) itemview.findViewById(R.id.count_down_clock);
            recyclerView = (BetterRecyclerView) itemview.findViewById(R.id.recycler_view);
            item_all_head_time = (LinearLayout) itemview.findViewById(R.id.item_all_head_time);
        }
    }
}
