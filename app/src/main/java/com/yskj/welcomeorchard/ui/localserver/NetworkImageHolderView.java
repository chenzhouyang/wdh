package com.yskj.welcomeorchard.ui.localserver;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.utils.GlideImage;


/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements Holder<String> {
    private ImageView imageView;
    private int i = 0;

    public NetworkImageHolderView() {
        this.i = 0;
    }

    public NetworkImageHolderView(int i) {
        this.i = i;
    }

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        if (i == 0) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        imageView.setImageResource(R.mipmap.img_error);
        if (data.length() != 0) {
            GlideImage.loadImage(context,imageView,data,R.mipmap.img_error);
        } else {
            imageView.setImageResource(R.mipmap.img_error);
        }
    }
}
