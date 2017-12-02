package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/10/14 0014.
 */
public class LoginEntity {

    /**
     * access_token : c5d9b59f-02b2-4c7c-808b-468d50c1f84b
     * token_type : bearer
     * refresh_token : 7f1d4672-cf35-4ab1-9356-d164a78d8277
     * expires_in : 7137
     * scope : api
     */

    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("token_type")
    public String tokenType;
    @SerializedName("refresh_token")
    public String refreshToken;
    @SerializedName("expires_in")
    public int expiresIn;
    @SerializedName("scope")
    public String scope;
}
