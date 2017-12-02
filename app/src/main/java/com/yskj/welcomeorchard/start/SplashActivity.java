package com.yskj.welcomeorchard.start;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AppIntroEntity;
import com.yskj.welcomeorchard.entity.LoginEntity;
import com.yskj.welcomeorchard.home.HomeActivity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.Network;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @{# SplashActivity.java Create on 2013-5-2 下午9:10:01
 *
 *     class desc: 启动画面 (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 *     (2)是，则进入GuideActivity；否，则进入MainActivity (3)3s后执行(2)操作
 *
 *     <p>
 *     Copyright: Copyright(c) 2013
 *     </p>
 * @Version 1.0
 * @Author <a href="mailto:gaolei_xj@163.com">Leo</a>
 *
 *
 */
public class SplashActivity extends BaseActivity {
	private LoadingCaches aCache = LoadingCaches.getInstance();
	public SharedPreferences preferences;
	private static final int GO_SELF_LOGIN = 1000;
	private static final int GO_START = 1001;
	private static final int GO_HOME = 1002;
	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	private String   isFirstIn = null;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				//自动登录
				case GO_SELF_LOGIN:
					goSelfLogin();
					break;
				//跳转到引导页
				case GO_START:
					goToStart();
					break;
				//跳转到主界面
				case GO_HOME:
					startActivity(new Intent(context,HomeActivity.class));
					break;
			}
			super.handleMessage(msg);
		}
	};
	private String string;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		aCache.put("access_token","null");
		sign();
		//getCB();
	}
	private void sign(){
		OkHttpUtils.get().url(Urls.QNYJSON).build().execute(new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {
				getCB();
			}

			@Override
			public void onResponse(String response, int id) {
				Map<String,Object> map = JSONFormat.jsonToMap(response);
				String sign = (String) map.get("sign");
				if(sign.equals("1")){
					getCB();
				}else {
					new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
							.setMessage("唯多惠正在更新中，请稍后再试")//设置显示的内容
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
								public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
								}
							}).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
						public void onClick(DialogInterface dialog, int which) {//响应事件
							dialog.dismiss();
						}
					}).show();//在按键响应事件中显示此对话框
				}
			}
		});
	}
	private void getCB() {
		OkHttpUtils.get().url(Urls.APPINTRO).build().execute(new ConvenientBannerCallBack());
	}
	public class ConvenientBannerCallBack extends Callback<AppIntroEntity> {
		@Override
		public AppIntroEntity parseNetworkResponse(Response response, int id) throws Exception {
			string = response.body().string();
			AppIntroEntity convenientBannerEntities = new Gson().fromJson(string, new TypeToken<AppIntroEntity>() {
			}.getType());
			return convenientBannerEntities;
		}

		@Override
		public void onError(Call call, Exception e, int id) {
			mHandler.sendEmptyMessage(GO_HOME);
		}

		@Override
		public void onResponse(AppIntroEntity response, int id) {

			//如果缓存与请求结果相同 ，跳转到home页 否则跳转到引导页
			boolean isRight = sp.getString("cbimg","0").equals(string);
			if (isRight){
				//登陆时sp = 0； 退出时为1
				SharedPreferences share4 = getSharedPreferences("isFirstIn", 0);
				isFirstIn = share4.getString("isFirstIn", "");
				if (isFirstIn.equals("0")) {
					mHandler.sendEmptyMessage(GO_SELF_LOGIN);
				}else {
					mHandler.sendEmptyMessage(GO_HOME);
				}
			}else {
				mHandler.sendEmptyMessage(GO_START);
			}
			sp.edit().putString("cbimg",string).commit();
		}
		
	}

	//跳转到引导页
	private void goToStart() {
		if(Network.isNetworkAvailable(SplashActivity.this)){
			startActivity(new Intent(context,StartActivity.class));
		}else{
			Toast.makeText(getApplicationContext(),"当前无网络", Toast.LENGTH_SHORT).show();
		}
	}

	private void goSelfLogin() {
		if (Network.isNetworkAvailable(SplashActivity.this)){
			SharedPreferences share2    = getSharedPreferences("mobile", 0);
			String mobile  = share2.getString("mobile", "null");
			SharedPreferences share    = getSharedPreferences("password", 0);
			String password  = share.getString("password", "null");
			startMyDialog();
			OkHttpUtils.post().url(Urls.LOGIN).addHeader("Content-Type", Ips.CONTENT_TYPE)
					.addHeader("Authorization", Ips.AUTHORIZATION)
					.addParams("username", mobile)
					.addParams("password", password)
					.addParams("grant_type", "password").build()
					.execute(new LoginCallBack());
			stopMyDialog();
		}else {
			showToast("请检查您的网络设置");
		}
	}
	private class LoginCallBack extends Callback<LoginEntity> {

		@Override
		public void onBefore(Request request, int id) {
			super.onBefore(request, id);
			stopMyDialog();
		}

		@Override
		public void onAfter(int id) {
			super.onAfter(id);
			stopMyDialog();
		}

		@Override
		public LoginEntity parseNetworkResponse(Response response, int id) throws Exception {
			String s = response.body().string();
			LoginEntity loginEntity = new Gson().fromJson(s, new TypeToken<LoginEntity>() {
			}.getType());
			return loginEntity;
		}

		@Override
		public void onError(Call call, Exception e, int id) {
			if (e.getMessage()==null){
				return;
			}
			if(e.getMessage().contains("401")||e.getMessage().contains("403")||e.getMessage().contains("400")){
				showToast("帐号或密码错误");
				startActivity(new Intent(context, LoginActivity.class));
			}else if(e.getMessage().contains("502")){
				showToast("服务器维护中");
				startActivity(new Intent(context,HomeActivity.class));
			}else {
				showToast("网络链接错误");
			}
			mHandler.sendEmptyMessage(GO_HOME);
		}

		@Override
		public void onResponse(LoginEntity response, int id) {
			aCache.put("access_token", Config.TOKENHEADER+response.accessToken);
			aCache.put("php_token",response.accessToken);
			aCache.put("refresh_token",response.refreshToken);
			aCache.put("token_type",response.tokenType);
			aCache.put("expires_in",response.expiresIn+"");
			aCache.put("scope",response.scope);
			startActivity(new Intent(context, HomeActivity.class));
		}
	}
}
