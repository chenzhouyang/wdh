package com.yskj.welcomeorchard.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.yskj.welcomeorchard.dialog.MyLoading;

/**
 * Created by jianghe on 2016/11/18 0018.
 */
public class LocalWebViewClient extends BridgeWebViewClient {
    private MyLoading myloading;
    private Context context;

    public LocalWebViewClient(BridgeWebView webView) {
        super(webView);
    }
    public LocalWebViewClient(BridgeWebView webView, Context context){
        super(webView);
        this.context = context;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        startMyDialog();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        stopMyDialog();
    }

    /**
    * dialog 启动
    */
    public void startMyDialog() {
        if (myloading == null) {
            myloading = MyLoading.createLoadingDialog(context);
        }
        if (!myloading.isShowing()) {
            myloading.show();
        }
    }

    /**
     * dialog 销毁
     */
    public void stopMyDialog() {
        if (myloading != null) {
            if (myloading.isShowing()){
                myloading.dismiss();
            }
            myloading = null;
        }
    }
}
