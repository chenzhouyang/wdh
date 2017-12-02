package com.yskj.welcomeorchard.ui.web;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.ApproveDialog;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.ui.store.ShoppingCartActivity;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.widget.SimplexToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;


/**
 * 显示网页的Activity
 */
public class WebViewActivity extends BaseActivity implements View.OnClickListener{

    private ProgressBar progressBar;
    private BridgeWebView webView;
    private ImageView imgBack,imageRight;
    private TextView txt_title;
    private ViewGroup mRoot;
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    //    private ArrayList<String> titles = new ArrayList<>();
    private String gid, paramname, num, optype, param,gtag;
    private int id;
    private UserInfoEntity userInfoEntity;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String url,goodsName,imgUrl;
    private String umurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
        initData();
    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                SimplexToast.show(context, platform + " 收藏成功啦");
            } else {
                SimplexToast.show(context, platform + " 分享成功啦");
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            SimplexToast.show(context, platform + " 分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            SimplexToast.show(context, platform + " 分享取消了");
        }
    };


    private void initView() {
        url = getIntent().getStringExtra("url");
        goodsName = getIntent().getStringExtra("goodsName");
        imgUrl = getIntent().getStringExtra("imgUrl");

        mRoot = (ViewGroup) findViewById(R.id.root);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        webView = (BridgeWebView) findViewById(R.id.web_view);
        imgBack = (ImageView) findViewById(R.id.img_back);
        txt_title = (TextView) findViewById(R.id.txt_title);
        imageRight = (ImageView) findViewById(R.id.image_right);
        imgBack.setOnClickListener(this);
        imageRight.setImageResource(R.mipmap.share);
        imageRight.setVisibility(View.VISIBLE);
        imageRight.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.image_right:
                if (!caches.get("access_token").equals("null")) {
                    umurl = Urls.UMSTR + url.replace(Urls.GOODSDETAIL,"") + "&spreader=" + caches.get("spreadCode");
                    new ShareAction((Activity) context).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                            .withTitle("唯多惠精品")
                            .withText(goodsName+"\n"+context.getResources().getString(R.string.share_content))
                            .withMedia(new UMImage(context, Ips.PHPURL + "/" + imgUrl))
                            .withTargetUrl(umurl)
                            .setCallback(umShareListener)
                            .open();
                } else {
                    new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                            .setMessage("您还没有登录，请先登录。")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    context.startActivity(new Intent(context, LoginActivity.class));
                                }
                            }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                            dialog.dismiss();
                        }
                    }).show();//在按键响应事件中显示此对话框
                }

                break;
        }
    }
    private void initData() {
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.requestFocus();
        webView.setWebViewClient(new BridgeWebViewClient(webView));

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                txt_title.setText(title);
            }

            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE)
                        progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(progress);
                }
                super.onProgressChanged(view, progress);
            }
        });
        initJs();
    }
    private void initJs() {
        //点击跳转到商品详情页面。
        //  JS调JAVA   重点： Java端需要注册事件监听，即webView.registerHandler()。PHP需要做的工作：重写接口回调，
        webView.registerHandler("submitFrom", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Logger.json(data);
                Map<String, Object> map = JSONFormat.jsonToMap(data);
                gid = (String) map.get("gid");//商品id
                paramname = (String) map.get("paramname");//规格名称，英文下划线(可能为空) eg： 尺寸_内存
                num = map.get("num").toString();//商品数量
                optype = map.get("optype").toString();//操作类型 1、立即购买 2、加入购物车
                param = (String) map.get("param");//规格属性，英文下划线(可能为空) eg: 42_29
                gtag = (String) map.get("gtag"); //判断是否是过境商品 有1就是

                //1、立即购买 跳转到本地订单页面   2、加入购物车 请求接口
                if(caches.get("access_token").equals("null")){
                    startActivity(new Intent(context, LoginActivity.class));
                }else {
                    userInfoEntity = new Gson().fromJson(caches.get("userinfo"),new TypeToken<UserInfoEntity>(){}.getType());
                    id = userInfoEntity.data.userVo.id;
                    //过境商品为true
                    if (isTransit(gtag)){
                        UserInfoEntity.DataBean.RealnameVoBean realname = userInfoEntity.data.realnameVo;
                        if (realname == null || realname.name == null) {
                            ApproveDialog dialog = new ApproveDialog(context, "进口物品，保税清关，需要进行实名认证信息！请实名认证后再购买！", "3");
                            dialog.show();
                        }else {
                            addToShoppingCart(optype);
                        }
                    }else {
                        addToShoppingCart(optype);
                    }
                }


            }
        });
        webView.registerHandler("cartlist", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if(caches.get("access_token").equals("null")){
                    startActivity(new Intent(context, LoginActivity.class));
                }else {
                    Intent intent = new Intent(context, ShoppingCartActivity.class);
                    intent.putExtra("type","1");
                    startActivity(intent);
                }

            }
        });

    }
    //判断是否是过境商品
    private boolean isTransit(String gtag) {
        if (gtag.equals("")){
            return false;
        }else {
            String[] tag =  gtag.split("|");
            for (int i = 0; i < tag.length; i++) {
                if (tag[i].equals("1")){
                    return true;
                }
            }
            return false;
        }
    }

    //添加到购物车
    private void addToShoppingCart(final String optype) {
        String[] paramnameList;
        String[] paramList;

        if (paramname.contains("_")) {
            paramnameList = paramname.split("_");
            paramList = param.split("_");
        } else {
            paramnameList = paramname.split(" ");
            paramList = param.split(" ");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (int i = 0; i <paramnameList.length;i++) {
            if (i!=paramList.length-1){
                stringBuilder.append("\""+paramnameList[i]+"\":\""+paramList[i]+"\",");
            }else {
                stringBuilder.append("\""+paramnameList[i]+"\":\""+paramList[i]+"\"");
            }
        }
        stringBuilder.append("}");

        StringBuilder strJson = new StringBuilder();
        if (paramname.equals("")){
            strJson.append("{\"goods_id\":\""+gid+"\","+"\"goods_num\":\""+num+"\","+"\"uid\":"+id+"}");
        }else {
            strJson.append("{\"goods_id\":\""+gid+"\","+"\"goods_num\":\""+num+"\","+"\"goods_spec\":"+stringBuilder+","+"\"uid\":"+id+"}");
        }
        //加入删除购物车   1加入购物车 2 删除
        OkHttpUtils.postString().url(Urls.ADDTOSHOPPINGCART+1).content(strJson.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8")).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showToast("请检查网络");
            }
            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                String code = map.get("error_code").toString();
                String error_msg = map.get("error_msg").toString();
                if (code.equals("000")){
                    switch (optype){
                        case "1":
                            Intent intent = new Intent(context, ShoppingCartActivity.class);
                            intent.putExtra("type","1");
                            startActivity(intent);
                            break;
                        case "2":
                            showToast("添加成功");
                            break;
                    }
                }else {
                    showToast(error_msg);
                }
            }
        });
    }

    protected void onPause() {
        super.onPause();
        webView.reload();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.clearView();
            mRoot.removeView(webView);
        }
        webView = null;
        super.onDestroy();
    }
}
