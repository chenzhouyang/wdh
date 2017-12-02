package com.yskj.welcomeorchard.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AdvertisingDraftEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.ui.advertising.VersionWebActivity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;

/**
 * 创建日期 2017/3/16on 9:59.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AdvertisingDraftAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<AdvertisingDraftEntity.AdrListBean> arrayList;
    private int uid;
    private UserInfoEntity userInfoEntity;
    private LoadingCaches caches = LoadingCaches.getInstance();
    public AdvertisingDraftAdapter(Context context, ArrayList<AdvertisingDraftEntity.AdrListBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
        }.getType());
        uid = userInfoEntity.data.userVo.id;
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
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_advertising_draft,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(arrayList.get(position).title!=null){
            holder.title.setText(arrayList.get(position).title);
        }else {
            holder.title.setVisibility(View.INVISIBLE);
        }

        if(arrayList.get(position).create_time!=null){
            holder.draft_time.setText(arrayList.get(position).create_time);
        }else {
            holder.draft_time.setVisibility(View.INVISIBLE);
        }

        GlideImage.loadImage(context,holder.imageView,arrayList.get(position).thumb,R.mipmap.img_error);
        holder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                        .setMessage("确定删除吗？")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                sureDel(position);
                            }
                        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        dialog.dismiss();
                    }
                }).show();//在按键响应事件中显示此对话框
            }
        });
        holder.draft_compile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,VersionWebActivity.class);
                intent.putExtra("url",Urls.ADDRAFTEDIT+"/rid/"+arrayList.get(position).adRedId+"/uid/"+uid);
                intent.putExtra("cid",arrayList.get(position).class_id);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private void sureDel(final int position) {
        OkHttpUtils.get().url(Urls.DELDRAFT+"/uid/"+arrayList.get(position).userId+"/rid/"+arrayList.get(position).adRedId).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(context,"服务器正忙，稍等...",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Logger.json(response);
                Map<String,Object> map = JSONFormat.jsonToMap(response);
                String code = (String) map.get("error_code");
                if(code.equals("000")){
                    Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                    arrayList.remove(arrayList.get(position));
                    notifyDataSetChanged();
                }else {
                    Toast.makeText(context,code,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class ViewHolder {
        ImageView imageView;
        TextView title,imgDel,draft_compile,draft_time;
        public ViewHolder(View itemview) {
            title = (TextView) itemview.findViewById(R.id.tv_title);
            imageView = (ImageView) itemview.findViewById(R.id.image);
            imgDel = (TextView) itemview.findViewById(R.id.img_del);
            draft_compile = (TextView) itemview.findViewById(R.id.draft_compile);
            draft_time = (TextView) itemview.findViewById(R.id.draft_time);
        }
    }


}
