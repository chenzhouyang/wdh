package com.yskj.welcomeorchard.ui.localserver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.utils.GlideImage;


/**
 * Created by jianghe on 2016/12/6 0006.
 */
public class NetworkCenterImgHolderView implements Holder<String> {
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        View contentView = LayoutInflater.from(context).inflate(R.layout.item_network_center_img_holder_view, null);
        imageView = (ImageView) contentView.findViewById(R.id.img_network);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return contentView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        imageView.setImageResource(R.mipmap.img_error);
        if(data.length()!=0){
            GlideImage.loadImage(context,imageView,data,R.mipmap.img_error);
        }else{
            imageView.setImageResource(R.mipmap.img_error);
        }
    }
}
