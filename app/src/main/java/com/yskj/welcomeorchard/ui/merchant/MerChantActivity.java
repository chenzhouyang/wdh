package com.yskj.welcomeorchard.ui.merchant;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unionpay.tsmservice.request.RequestParams;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.MerChantAdapter;
import com.yskj.welcomeorchard.adapter.SubListViewAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.BusinessInfoBean;
import com.yskj.welcomeorchard.entity.City;
import com.yskj.welcomeorchard.entity.InfoEntity;
import com.yskj.welcomeorchard.entity.LocalServerMainListBean;
import com.yskj.welcomeorchard.entity.LocalServerNearPopBean;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.utils.CitySelect1Activity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.MethodUtils;
import com.yskj.welcomeorchard.utils.MobileEncryption;
import com.yskj.welcomeorchard.utils.PhotoUtil;
import com.yskj.welcomeorchard.utils.ScreenUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liuchaoya(整理的不错!!!赞一个!!!) on 2016/10/17.
 * For yskj
 * Project Name : LSK
 */

public class MerChantActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_shoprenzheng_back;
    /**
     * 经营类型，经营模式
     */
    private TextView operate, et_detail_address;
    private EditText et_shopname, et_phonenum, et_profile, et_detail_address_content;
    private ImageView iv_idcardzheng, iv_idcardfan, iv_license, iv_shop_cover;
    private LinearLayout ll_commit, select_address;
    /*用来标识请求gallery的activity*/
    private static final int FRONT_PICTURE = 11;
    private static final int BEHIND_PICTURE = 12;
    private static final int LICENSE_PICTURE = 13;
    private static final int COVER_PICTURE = 14;
    private static final int SELECT_CITY = 15;
    private int currentOperate = -1;
    private Bitmap bitMap;

    String phoneNum, shopName;
    SharedPreferences sps;
    ProgressDialog dialog;
    private RequestParams params;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token;
    private City city;
    private String hometownId;
    /**
     * 从相册选择照片
     */
    public static final int ALBUM = 1;
    /**
     * 从相机拍照获取照片
     */
    public static final int CAMERA = 2;
    /**
     * 照片来源
     */
    public int pictureFromWhere = 0;
    /**
     * 当前要上传的图片的file路径
     */
    private File mCurrentPhotoFile;
    private static final String FRONT_PICTURE_TIP = "正面照片上传成功，请上传下一张";
    private static final String BEHIND_PICTURE_TIP = "反面照片上传成功，请上传下一张";
    private static final String LICENSE_PICTURE_TIP = "营业执照上传成功，请提交申请";
    private static final String COVER_PICTURE_TIP = "封面照上传成功，请上传下一张";
    /**
     * 从相机来的照片路径
     */
    private String path;
    /**
     * 当前上传图片完成后给出的提醒
     */
    private String currentUploadPictureTip;
    /**
     * 本地存储照片名
     */
    private static final String FRONT_NAME = "front";
    private static final String BEHIND_NAME = "behind";
    private static final String LICENSE_NAME = "license";
    private static final String COVER_NAME = "cover";
    /**
     * 当前存储的照片名
     */
    private String currentPictureName;
    /**
     * 当前ImageView
     */
    private ImageView currentImageView;

    /**
     * cateId
     */
    private String categoryId;
    /**
     * 弹出的分类popupWindow布局
     */
    private LinearLayout popupLayout;
    /**
     * 二级菜单的根目录
     */
    private ListView rootListView;
    /**
     * 根目录的节点
     */
    private ArrayList<LocalServerNearPopBean.RetData> roots = new ArrayList<>();
    /**
     * 子目录的布局
     */
    private FrameLayout subLayout;
    /**
     * 子目录的布局
     */
    private ListView subListView;
    /**
     * 根目录被选中的节点
     */
    private int selectedPosition;
    /**
     * 子目录节点
     */
    private ArrayList<ArrayList<LocalServerNearPopBean.Children>> sub_items = new ArrayList<>();
    /**
     * 分类popupwindow
     */
    private PopupWindow mPopupWindow;
    private ArrayList<LocalServerNearPopBean.Children> arrayList = new ArrayList<LocalServerNearPopBean.Children>();
    private LocalServerNearPopBean localServerNearPopBean;
    private ArrayList<LocalServerMainListBean.LocalLifesBean> localLifes = new ArrayList<>();
    Gson gson = new Gson();
    String pictureId;
    private PopupWindow popupWindow;
    private UserInfoEntity userInfoEntity;
    private ImageView img_back;
    private TextView txt_title;
    private String Province,citys,District,Districtid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);
        sps = getSharedPreferences("UserInfor", 0);
        if (!caches.get("userinfo").equals("null")) {
            userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
            }.getType());
        }
        Intent intent = getIntent();
        int where = intent.getIntExtra("where", -1);
        if (where == 2) {
            requestBusinessInfo();
        }
        initView();
        initData();
    }


    private void initData() {
        //弹框数据请求
        OkHttpUtils.get().url(Urls.LOCALSERVERPOPWIN)
                .addParams("type","1").build().execute(new LocalServerPopwinCallBack());
    }

    private class LocalServerPopwinCallBack extends Callback<LocalServerNearPopBean> {
        @Override
        public void onBefore(Request request, int id) {
            startMyDialog();
            super.onBefore(request, id);
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            stopMyDialog();
        }

        @Override
        public LocalServerNearPopBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            localServerNearPopBean = new Gson().fromJson(s, new TypeToken<LocalServerNearPopBean>() {
            }.getType());
            return localServerNearPopBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(LocalServerNearPopBean response, int id) {
            for (LocalServerNearPopBean.RetData retData : response.ret_data) {
                roots.add(retData);
            }
        }
    }

    //分类
    private void showPopBtn(int screenWidth, int screenHeight) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(MerChantActivity.this);
        popupLayout = (LinearLayout) inflater.inflate(R.layout.pop_layout_local_server, null, false);
        rootListView = (ListView) popupLayout.findViewById(R.id.root_listview);
        final MerChantAdapter adapter = new MerChantAdapter(MerChantActivity.this);
        adapter.setItems(roots);
        rootListView.setAdapter(adapter);

        /**
         * 子popupWindow
         */
        subLayout = (FrameLayout) popupLayout.findViewById(R.id.sub_popupwindow);

        /**
         * 初始化subListview
         */
        subListView = (ListView) popupLayout.findViewById(R.id.sub_listview);

        /**
         * 弹出popupwindow时，二级菜单默认隐藏，当点击某项时，二级菜单再弹出
         */
        subLayout.setVisibility(View.INVISIBLE);

        /**
         * 初始化mPopupWindow
         */
        mPopupWindow = new PopupWindow(popupLayout, screenWidth,
                FrameLayout.LayoutParams.WRAP_CONTENT, true);

        /**
         * 有了mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
         * 这句可以使点击popupwindow以外的区域时popupwindow自动消失 但这句必须放在showAsDropDown之前
         */
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

        /**
         * popupwindow的位置，第一个参数表示位于哪个控件之下 第二个参数表示向左右方向的偏移量，正数表示向左偏移，负数表示向右偏移
         * 第三个参数表示向上下方向的偏移量，正数表示向下偏移，负数表示向上偏移
         *
         */
        mPopupWindow.showAsDropDown(operate, -5, 20);// 在控件下方显示popwindow

        mPopupWindow.update();

        rootListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        /**
                         * 选中root某项时改变该ListView item的背景色
                         */
                        selectedPosition = position;
                        if (localServerNearPopBean.ret_data.get(selectedPosition).childrens != null && localServerNearPopBean.ret_data.get(selectedPosition).childrens.size() != 0) {
                            for (int i = 0; i < localServerNearPopBean.ret_data.size(); i++) {
                                arrayList.clear();
                                for (int n = 0; n < localServerNearPopBean.ret_data.get(selectedPosition).childrens.size(); n++) {
                                    arrayList.add(localServerNearPopBean.ret_data.get(selectedPosition).childrens.get(n));
                                }
                                sub_items.add(arrayList);
                            }
                            SubListViewAdapter subAdapter = new SubListViewAdapter(
                                    MerChantActivity.this, sub_items, position);
                            subListView.setAdapter(subAdapter);
                            subAdapter.notifyDataSetChanged();
                            /**
                             * 选中某个根节点时，使显示相应的子目录可见
                             */
                            subLayout.setVisibility(View.VISIBLE);
                            subListView
                                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        @Override
                                        public void onItemClick(
                                                AdapterView<?> parent, View view,
                                                int position, long id) {
                                            // TODO Auto-generated method stub
                                            mPopupWindow.dismiss();
                                            categoryId = sub_items.get(selectedPosition).get(position).cateId;
                                            shopName = sub_items.get(selectedPosition).get(position).name;
                                            operate.setText(shopName);
                                            localLifes.clear();
                                        }
                                    });
                        } else {

                            mPopupWindow.dismiss();
                            adapter.setSelectedPosition(position);
                            adapter.notifyDataSetInvalidated();

                            categoryId = roots.get(position).cateId;
                            shopName = roots.get(position).name;
                            operate.setText(shopName);
                        }


                    }
                });
    }

    private void initView() {
        et_shopname = (EditText) findViewById(R.id.et_shopname);
        et_phonenum = (EditText) findViewById(R.id.et_phonenum);
        et_phonenum.setFocusable(false);
        SharedPreferences share2    = getSharedPreferences("mobile", 0);
        String mobile  = share2.getString("mobile", "null");
        et_phonenum.setText(mobile);
        /**选择城市*/
        select_address = (LinearLayout) findViewById(R.id.select_address);
        /**显示选择结果*/
        et_detail_address = (TextView) findViewById(R.id.et_detail_address);
        /**详细地址*/
        et_detail_address_content = (EditText) findViewById(R.id.et_detail_address_content);
        /**商家简介*/
        et_profile = (EditText) findViewById(R.id.et_profile);
        /**商家封面*/
        iv_shop_cover = (ImageView) findViewById(R.id.iv_shop_cover);
        /**经营类型*/
        operate = (TextView) findViewById(R.id.operate);
        //iv_idcardzheng = (ImageView) findViewById(R.id.iv_idcardzheng);
        //iv_idcardfan = (ImageView) findViewById(R.id.iv_idcardfan);
        iv_license = (ImageView) findViewById(R.id.iv_license);
        ll_commit = (LinearLayout) findViewById(R.id.ll_commit);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("申请入驻");
        et_detail_address.setOnClickListener(this);
        operate.setOnClickListener(this);
        //提交所有信息
        ll_commit.setOnClickListener(this);
        //身份证正面
        // iv_idcardzheng.setOnClickListener(this);
        //身份证反面
        // iv_idcardfan.setOnClickListener(this);
        //营业执照
        iv_license.setOnClickListener(this);
        //商家封面
        iv_shop_cover.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_detail_address:
                Intent intent = new Intent(MerChantActivity.this, CitySelect1Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                currentOperate = SELECT_CITY;
                startActivityForResult(intent, currentOperate);
                break;
           /* case R.id.iv_idcardzheng:
                currentOperate = FRONT_PICTURE;
                currentUploadPictureTip = FRONT_PICTURE_TIP;
                selectPictureSource();
                break;*/
           /* case R.id.iv_idcardfan:
                currentOperate = BEHIND_PICTURE;
                currentUploadPictureTip = BEHIND_PICTURE_TIP;
                selectPictureSource();
                break;*/
            case R.id.iv_license:
                currentOperate = LICENSE_PICTURE;
                currentUploadPictureTip = LICENSE_PICTURE_TIP;
                dialogShow(v);
                break;
            case R.id.iv_shop_cover:
                currentOperate = COVER_PICTURE;
                currentUploadPictureTip = COVER_PICTURE_TIP;
                dialogShow(v);
                break;
            case R.id.ll_commit:
                submitBusinessInfo();
                break;
            case R.id.operate:
                showPopBtn(ScreenUtils.getScreenWidth(MerChantActivity.this),
                        ScreenUtils.getScreenHeight(MerChantActivity.this));
                break;
            case R.id.img_back:
                finish();
                break;

        }
    }

    /**
     * 从相册选择照片
     */
    private void doSelectImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, currentOperate);
    }


    /**
     * 调用系统相机拍照
     */
    protected void doTakePhoto() {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/Photo");
            if (!file.exists()) {
                file.mkdirs();
            }
            mCurrentPhotoFile = new File(file, PhotoUtil.getRandomFileName() + ".jpg");
            path = Environment.getExternalStorageDirectory() + "/DCIM/Photo/" + PhotoUtil.getRandomFileName();
            final Intent intent = getTakePickIntent(mCurrentPhotoFile);
            startActivityForResult(intent, currentOperate);
        } catch (ActivityNotFoundException d) {
            Toast.makeText(this, "手机中无可用的图片", Toast.LENGTH_LONG).show();
        }
    }

    public static Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (currentOperate) {
            case FRONT_PICTURE:
                currentImageView = iv_idcardzheng;
                if (pictureFromWhere == ALBUM) {
                    mCurrentPhotoFile = getPicturePathFromAlbum(data);
                    if(bitMap!=null){
                        if (bitMap.getByteCount() > (1024 * 1024 * 3)) {
                            MethodUtils.showToast(MerChantActivity.this, "请上传小于3M的图片");
                            break;
                        }
                    }

                } else {
                    getPicturePathFromCamera();
                }
                currentPictureName = FRONT_NAME;
                uploadPicture();
                break;
            case BEHIND_PICTURE:
                currentImageView = iv_idcardfan;
                if (pictureFromWhere == ALBUM) {
                    mCurrentPhotoFile = getPicturePathFromAlbum(data);
                    if(bitMap!=null){
                        if (bitMap.getByteCount() > (1024 * 1024 * 3)) {
                            MethodUtils.showToast(MerChantActivity.this, "请上传小于3M的图片");
                            break;
                        }
                    }

                } else {
                    getPicturePathFromCamera();
                }
                currentPictureName = BEHIND_NAME;
                uploadPicture();
                break;
            case LICENSE_PICTURE:
                currentImageView = iv_license;
                if (pictureFromWhere == ALBUM) {
                    mCurrentPhotoFile = getPicturePathFromAlbum(data);
                    if(bitMap!=null){
                        if (bitMap.getByteCount() > (1024 * 1024 * 3)) {
                            MethodUtils.showToast(MerChantActivity.this, "请上传小于3M的图片");
                            break;
                        }
                    }

                } else {
                    getPicturePathFromCamera();
                }
                currentPictureName = LICENSE_NAME;
                uploadPicture();
                break;
            case COVER_PICTURE:
                currentImageView = iv_shop_cover;
                if (pictureFromWhere == ALBUM) {
                    mCurrentPhotoFile = getPicturePathFromAlbum(data);
                    if(bitMap !=null){
                        if (bitMap.getByteCount() > (1024 * 1024 * 3)) {
                            MethodUtils.showToast(MerChantActivity.this, "请上传小于3M的图片");
                            break;
                        }
                    }
                } else {
                    getPicturePathFromCamera();
                }
                currentPictureName = COVER_NAME;
                uploadPicture();
                break;
            case SELECT_CITY:
                Province = data.getStringExtra("Province");
                citys = data.getStringExtra("City");
                District = data.getStringExtra("Area");
                et_detail_address.setText(Province + citys + District);
                Districtid = data.getStringExtra("AreaId");
                hometownId = Districtid;
                break;
        }
    }

    /**
     * 上传图片方法，所有上传均走此方法，改变之前的一个图片一个方法导致的代码臃肿问题
     */
    private void uploadPicture() {
        dialog = new ProgressDialog(MerChantActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("上传");
        dialog.setMessage("玩命上传中，请稍等...");
        dialog.show();
        if (mCurrentPhotoFile == null) {
            dialog.dismiss();
            MethodUtils.showToast(MerChantActivity.this, "获取照片失败，请重试");
        } else {
            mCurrentPhotoFile = saveBitmap(bitMap);
            OkHttpUtils.post().url(Urls.UPDATRSVTEA)
                    .addParams("userId", userInfoEntity.data.userVo.id + "")
                    .addFile("files", System.currentTimeMillis() + ".jpg", mCurrentPhotoFile).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    dialog.dismiss();
                    isLogin(e);
                }

                @Override
                public void onResponse(String response, int id) {
                    dialog.dismiss();
                    InfoEntity infoEntity = new Gson().fromJson(response, new TypeToken<InfoEntity>() {
                    }.getType());
                    if (infoEntity.code == 0) {
                        showToast("上传成功");
                        pictureId = infoEntity.data.get(0);
                        sp.edit().putString(currentPictureName, pictureId).commit();
                        currentImageView.setImageBitmap(bitMap);
                        bitMap = null;
                    } else {
                        showToast(MessgeUtil.geterr_code(infoEntity.code));
                    }
                }
            });
        }

    }
    /**
     * 选择图片来源
     */
    private void dialogShow(View view) {
        getPhotoPopupWindow(R.layout.popupwindow_amenduserphoto, -1, ViewGroup.LayoutParams.WRAP_CONTENT, R.style.anim_popup_dir);
        // 这里是位置显示方式,在屏幕的底部
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.2f);
    }

    /***
     * 获取图片PopupWindow实例
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
     * 图片PopupWindow
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
        pictureFromWhere = CAMERA;
        doTakePhoto();
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
        pictureFromWhere = ALBUM;
        doSelectImageFromAlbum();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }

    private File getPicturePathFromAlbum(Intent data) {
        Uri uri = data.getData();
        uri = geturi(data);//解决方案
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 2;   // width，hight设为原来的十分一
            try {
                bitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
        } else {
            return null;
        }
    }
    /**
     * 解决小米手机上获取图片路径为null的情况
     * @param intent
     * @return
     */
    public Uri geturi(android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }
    private void getPicturePathFromCamera() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 2;   // width，hight设为原来的十分一
        try {
            bitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(mCurrentPhotoFile)), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩图片（质量压缩）
     * @param bitmap
     */
    public static File  saveBitmap(Bitmap bitmap) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = 100;
//        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
//            baos.reset();//重置baos即清空baos
//            options -= 10;//每次都减少10
//            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
//            long length = baos.toByteArray().length;
//        }
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/lsk/");
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        File img = new File(tmpDir.getAbsolutePath() + "share.png");
        BufferedOutputStream bos= null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(img));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    /**
     * 提交商户信息
     */
    private void submitBusinessInfo() {

        phoneNum = et_phonenum.getText().toString().trim();
        shopName = et_shopname.getText().toString().trim();
        String address = et_detail_address.getText().toString().trim();
        String detailAddress = et_detail_address_content.getText().toString().trim();
        String profile = et_profile.getText().toString().trim();
        if (TextUtils.isEmpty(shopName)) {
            MethodUtils.showToast(MerChantActivity.this, "商家名称不能为空");
        } else if (TextUtils.isEmpty(phoneNum)) {
            MethodUtils.showToast(MerChantActivity.this, "手机号不能为空");
        } else if (TextUtils.isEmpty(address)) {
            MethodUtils.showToast(MerChantActivity.this, "地址未选择");
        } else if (TextUtils.isEmpty(detailAddress)) {
            MethodUtils.showToast(MerChantActivity.this, "未填写详细地址");
        } else if (TextUtils.isEmpty(profile)) {
            MethodUtils.showToast(MerChantActivity.this, "未填写简介");
        } else if (!MobileEncryption.isMobileNO(phoneNum)) {
            MethodUtils.showToast(MerChantActivity.this, "手机号格式不对，请重新输入");
        } else if (categoryId==null||TextUtils.isEmpty(categoryId)) {
            MethodUtils.showToast(MerChantActivity.this, "请选择经营类型");
        }else {
            String zhengmian = sp.getString(FRONT_NAME, "");
            String fanmian = sp.getString(BEHIND_NAME, "");
            String licence = sp.getString(LICENSE_NAME, "");
            String cover = sp.getString(COVER_NAME, "");
            String latitude = sps.getString(Config.SPKEY_LATITUDE,"");
            String longitude = sps.getString(Config.SPKEY_LONGITUDE,"");
            if (licence != null) {
                dialog = new ProgressDialog(MerChantActivity.this);
                dialog.setTitle("商家信息提交");
                dialog.setMessage("玩命上传中，请稍等...");
                dialog.show();
                OkHttpUtils.post().url(Urls.APPLY).addHeader("Authorization", caches.get("access_token"))
                        .addParams("idCardFront", "")
                        .addParams("idCardBack", "")
                        .addParams("licence", licence)
                        .addParams("cover", cover)
                        .addParams("mobile", phoneNum)
                        .addParams("shopName", shopName)
                        .addParams("categoryId", categoryId)
                        .addParams("areaId", hometownId + "")
                        .addParams("detailAddress", detailAddress)
                        .addParams("profile", profile)
                        .addParams("latitude",latitude)
                        .addParams("longitude",longitude)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                        isLogin(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        Map<String, Object> map = JSONFormat.jsonToMap(response);
                        int code = (int) map.get("code");
                        if (code == 0) {
                            getuserinfo();
                        } else {
                            finish();
                            showToast(MessgeUtil.geterr_code(code));
                        }
                    }
                });
            } else {
                MethodUtils.showToast(MerChantActivity.this, "请上传照片");
                dismiss();
            }
        }
    }

    private void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    /**
     * 用于上传过后再进入此界面时拉取之前上传的信息
     */
    private void requestBusinessInfo() {
        OkHttpUtils.get().url(Urls.BUSINESS).addHeader("Authorization", caches.get("access_token"))
                .build().execute(new StringCallback() {
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                stopMyDialog();
            }

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                startMyDialog();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                try {
                    if (code == 0) {
                        BusinessInfoBean bean = new Gson().fromJson(response,new TypeToken<BusinessInfoBean>(){}.getType());
                        sp.edit().putString("license", bean.data.licence);
                        sp.edit().putString("cover", bean.data.cover);
                        hometownId = bean.data.areaId+"";
                        categoryId = bean.data.categoryId + "";
                        et_shopname.setText(bean.data.shopName);
                        et_phonenum.setText(bean.data.mobile+"");
                        et_detail_address.setText(bean.data.areaString);
                        et_detail_address_content.setText(bean.data.detailAddress);
                        et_profile.setText(bean.data.profile);
                        GlideImage.loadImage(getBaseContext(),iv_shop_cover,bean.data.coverUrl,R.mipmap.img_error);
                        GlideImage.loadImage(getBaseContext(),iv_license,bean.data.licenceUrl,R.mipmap.img_error);
                        Toast.makeText(MerChantActivity.this, bean.data.approvalOpinion.toString(), Toast.LENGTH_LONG).show();
                        operate.setText(bean.data.categoryName);
                    } else if (code == 300302) {
                        Toast.makeText(MerChantActivity.this, "商户不存在", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 请求个人信息
     */
    private void getuserinfo() {
        OkHttpUtils.get().url(Urls.USERINFO).addHeader("Authorization", caches.get("access_token"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                caches.put("userinfo", response);
                showToast("信息已提交请勿重复提交，请等待审核");
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
            }
        });
    }
}