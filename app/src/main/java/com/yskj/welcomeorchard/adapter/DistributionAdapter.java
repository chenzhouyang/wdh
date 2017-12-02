package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.dialog.DistriQeCodeDialog;
import com.yskj.welcomeorchard.entity.DistriTionEntity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.StringUtils;

import java.util.List;

/**
 * 作者： chen
 * 时间： 2017/9/1
 * 描述：
 */

public class DistributionAdapter extends BaseAdapter {
    private Context context;
    private List<DistriTionEntity.DataBean> dataBeanList;
    private ModifyCountInterface modifyCountInterface;//更改数量

    public DistributionAdapter(Context context, List<DistriTionEntity.DataBean> dataBeanList) {
        this.context = context;
        this.dataBeanList = dataBeanList;
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
        return dataBeanList==null?0:dataBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dialog_diartion, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final DistriTionEntity.DataBean dataBean = dataBeanList.get(position);
        holder.diartion_name.setText(dataBean.goodsName);
        holder.diartion_price.setText("¥"+StringUtils.getStringtodouble(dataBean.specPrice));
        holder.diartion_spec.setText(dataBean.specName);
        GlideImage.loadImage(context,holder.diration_image,dataBean.cover,R.mipmap.img_error);
        //数量增加
        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doIncrease(position,holder.tvNum2,dataBean.stock);
            }
        });
        //数量减少
        holder.ivReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doDecrease(position,holder.tvNum2);
            }
        });
        //生成销售二维码
        holder.diartion_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DistriQeCodeDialog distriQeCodeDialog = new DistriQeCodeDialog(context,dataBean.ugsId+"",dataBean.getNum()+"",dataBean.cover+"");
                distriQeCodeDialog.show();
            }
        });
        return convertView;
    }
    //初始化控件
    class ViewHolder {
        TextView diartion_name,diartion_price,tvNum2,diartion_tv,diartion_spec;
        ImageView ivAdd,ivReduce,diration_image;
        public ViewHolder(View itemView) {
            diartion_name = (TextView) itemView.findViewById(R.id.diartion_name);
            diartion_price = (TextView) itemView.findViewById(R.id.diartion_price);
            tvNum2 = (TextView) itemView.findViewById(R.id.tvNum2);
            diartion_tv = (TextView) itemView.findViewById(R.id.diartion_tv);
            ivAdd = (ImageView) itemView.findViewById(R.id.ivAdd);
            ivReduce = (ImageView) itemView.findViewById(R.id.ivReduce);
            diartion_spec = (TextView) itemView.findViewById(R.id.diartion_spec);
            diration_image = (ImageView) itemView.findViewById(R.id.diration_image);
        }
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
         * @param stock       库存
         */
        void doIncrease(int position, View showCountView, int stock);

        /**
         * 删减操作
         *
         * @param position      组元素位置
         * @param showCountView 用于展示变化后数量的View
         */
        void doDecrease(int position, View showCountView);
    }
}
