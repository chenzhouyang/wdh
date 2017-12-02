package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.LocalServerEntity;
import com.yskj.welcomeorchard.entity.LocalServerNumEntity;
import com.yskj.welcomeorchard.entity.OrderListEntity;
import com.yskj.welcomeorchard.entity.ScanCodeTmpEntity;
import com.yskj.welcomeorchard.interfaces.OnShoppingCartChangeListener;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.ui.advertising.ChooseDishActivity;
import com.yskj.welcomeorchard.ui.localserver.LocalServerDetailActivity;
import com.yskj.welcomeorchard.ui.order.ConfirmOrderActivity;
import com.yskj.welcomeorchard.ui.order.OrderDetailActivity;
import com.yskj.welcomeorchard.ui.order.OrderPayActivity;
import com.yskj.welcomeorchard.ui.supply.ShoppingCartBiz;
import com.yskj.welcomeorchard.ui.supply.SourceParticularsActivity;
import com.yskj.welcomeorchard.ui.supply.SupplyConfirmActivity;
import com.yskj.welcomeorchard.utils.DecimalUtil;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.StringUtils;
import com.yskj.welcomeorchard.view.UIAlertView;
import com.yskj.welcomeorchard.widget.ToastHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 购物车adapter
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<LocalServerEntity> mListGoods = new ArrayList<>();
    private OnShoppingCartChangeListener mChangeListener;
    private boolean isSelectAll = false;
private LoadingCaches caches = LoadingCaches.getInstance();
    public MyExpandableListAdapter(Context context) {
        mContext = context;
    }

    public void setList(List<LocalServerEntity> mListGoods) {
        this.mListGoods = mListGoods;
        setSettleInfo();
    }

    public void setOnShoppingCartChangeListener(OnShoppingCartChangeListener changeListener) {
        this.mChangeListener = changeListener;
    }

    public View.OnClickListener getAdapterListener() {
        return listener;
    }

    @Override
    public int getGroupCount() {
        return mListGoods.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mListGoods.get(groupPosition).getLocalServerNumEntityList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListGoods.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mListGoods.get(groupPosition).getLocalServerNumEntityList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_elv_group_test, parent, false);
            holder.tvGroup = (TextView) convertView.findViewById(R.id.tvShopNameGroup);
            holder.tvEdit = (TextView) convertView.findViewById(R.id.tvEdit);
            holder.ivCheckGroup = (ImageView) convertView.findViewById(R.id.ivCheckGroup);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.tvGroup.setText(mListGoods.get(groupPosition).getShopname());
        ShoppingCartBiz.checkItem(mListGoods.get(groupPosition).isGroupSelected(), holder.ivCheckGroup);
        boolean isEditing = mListGoods.get(groupPosition).isEditing();
        if (isEditing) {
            holder.tvEdit.setText("完成");
        } else {
            holder.tvEdit.setText("编辑");
        }
        holder.ivCheckGroup.setTag(groupPosition);
        holder.tvGroup.setTag(groupPosition);
        holder.ivCheckGroup.setOnClickListener(listener);
        holder.tvEdit.setTag(groupPosition);
        holder.tvEdit.setOnClickListener(listener);
        holder.tvGroup.setOnClickListener(listener);
        return convertView;
    }

    /**
     * child view
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_elv_child_test, parent, false);
            holder.tvChild = (TextView) convertView.findViewById(R.id.tvItemChild);
            holder.tvDel = (TextView) convertView.findViewById(R.id.tvDel);
            holder.ivCheckGood = (ImageView) convertView.findViewById(R.id.ivCheckGood);
            holder.rlEditStatus = (RelativeLayout) convertView.findViewById(R.id.rlEditStatus);
            holder.llGoodInfo = (LinearLayout) convertView.findViewById(R.id.llGoodInfo);
            holder.ivAdd = (ImageView) convertView.findViewById(R.id.ivAdd);
            holder.ivReduce = (ImageView) convertView.findViewById(R.id.ivReduce);
            holder.tvGoodsParam = (TextView) convertView.findViewById(R.id.tvGoodsParam);
            holder.tvPriceNew = (TextView) convertView.findViewById(R.id.tvPriceNew);
            holder.tvPriceOld = (TextView) convertView.findViewById(R.id.tvPriceOld);
            holder.tvNum = (TextView) convertView.findViewById(R.id.tvNum);
            holder.tvNum2 = (TextView) convertView.findViewById(R.id.tvNum2);
            holder.ivGoods = (ImageView) convertView.findViewById(R.id.ivGoods);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        LocalServerNumEntity goods = mListGoods.get(groupPosition).getLocalServerNumEntityList().get(childPosition);
        boolean isChildSelected = mListGoods.get(groupPosition).getLocalServerNumEntityList().get(childPosition).isChildSelected();
        boolean isEditing = goods.isEditing();
        String priceNew = "¥" + goods.getPrice();
        String num = goods.getCount()+"";
        String pdtDesc = goods.getGoodsname();
        String goodName = mListGoods.get(groupPosition).getLocalServerNumEntityList().get(childPosition).getGoodsname();
        holder.tvPriceOld.setText(mListGoods.get(groupPosition).getLocalServerNumEntityList().get(childPosition).getParametername());
        holder.ivCheckGood.setTag(groupPosition + "," + childPosition);
        holder.llGoodInfo.setTag(groupPosition + "," + childPosition);
        holder.tvChild.setText(goodName);
        holder.tvPriceNew.setText(priceNew);
        holder.tvNum.setText("X " + num);
        holder.tvNum2.setText(num);
        holder.tvGoodsParam.setText(pdtDesc);
        GlideImage.loadImage(mContext,holder.ivGoods,mListGoods.get(groupPosition).getLocalServerNumEntityList().get(childPosition).getGoodsimage(),R.mipmap.img_error);
        holder.ivAdd.setTag(goods);
        holder.ivReduce.setTag(goods);
        holder.tvDel.setTag(groupPosition + "," + childPosition);
        holder.tvDel.setTag(groupPosition + "," + childPosition);

        ShoppingCartBiz.checkItem(isChildSelected, holder.ivCheckGood);
        if (isEditing) {
            holder.llGoodInfo.setVisibility(View.GONE);
            holder.rlEditStatus.setVisibility(View.VISIBLE);
        } else {
            holder.llGoodInfo.setVisibility(View.VISIBLE);
            holder.rlEditStatus.setVisibility(View.GONE);
        }

        holder.ivCheckGood.setOnClickListener(listener);
        holder.tvDel.setOnClickListener(listener);
        holder.ivAdd.setOnClickListener(listener);
        holder.ivReduce.setOnClickListener(listener);
        holder.llGoodInfo.setOnClickListener(listener);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    private String goodsid = "0";
    private String selectedMoney = "0";
    private String cloudOffer ="0";
    private String listgoods = "null";
    private String parameterid;
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //main
                case R.id.ivSelectAll:
                    isSelectAll = ShoppingCartBiz.selectAll(mListGoods, isSelectAll, (ImageView) v);
                    setSettleInfo();
                    notifyDataSetChanged();
                    break;
                case R.id.btnSettle:
                    if (ShoppingCartBiz.hasSelectedGoods(mListGoods)) {
                        if(caches.get("access_token").equals("null")){
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        }
                        List<LocalServerEntity> list = new ArrayList<>();
                        List<LocalServerNumEntity> numEntityList = new ArrayList<>();
                        boolean isAllChekd = true;
                        for (int i = 0; i < mListGoods.size(); i++) {
                            isAllChekd = true;
                            for (int j = 0; j < mListGoods.get(i).getLocalServerNumEntityList().size(); j++) {

                                boolean isCheckd = mListGoods.get(i).getLocalServerNumEntityList().get(j).isChildSelected();
                                if(isCheckd){
                                    if(isAllChekd){
                                        list.add(mListGoods.get(i));
                                        isAllChekd = false;
                                    }
                                    numEntityList.add(mListGoods.get(i).getLocalServerNumEntityList().get(j));
                                }
                            }
                        }
                     mContext.startActivity(new Intent(mContext, SupplyConfirmActivity.class)
                             .putExtra("list", (Serializable) list)
                     .putExtra("numEntityList", (Serializable) numEntityList));

                    } else {
                        ToastHelper.getInstance()._toast("亲，先选择商品！");
                    }
                    //group
                    break;
                case R.id.tvEdit://切换界面，属于特殊处理，假如没打算切换界面，则不需要这块代码
                    int groupPosition2 = Integer.parseInt(String.valueOf(v.getTag()));
                    boolean isEditing = !(mListGoods.get(groupPosition2).isEditing());
                    mListGoods.get(groupPosition2).setEditing(isEditing);
                    for (int i = 0; i < mListGoods.get(groupPosition2).getLocalServerNumEntityList().size(); i++) {
                        mListGoods.get(groupPosition2).getLocalServerNumEntityList().get(i).setEditing(isEditing);
                    }
                    notifyDataSetChanged();
                    break;
                case R.id.ivCheckGroup:
                    int groupPosition3 = Integer.parseInt(String.valueOf(v.getTag()));
                    isSelectAll = ShoppingCartBiz.selectGroup(mListGoods, groupPosition3);
                    selectAll();
                    setSettleInfo();
                    notifyDataSetChanged();
                    break;
                //child
                case R.id.ivCheckGood:
                    String tag = String.valueOf(v.getTag());
                    if (tag.contains(",")) {
                        String s[] = tag.split(",");
                        int groupPosition = Integer.parseInt(s[0]);
                        int childPosition = Integer.parseInt(s[1]);
                        isSelectAll = ShoppingCartBiz.selectOne(mListGoods, groupPosition, childPosition);
                        selectAll();
                        setSettleInfo();
                        notifyDataSetChanged();
                    }
                    break;
                case R.id.tvDel:
                    String tagPos = String.valueOf(v.getTag());
                    if (tagPos.contains(",")) {
                        String s[] = tagPos.split(",");
                        int groupPosition = Integer.parseInt(s[0]);
                        int childPosition = Integer.parseInt(s[1]);
                        showDelDialog(groupPosition, childPosition);
                    }
                    break;
                case R.id.ivAdd:
                    ShoppingCartBiz.addOrReduceGoodsNum(true, (LocalServerNumEntity) v.getTag(), ((TextView) (((View) (v.getParent())).findViewById(R.id.tvNum2))));
                    setSettleInfo();
                    break;
                case R.id.ivReduce:
                    ShoppingCartBiz.addOrReduceGoodsNum(false, (LocalServerNumEntity) v.getTag(), ((TextView) (((View) (v.getParent())).findViewById(R.id.tvNum2))));
                    setSettleInfo();
                    break;
                case R.id.llGoodInfo:
                    String tagPosintent = String.valueOf(v.getTag());

                    if (tagPosintent.contains(",")) {
                        String s[] = tagPosintent.split(",");
                        int groupPosition = Integer.parseInt(s[0]);
                        int childPosition = Integer.parseInt(s[1]);
                       mContext.startActivity(new Intent(mContext,SourceParticularsActivity.class)
                                .putExtra("goodsid",mListGoods.get(groupPosition).getGoodsid()+"")
                                .putExtra("imageurl",mListGoods.get(groupPosition).getLocalServerNumEntityList().get(childPosition).getGoodsimage())
                                .putExtra("shopname",mListGoods.get(groupPosition).getShopname()));
                    }
                    break;
                case R.id.tvShopNameGroup:
//                    String tagPosshop = String.valueOf(v.getTag());
//                    if (tagPosshop!=null) {
//                        int groupPosition = Integer.parseInt(tagPosshop);
//
//                    }
                    break;
            }
        }
    };
    private void selectAll() {
        if (mChangeListener != null) {
            mChangeListener.onSelectItem(isSelectAll);
        }
    }

    private void setSettleInfo() {
        String[] infos = ShoppingCartBiz.getShoppingCount(mListGoods);
        //删除或者选择商品之后，需要通知结算按钮，更新自己的数据；
        if (mChangeListener != null && infos != null) {
            mChangeListener.onDataChange(infos[0], infos[1]);
        }
    }

    private void showDelDialog(final int groupPosition, final int childPosition) {
        final UIAlertView delDialog = new UIAlertView(mContext, "温馨提示", "确认删除该商品吗?",
                "取消", "确定");
        delDialog.show();

        delDialog.setClicklistener(new UIAlertView.ClickListenerInterface() {

                                       @Override
                                       public void doLeft() {
                                           delDialog.dismiss();
                                       }

                                       @Override
                                       public void doRight() {
                                           String productID = mListGoods.get(groupPosition).getLocalServerNumEntityList().get(childPosition).getParameterid()+"";
                                           ShoppingCartBiz.delGood(productID);
                                           delGoods(groupPosition, childPosition);
                                           setSettleInfo();
                                           notifyDataSetChanged();
                                           delDialog.dismiss();
                                       }
                                   }
        );
    }

    private void delGoods(int groupPosition, int childPosition) {
        mListGoods.get(groupPosition).getLocalServerNumEntityList().remove(childPosition);
        if (mListGoods.get(groupPosition).getLocalServerNumEntityList().size() == 0) {
            ShoppingCartBiz.delAllGoods(mListGoods.get(groupPosition).getGoodsid()+"");
            mListGoods.remove(groupPosition);
        }
        notifyDataSetChanged();
    }

    class GroupViewHolder {
        TextView tvGroup;
        TextView tvEdit;
        ImageView ivCheckGroup;
    }

    class ChildViewHolder {
        /** 商品名称 */
        TextView tvChild;
        /** 商品规格 */
        TextView tvGoodsParam;
        /** 选中 */
        ImageView ivCheckGood;
        /** 非编辑状态 */
        LinearLayout llGoodInfo;
        /** 编辑状态 */
        RelativeLayout rlEditStatus;
        /** +1 */
        ImageView ivAdd;
        /** -1 */
        ImageView ivReduce;
        /** 删除 */
        TextView tvDel;
        /** 新价格 */
        TextView tvPriceNew;
        /** 旧价格 */
        TextView tvPriceOld;
        /** 商品状态的数量 */
        TextView tvNum;
        /** 编辑状态的数量 */
        TextView tvNum2;
        /**商品图片*/
        ImageView ivGoods;
    }
}
