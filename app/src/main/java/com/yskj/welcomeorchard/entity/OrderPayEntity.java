package com.yskj.welcomeorchard.entity;


/**
 * Created by YSKJ-JH on 2017/1/21.
 */

public class OrderPayEntity  {
    public int imgId;
    public String payType;
    public boolean isChacked;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public boolean isChacked() {
        return isChacked;
    }

    public void setChacked(boolean chacked) {
        isChacked = chacked;
    }
}
