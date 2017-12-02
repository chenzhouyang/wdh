package com.yskj.welcomeorchard.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.ShoppingCartEntity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.NumberFormat;

import java.util.List;

/**
 * Created by YSKJ-JH on 2017/1/14.
 */

public class ShoppingCartAdapter extends BaseAdapter {
    private List<ShoppingCartEntity.CartListBean> shoppingCartBeanList;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private Context context;

    public ShoppingCartAdapter(Context context) {
        this.context = context;
    }

    public void setShoppingCartBeanList(List<ShoppingCartEntity.CartListBean> shoppingCartBeanList) {
        this.shoppingCartBeanList = shoppingCartBeanList;
        notifyDataSetChanged();
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
        return shoppingCartBeanList == null ? 0 : shoppingCartBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingCartBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shopping_cart, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ShoppingCartEntity.CartListBean shoppingCartBean = shoppingCartBeanList.get(position);
        holder.tv_commodity_name.setText(shoppingCartBean.goodsName);
        holder.tv_spec.setText(shoppingCartBean.specKeyName==null?"":shoppingCartBean.specKeyName);
        holder.tv_price.setText("￥" + String.format("%.2f", NumberFormat.convertToDouble(shoppingCartBean.goodsPrice,0.00)));
       /* if(shoppingCartBean.is_fail.equals("1")){
            //失效
            holder.lose_efficacy.setVisibility(View.VISIBLE);
            holder.ck_chose.setVisibility(View.GONE);
        }else {
            //不失效
            holder.lose_efficacy.setVisibility(View.GONE);
            holder.ck_chose.setVisibility(View.VISIBLE);
        }*/
        holder.ck_chose.setChecked(shoppingCartBean.isChoosed());
        holder.et_show_num.setText(shoppingCartBean.goodsNum + "");
        GlideImage.loadImage(context,holder.iv_show_pic,shoppingCartBean.originalImg,R.mipmap.img_error);
        //单选框按钮
        holder.ck_chose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shoppingCartBean.setChoosed(((CheckBox) v).isChecked());
                        checkInterface.checkGroup(position, ((CheckBox) v).isChecked());//向外暴露接口
                    }
                }
        );
        //增加按钮
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doIncrease(position, holder.et_show_num,shoppingCartBeanList.get(position).limited, holder.ck_chose.isChecked());//暴露增加接口
            }
        });
        //删减按钮
        holder.btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doDecrease(position, holder.et_show_num, holder.ck_chose.isChecked());//暴露删减接口
            }
        });
        //删除弹窗
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = new AlertDialog.Builder(context).create();
                alert.setTitle("操作提示");
                alert.setMessage("您确定要将这些商品从购物车中移除吗？");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                modifyCountInterface.childDelete(position);//删除 目前只是从item中移除

                            }
                        });
                alert.show();
            }
        });

        return convertView;
    }


    //初始化控件
    class ViewHolder {
        ImageView iv_show_pic,tv_delete;
        TextView tv_commodity_name,tv_price,tv_spec,lose_efficacy;
        CheckBox ck_chose;
        Button btn_sub,btn_add;
        EditText et_show_num;

        public ViewHolder(View itemView) {
            ck_chose = (CheckBox) itemView.findViewById(R.id.ck_chose);
            iv_show_pic = (ImageView) itemView.findViewById(R.id.iv_show_pic);
            btn_sub = (Button) itemView.findViewById(R.id.btn_sub);
            btn_add = (Button) itemView.findViewById(R.id.btn_add);
            tv_commodity_name = (TextView) itemView.findViewById(R.id.tv_commodity_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_delete = (ImageView) itemView.findViewById(R.id.tv_delete);
            et_show_num = (EditText) itemView.findViewById(R.id.et_show_num);
            tv_spec = (TextView) itemView.findViewById(R.id.tv_spec);
            lose_efficacy = (TextView) itemView.findViewById(R.id.lose_efficacy);
        }
    }
    /**
     * 复选框接口
     */
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
         * @param limited     限购
         */
        void doIncrease(int position, View showCountView,String limited, boolean isChecked);

        /**
         * 删减操作
         *
         * @param position      组元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int position, View showCountView, boolean isChecked);
        /**
         * 删除子item
         *
         * @param position
         */
        void childDelete(int position);
    }
}
