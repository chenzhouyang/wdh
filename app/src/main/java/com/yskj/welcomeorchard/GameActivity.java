package com.yskj.welcomeorchard;

import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.utils.LoadingCaches;

import org.cocos2dx.lib.Cocos2dxActivity;

/**
 * 创建日期 2017/10/10on 12:04.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class GameActivity extends Cocos2dxActivity {
    private static LoadingCaches aCache = LoadingCaches.getInstance();
    private static UserInfoEntity userInfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static String getToken() {
        return aCache.get("php_token");
    }
    public static String getRefreshToken () {
        return aCache.get("refresh_token");
    }
    public static boolean getOnLine() {
        return Ips.ONLINE;
    }

    public  static  String getUserImage() {
        if (TextUtils.isEmpty(aCache.get("userinfo"))){
            return "";
        }
        userInfoEntity = new Gson().fromJson(aCache.get("userinfo"), new TypeToken<UserInfoEntity>() {}.getType());
        return userInfoEntity.data.userVo.avatar==null?"":userInfoEntity.data.userVo.avatar;
    }
}
