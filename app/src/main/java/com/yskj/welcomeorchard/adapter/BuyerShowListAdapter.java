package com.yskj.welcomeorchard.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.BuyersShowEntity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.widget.HorizontalListView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.R.attr.path;

/**
 * Created by YSKJ-JH on 2017/1/22.
 */

public class BuyerShowListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<BuyersShowEntity.CommentlistBean> arrayList;
    private LoadingCaches caches = LoadingCaches.getInstance();

    public BuyerShowListAdapter(Context context, ArrayList<BuyersShowEntity.CommentlistBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_buy_show_list,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final BuyersShowEntity.CommentlistBean commentlistBean = arrayList.get(position);
        holder.textView.setText(commentlistBean.content);
        BuyerShowListImgAdapter adapter = new BuyerShowListImgAdapter(context, commentlistBean.img);
        holder.horizontalListView.setAdapter(adapter);
        //复制text
        holder.ll_copy_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(commentlistBean.content+"唯多惠:点击前往"+Urls.UMSTR + arrayList.get(position).goodsId + "&spreader=" + caches.get("spreadCode"));
                Toast.makeText(context,"复制成功",Toast.LENGTH_SHORT).show();
            }
        });
        //保存图片
        holder.ll_save_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < commentlistBean.img.size(); i++) {
                    final int finalI = i;
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            Bitmap bitmap = getBitmapFromUrl(context,Ips.PHPURL+commentlistBean.img.get(finalI));
                            saveImageToGallery(context,bitmap);
                        }
                    }.start();
                }
                Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    /**
     * 根据图片的url路径获得Bitmap对象
     * @return
     */
    public Bitmap getBitmapFromUrl(Context context,String urlStr){
        URL url = null;
        Bitmap bitmap = null;
        HttpURLConnection connection = null;
        try{
            url = new URL(urlStr);
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

        }catch (Exception e){
            e.printStackTrace();
            Resources resources = context.getResources();
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher);
        }finally {
            connection.disconnect();
        }
        return bitmap;
    }

    /**
     * 保存到本地
     * @param context
     * @param bmp
     */
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "/DCIM/camera");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpeg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }
    private class ViewHolder {
        TextView textView;
        HorizontalListView horizontalListView;
        LinearLayout ll_save_img,ll_copy_text;

        public ViewHolder(View itemview) {
            textView = (TextView) itemview.findViewById(R.id.tv_content);
            horizontalListView = (HorizontalListView) itemview.findViewById(R.id.listView);
            ll_save_img = (LinearLayout) itemview.findViewById(R.id.ll_save_img);
            ll_copy_text = (LinearLayout) itemview.findViewById(R.id.ll_copy_text);
        }
    }
}
