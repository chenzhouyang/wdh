package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.GoodsBean;
import com.yskj.welcomeorchard.ui.advertising.ChooseDishActivity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.StringUtils;

import java.util.List;

/**
 * Created by chenzhouyang on 2016/5/24 0024.
 */

public class GoodsAdapter extends BaseAdapter {
    private List<GoodsBean.DataBean.LocalLifesBean> list;
    private Context context;
    private CatograyAdapter catograyAdapter;
    private ModifySkip modifySkip;
    public GoodsAdapter(Context context, List<GoodsBean.DataBean.LocalLifesBean> list2, CatograyAdapter catograyAdapter) {
        this.context=context;
        this.list=list2;
        this.catograyAdapter=catograyAdapter;
    }
    public void setModifySkip(ModifySkip modifySkip){
        this.modifySkip = modifySkip;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Viewholder viewholder;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.shopcart_right_listview,null);
            viewholder=new Viewholder();
            viewholder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            viewholder.tv_original_price= (TextView) convertView.findViewById(R.id.tv_original_price);
            viewholder.tv_price= (TextView) convertView.findViewById(R.id.tv_price);
            viewholder.iv_add= (ImageView) convertView.findViewById(R.id.iv_add);
            viewholder.iv_remove= (ImageView) convertView.findViewById(R.id.iv_remove);
            viewholder.tv_acount= (TextView) convertView.findViewById(R.id.tv_acount);
            viewholder.iv_pic= (ImageView) convertView.findViewById(R.id.iv_pic);
            viewholder.rl_item= (RelativeLayout) convertView.findViewById(R.id.rl_item);
            convertView.setTag(viewholder);
        }else {
            viewholder = (Viewholder) convertView.getTag();

        }
        viewholder.tv_name.setText(list.get(position).lifeName);
        viewholder.tv_original_price.setText("抵用金"+ StringUtils.getStringtodouble(list.get(position).cloudOffset));
        viewholder.tv_price.setText("￥"+StringUtils.getStringtodouble(list.get(position).teamBuyPrice));


        if(list.get(position)!=null){
        //默认进来数量
            if (list.get(position).getNum()<1){
                viewholder.tv_acount.setVisibility(View.INVISIBLE);
                viewholder.iv_remove.setVisibility(View.INVISIBLE);
                catograyAdapter.notifyDataSetChanged();
            }else{
                viewholder.tv_acount.setVisibility(View.VISIBLE);
                viewholder.iv_remove.setVisibility(View.VISIBLE);
                viewholder.tv_acount.setText(String.valueOf(list.get(position).getNum()));
                catograyAdapter.notifyDataSetChanged();
            }
        }else{
            viewholder.tv_acount.setVisibility(View.INVISIBLE);
            viewholder.iv_remove.setVisibility(View.INVISIBLE);
        }

        //商品图片
        if(list.get(position).lifeCover!=null){
            GlideImage.loadImage(context,viewholder.iv_pic,list.get(position).lifeCover,R.mipmap.img_error);
        }
        //数量增加
        viewholder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = ((ChooseDishActivity)context).getSelectedItemCountById(list.get(position).lifeId);
                if (count < 1) {
                    viewholder.iv_remove.setAnimation(getShowAnimation());
                    viewholder.iv_remove.setVisibility(View.VISIBLE);
                    viewholder.tv_acount.setVisibility(View.VISIBLE);
                }

                ((ChooseDishActivity)context).handlerCarNum(1,list.get(position),true);
                catograyAdapter.notifyDataSetChanged();

                int[] loc = new int[2];
                viewholder.iv_add.getLocationInWindow(loc);
                int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                ImageView ball = new ImageView(context);
                ball.setImageResource(R.drawable.number);
                ((ChooseDishActivity)context).setAnim(ball, startLocation);// 开始执行动画

            }
        });
//数量减少
        viewholder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = ((ChooseDishActivity)context).getSelectedItemCountById(list.get(position).lifeId);
                if (count < 2) {
                    viewholder.iv_remove.setAnimation(getHiddenAnimation());
                    viewholder.iv_remove.setVisibility(View.GONE);
                    viewholder.tv_acount.setVisibility(View.GONE);
                }
                ((ChooseDishActivity)context).handlerCarNum(0,list.get(position),true);
                catograyAdapter.notifyDataSetChanged();

            }
        });

        viewholder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifySkip.skip(position);
            }
        });
        return convertView;
    }
    class Viewholder{
        TextView tv_name,tv_original_price,tv_price;
        ImageView iv_add,iv_remove,iv_pic;
        TextView tv_acount;
        RelativeLayout rl_item;
    }

public interface ModifySkip{
    /**
     *
     * @param postion
     */
    void skip(int postion);
}

    //显示减号的动画
    private Animation getShowAnimation(){
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0,720,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,2f
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(0,1);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }
    //隐藏减号的动画
    private Animation getHiddenAnimation(){
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0,720,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,2f
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(1,0);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }



}
