package com.yskj.welcomeorchard.ui.advertising;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.utils.JSONFormat;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建日期 2017/3/15on 11:27.
 * 描述：广告数据交互界面
 * 作者：姜贺YSKJ-JH
 */

public class VersionWebActivity extends BaseActivity implements View.OnClickListener ,IWebPageView{

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.web_view)
    BridgeWebView webView;
    @Bind(R.id.root)
    LinearLayout root;
    private String url;
    private MyWebChromeClient mWebChromeClient;
    private String cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_web);
        ButterKnife.bind(this);
        initView();
        initData();
    }
    private void initView() {
        url = getIntent().getStringExtra("url");
        cid = getIntent().getStringExtra("cid");
        imgBack.setOnClickListener(this);
        initWebViewSettings();
    }

    private void initWebViewSettings() {
            WebSettings ws = webView.getSettings();
            // 网页内容的宽度是否可大于WebView控件的宽度
            ws.setLoadWithOverviewMode(false);
            // 保存表单数据
            ws.setSaveFormData(true);
            // 是否应该支持使用其屏幕缩放控件和手势缩放
            ws.setSupportZoom(true);
            ws.setBuiltInZoomControls(true);
            ws.setDisplayZoomControls(false);
            // 启动应用缓存
            ws.setAppCacheEnabled(true);
            // 设置缓存模式
            ws.setCacheMode(WebSettings.LOAD_DEFAULT);
            // setDefaultZoom  api19被弃用
            // 设置此属性，可任意比例缩放。
            ws.setUseWideViewPort(true);
            // 缩放比例 1
            webView.setInitialScale(1);
            // 告诉WebView启用JavaScript执行。默认的是false。
            ws.setJavaScriptEnabled(true);
            //  页面加载好以后，再放开图片
            ws.setBlockNetworkImage(false);
            // 使用localStorage则必须打开
            ws.setDomStorageEnabled(true);
            // 排版适应屏幕
            ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            // WebView是否支持多个窗口。
            ws.setSupportMultipleWindows(true);

            // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            mWebChromeClient = new MyWebChromeClient(VersionWebActivity.this);
            webView.setWebChromeClient(mWebChromeClient);
            // 与js交互
            webView.addJavascriptInterface(new ImageClickInterface(this), "injectedObject");
    }

    private void initData() {
        webView.requestFocus();
        webView.setWebViewClient(new BridgeWebViewClient(webView){
            @Override
            public void onPageFinished(WebView view, String url) {
                VersionWebActivity.this.addImageClickListener();
                super.onPageFinished(view, url);
            }
        });

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
        initJs();
        webView.loadUrl(url);
    }


    private void initJs() {
        webView.registerHandler("ad_red_return", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Logger.json(data);
                Map<String, Object> map = JSONFormat.jsonToMap(data);
                String adRedId = (String) map.get("ad_red_id");//商品id
                String type = (String) map.get("type");//规格名称，英文下划线(可能为空) eg： 尺寸_内存
                Intent intent = null;
                switch (type) {
                    case "save_draft":
                        finish();
                        intent = new Intent(context, AdvertisingDraftActivity.class);
                        break;
                    case "save_release":
                        intent = new Intent(context, SendAdvertisingActivity.class);
                        intent.putExtra("adRedId", adRedId);
                        break;
                    case "add_page":
                        String sort = (String) map.get("sort");
                        finish();
                        intent = new Intent(context,ChooseVersionItemActivity.class);
                        intent.putExtra("type","template");
                        intent.putExtra("rid",adRedId);
                        intent.putExtra("sort",sort);
                        intent.putExtra("cid",cid);
                        break;
                }
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }


    @Override
    public void addImageClickListener() {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        // 如要点击一张图片在弹出的页面查看所有的图片集合,则获取的值应该是个图片数组
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                //  "objs[i].onclick=function(){alert(this.getAttribute(\"has_link\"));}" +
                "objs[i].onclick=function(){window.injectedObject.imageClick(this.getAttribute(\"src\"),this.getAttribute(\"has_link\"));}" +
                "}" +
                "})()");

        // 遍历所有的a节点,将节点里的属性传递过去(属性自定义,用于页面跳转)
        webView.loadUrl("javascript:(function(){" +
                "var objs =document.getElementsByTagName(\"a\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                "objs[i].onclick=function(){" +
                "window.injectedObject.textClick(this.getAttribute(\"type\"),this.getAttribute(\"item_pk\"));}" +
                "}" +
                "})()");
    }

    /**
     * 上传图片之后的回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == MyWebChromeClient.FILECHOOSER_RESULTCODE) {
            mWebChromeClient.mUploadMessage(intent, resultCode);
        } else if (requestCode == MyWebChromeClient.FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            mWebChromeClient.mUploadMessageForAndroid5(intent, resultCode);
        }
    }
}
