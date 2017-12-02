package com.yskj.welcomeorchard.ui.buyersShow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.FileEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.widget.NoScrollGridView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * Created by YSKJ-JH on 2017/1/22.
 */

public class BuyersShowActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.title)
    LinearLayout title;
    @Bind(R.id.edittext)
    EditText edittext;
    @Bind(R.id.linearlayout)
    LinearLayout linearlayout;
    @Bind(R.id.gridView1)
    NoScrollGridView gridView1;
    public static Bitmap bimap;
    private GridAdapter adapter;
    private PopupWindow popupWindow;
    private String ImageName;
    private ArrayList<String> commitImg = new ArrayList<>();
    private String goodsId;
    private LoadingCaches aCache = LoadingCaches.getInstance();
    private UserInfoEntity userInfoEntity;
    private String token;
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.mipmap.xiangji);
        PublicWay.activityList.add(this);
        setContentView(R.layout.activity_buyers_show);
        ButterKnife.bind(this);
        initView();
        initGridView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_right:
               if(!aCache.get(Config.TOKEN).equals("null")){
                   sureShare();
               }else {
                   startActivity(new Intent(context, LoginActivity.class));
               }

                break;
        }
    }

    private void sureShare() {
        if (edittext.getText()==null||edittext.getText().equals("")){
            showToast("请输入内容");
            return;
        }
        if (Bimp.tempSelectBitmap==null||Bimp.tempSelectBitmap.size()==0){
            showToast("请选择图片");
            return;
        }
        sendSimpleImg();
    }

    //便利上传图片
    private void sendSimpleImg() {
        for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
            ImageItem imageItem = Bimp.tempSelectBitmap.get(i);
            File file = new File(imageItem.getImagePath());
            OkHttpUtils.post().url(Urls.BUYSHOWPOSTSIMPLEIMG).addFile("wdh", imageItem.getImagePath(), file).build().execute(new fileCallBack());
        }
    }

    /**
     * 上传单张图片到php
     */
    private class fileCallBack extends Callback<FileEntity>{

        @Override
        public FileEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.json(s);
            FileEntity fileEntity = new Gson().fromJson(s,new TypeToken<FileEntity>(){}.getType());
            return fileEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
        }

        @Override
        public void onResponse(FileEntity response, int id) {
            if (response.errorCode.equals("000")){
                commitImg.addAll(response.img);
            }else {
                showToast(response.errorMsg);
            }
            //如果图片上传完后，上传内容及图片到php
            if (commitImg.size()== Bimp.tempSelectBitmap.size()){
                sendToPhp();
            }
        }
    }

    /**
     * 传内容及图片到php
     */
    private void sendToPhp() {
        StringBuilder commitIngBuilder = new StringBuilder();
        commitIngBuilder.append("[");
        for (int i = 0; i < commitImg.size(); i++) {
            if (i != commitImg.size() - 1) {
                commitIngBuilder.append("\"" + commitImg.get(i) + "\",");
            } else {
                commitIngBuilder.append("\"" + commitImg.get(i) + "\"");
            }
        }
        commitIngBuilder.append("]");
        StringBuilder strJson = new StringBuilder();
        strJson.append("{\"goods_id\":\"" + goodsId + "\"," + "\"comment_img\":" + commitIngBuilder + ","
               + "\"token\":\"" + token + "\"," + "\"content\":\"" + buyersshowencipher(edittext.getText().toString()) + "\"}");
        OkHttpUtils.postString().url(Urls.BUYSHOWPOSTALL).content(strJson.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8")).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showToast("请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                Logger.json(response);
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                String code = map.get("error_code").toString();
                String error_msg = map.get("error_msg").toString();
                if (code.equals("000")) {
                    showToast("上传成功");
                    finish();
                } else {
                    showToast(error_msg);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        Bimp.tempSelectBitmap.clear();
    }

    private void initView() {
        txtTitle.setText("文案编辑");
        imgBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        goodsId = getIntent().getStringExtra("goodsId");

        userInfoEntity = new Gson().fromJson(aCache.get("userinfo"), new TypeToken<UserInfoEntity>() {
        }.getType());
        userId = userInfoEntity.data.userVo.id;
        token = aCache.get("php_token");
    }

    private void initGridView() {
        adapter = new GridAdapter(context);
        gridView1.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView1.setAdapter(adapter);
        adapter.update();
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Bimp.tempSelectBitmap.size() != position) {
                    dismissInputMethod();
                    return;
                } else {
                    dismissInputMethod();
                    getPhotoPopupWindow(R.layout.popupwindow_amenduserphoto, -1, ViewGroup.LayoutParams.WRAP_CONTENT, R.style.anim_popup_dir);
                    // 这里是位置显示方式,在屏幕的底部
                    popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                    backgroundAlpha(0.2f);
                }
            }
        });
    }
    private String buyersshowencipher(String str) {
        String newStr = null;
        byte[] temp = new byte[0];//这里写原编码方式
        try {
            temp = str.getBytes("utf-8");
            byte[] newtemp = new String(temp, "utf-8").getBytes("gbk");//这里写转换后的编码方式
            newStr = new String(newtemp, "gbk");//这里写转换后的编码方式
            return  newStr;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return newStr;
    }
    /***
     * 获取PopupWindow实例
     */
    private void getPhotoPopupWindow(int resource, int width, int height, int animationStyle) {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPhotoPopuptWindow(resource, width, height, animationStyle);
        }
    }

    /**
     * 头像PopupWindow
     */
    protected void initPhotoPopuptWindow(int resource, int width, int height, int animationStyle) {
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(resource, null, false);
        popupWindow = new PopupWindow(popupWindow_view, width, height, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(animationStyle);
        backgroundAlpha(0.2f);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    backgroundAlpha(1f);
                    popupWindow = null;
                }
                return false;
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 拍照上传
     *
     * @param view
     */
    public void camera(View view) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");

            ImageName = System.currentTimeMillis() + ".jpeg";
            getImageByCamera.putExtra(MediaStore.ACTION_IMAGE_CAPTURE, Uri.fromFile(new File(
                    Environment.getExternalStorageDirectory()
                    , ImageName)));
            startActivityForResult(getImageByCamera, 1);
        } else {
            Toast.makeText(context, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }

    /**
     * 跳转至相册选择
     *
     * @param view
     */
    public void photoalbum(View view) {
       Intent intent = new Intent(context, AlbumActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ResultOk(requestCode, data);
        }
        if (resultCode == RESULT_CANCELED) {
            finish();
        }
    }

    private void ResultOk(int requestCode, Intent data) {
        if (requestCode == 1) {
            if (data == null) {
                File picture = new File(Environment.getExternalStorageDirectory() + File.separator + ImageName);
                Uri uri = Uri.fromFile(picture);
                return;
            } else {
                Bundle extras = data.getExtras();
                if (extras == null) {
                    return;
                }
                if (extras != null) {
                    if (Bimp.tempSelectBitmap.size() < 6) {

                        Bitmap bm = extras.getParcelable("data");
                        Uri uri = saveBitmap(bm);

                        ImageItem takePhoto = new ImageItem();
                        takePhoto.setBitmap(bm);
                        takePhoto.setImagePath(Environment.getExternalStorageDirectory()+ "/com.yskj.wdh/"+ImageName);
                        Bimp.tempSelectBitmap.add(takePhoto);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    /**
     * 保存图片
     *
     * @param bm
     * @return
     */
    private Uri saveBitmap(Bitmap bm) {
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.yskj.wdh/");
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }
        File img = new File(tmpDir,ImageName);
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onRestart() {
        adapter.notifyDataSetChanged();
        super.onRestart();
    }

    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
//            loading();
        }

        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 6) {
                return 6;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_buyers_show_img,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
                holder.img_del = (ImageView) convertView.findViewById(R.id.img_del);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.img_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bimp.tempSelectBitmap.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
            if (position == Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.xiangji));
                holder.img_del.setVisibility(View.GONE);
                if (position == 6) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.img_del.setVisibility(View.VISIBLE);
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }
            return convertView;
        }

        public class ViewHolder {
            public ImageView image, img_del;
        }

    }
}
