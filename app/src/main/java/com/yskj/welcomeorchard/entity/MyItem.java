package com.yskj.welcomeorchard.entity;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.ui.mapmanager.ClusterItem;

import java.util.Map;

/**
 * 创建日期 2017/3/16on 16:14.
 * 描述： 每个Marker点，包含Marker点坐标以及图标
 * 作者：姜贺YSKJ-JH
 */

public class MyItem implements ClusterItem {

    private Map<String, Object> map;
    private final LatLng mPosition;
    private String redId;//红包id
    private String redStatus;//红包状态
    private String redRegion;//红包范围
    private String redAddress;//红包地址
    private String redUserIds;//红包用户id
    private String redName;
    private String redImage;
    private String redTitle; //红包标题
    private String redCover;//红包封面
    private String redSex;//性别限制
    private String redMinAge;//最小年龄限制
    private String redMaxAge;//最大年龄限制
    private String redTemplate;//php模板信息

    public String getRedTitle() {
        return redTitle;
    }

    public void setRedTitle(String redTitle) {
        this.redTitle = redTitle;
    }

    public String getRedCover() {
        return redCover;
    }

    public void setRedCover(String redCover) {
        this.redCover = redCover;
    }

    public String getRedSex() {
        return redSex;
    }

    public void setRedSex(String redSex) {
        this.redSex = redSex;
    }

    public String getRedMinAge() {
        return redMinAge;
    }

    public void setRedMinAge(String redMinAge) {
        this.redMinAge = redMinAge;
    }

    public String getRedMaxAge() {
        return redMaxAge;
    }

    public void setRedMaxAge(String redMaxAge) {
        this.redMaxAge = redMaxAge;
    }

    public String getRedTemplate() {
        return redTemplate;
    }

    public void setRedTemplate(String redTemplate) {
        this.redTemplate = redTemplate;
    }

    public String getRedName() {
        return redName;
    }
    public void setRedName(String redName) {
        this.redName = redName;
    }

    public String getRedImage() {
        return redImage;
    }

    public void setRedImage(String redImage) {
        this.redImage = redImage;
    }

    public String getRedUserIds() {
        return redUserIds;
    }

    public void setRedUserIds(String redUserIds) {
        this.redUserIds = redUserIds;
    }

    public String getRedAddress() {
        return redAddress;
    }

    public void setRedAddress(String redAddress) {
        this.redAddress = redAddress;
    }

    public String getRedStatus() {
        return redStatus;
    }

    public void setRedStatus(String redStatus) {
        this.redStatus = redStatus;
    }

    public String getRedRegion() {
        return redRegion;
    }

    public void setRedRegion(String redRegion) {
        this.redRegion = redRegion;
    }

    public String getRedId() {
        return redId;
    }

    public void setRedId(String redId) {
        this.redId = redId;
    }

    public MyItem(LatLng latLng,Map<String, Object> map) {
        mPosition = latLng;
        this.map =  map;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        String type = map.get("ad_red_limit_sex") == null ? "" : map.get("ad_red_limit_sex").toString();
        switch (type){
            case "":
                return BitmapDescriptorFactory.fromResource(R.mipmap.red_position);
            case "0":
                return BitmapDescriptorFactory.fromResource(R.mipmap.red_woman);
            case "1":
                return BitmapDescriptorFactory.fromResource(R.mipmap.red_man);
            default:
                return BitmapDescriptorFactory.fromResource(R.mipmap.red_position);
        }
    }
}
