package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.ParticularsEntitiy;
import com.yskj.welcomeorchard.utils.DoubleUtils;
import com.yskj.welcomeorchard.utils.StringUtils;

import java.util.List;

/**
 * 作者： chen
 * 时间： 2017/8/28
 * 描述：微商货源商品详情规格
 */

public class SourceParAdapter extends BaseAdapter {
    private Context context;
    private List<ParticularsEntitiy.DataBean.ParameterListBean> parameterListBeanList;
    private CheckInterface checkInterface;//选中
    private ModifyCountInterface modifyCountInterface;//更改数量

    public SourceParAdapter(Context context, List<ParticularsEntitiy.DataBean.ParameterListBean> parameterListBeanList) {
        this.context = context;
        this.parameterListBeanList = parameterListBeanList;
    }
    /**
     * 单选接口
     *
     * @param checkInterface
     */
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }
    /**
     * 改变商品数量接口
     *
     * @param modifyCountInterface
     */
    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    @Override
    public int getCount() {
        return parameterListBeanList == null ? 0 : parameterListBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_collect_money, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ParticularsEntitiy.DataBean.ParameterListBean parameterListBean = parameterListBeanList.get(position);
        holder.item_collect_name.setText(parameterListBean.name);
        holder.item_collect_price.setText("¥"+StringUtils.getStringtodouble(parameterListBean.price));
        holder.tvPriceOld.setText("赠送积分"+StringUtils.getStringtodouble(DoubleUtils.div(parameterListBean.mAccount,10,2)));
        if(parameterListBeanList.get(position).isGroupSelected()){
            holder.ivCheckGood.setChecked(true);
        }else {
            holder.ivCheckGood.setChecked(false);
        }
        //选中
        holder.ivCheckGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parameterListBean.setGroupSelected(((CheckBox) v).isSelected());
                checkInterface.checkGroup(position, ((CheckBox) v).isChecked());//向外暴露接口
            }
        });
        //增加数量
        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doIncrease(position,holder.tvNum2,parameterListBean.stock,parameterListBean.isGroupSelected());
            }
        });
        //减少数量
        holder.ivReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doDecrease(position,holder.tvNum2,parameterListBean.isGroupSelected());
            }
        });
        return convertView;
    }
    //初始化控件
    class ViewHolder {
        TextView item_collect_name,item_collect_price,tvGoodsParam,tvPriceOld,tvNum2;
        CheckBox ivCheckGood;
        ImageView ivAdd,ivReduce;
        public ViewHolder(View itemView) {
            item_collect_price = (TextView) itemView.findViewById(R.id.tvPriceNew);
            item_collect_name = (TextView) itemView.findViewById(R.id.tvItemChild);
            tvGoodsParam = (TextView) itemView.findViewById(R.id.tvGoodsParam);
            tvPriceOld = (TextView) itemView.findViewById(R.id.tvPriceOld);
            tvNum2 = (TextView) itemView.findViewById(R.id.tvNum2);
            ivCheckGood = (CheckBox) itemView.findViewById(R.id.ivCheckGood);
            ivReduce = (ImageView) itemView.findViewById(R.id.ivReduce);
            ivAdd = (ImageView) itemView.findViewById(R.id.ivAdd);
        }
    }
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param position  元素位置
         * @param isChecked 元素选中与否
         */
        void checkGroup(int position, boolean isChecked);

    }
    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param position      组元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         * @param stock       库存
         */
        void doIncrease(int position, View showCountView, int stock, boolean isChecked);

        /**
         * 删减操作
         *
         * @param position      组元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int position, View showCountView, boolean isChecked);
    }
}
