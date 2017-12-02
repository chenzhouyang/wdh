package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.entity.PushMessageEntity;
import com.yskj.welcomeorchard.ui.web.MainImageActivity;
import com.yskj.welcomeorchard.utils.DateUtils;
import com.yskj.welcomeorchard.utils.GlideImage;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/18.
 */

public class PushMessageAdapter extends BaseAdapter {
    private ArrayList<PushMessageEntity.HistoryInfoBean> list;
    private Context context;
    public PushMessageAdapter(Context context,ArrayList<PushMessageEntity.HistoryInfoBean> list){
        super();
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        if(list!=null&&list.size()!=0){
            return list.size();
        }
        return 0;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder hoder = null;
        if(convertView == null){
            hoder = new ViewHoder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_poput_message,null);
            hoder.item_poput_image = (ImageView) convertView.findViewById(R.id.item_poput_image);
            hoder.item_poput_time = (TextView) convertView.findViewById(R.id.item_poput_time);
            convertView.setTag(hoder);
        }else {
            hoder = (ViewHoder) convertView.getTag();
        }
        final PushMessageEntity.HistoryInfoBean.NewsInfoBean newsInfoBeen = list.get(position).newsInfo;
        hoder.item_poput_time.setText(DateUtils.timeStampToStr(Long.parseLong(list.get(position).lastTime)));
        GlideImage.loadImage(context,hoder.item_poput_image,newsInfoBeen.thumb,R.mipmap.img_error);
        hoder.item_poput_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MainImageActivity.class).putExtra("url",Ips.PHPURL+newsInfoBeen.newsUrl));
            }
        });
        return convertView;
    }
    public class ViewHoder{
        TextView item_poput_time;
        ImageView item_poput_image;
    }
}
