package com.yskj.welcomeorchard.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2017/8/24.
 */

public class LocalServerEntity extends DataSupport implements Serializable{
    @Column(unique = true,nullable = false)
    private long id;
    @Column(nullable = false)
    private String shopname;//供应商名称
    @Column(nullable = false)
    private String goodsid;//商品id
    @Column(nullable = false)
    private String shopid;//供应商id
    @Column(nullable = false)
    private int parameterid;//规格Id
    /** 是否处于编辑状态 */
    @Column(nullable = false)
    private boolean isEditing;
    /** 组是否被选中 */
    @Column(nullable = false)
    private boolean isGroupSelected;

    public int getParameterid() {
        return parameterid;
    }

    public void setParameterid(int parameterid) {
        this.parameterid = parameterid;
    }
    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    public boolean isGroupSelected() {
        return isGroupSelected;
    }

    public void setGroupSelected(boolean groupSelected) {
        isGroupSelected = groupSelected;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    private List<LocalServerNumEntity> localServerNumEntityList = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public List<LocalServerNumEntity> getLocalServerNumEntityList() {
        return localServerNumEntityList;
    }

    public void setLocalServerNumEntityList(List<LocalServerNumEntity> localServerNumEntityList) {
        this.localServerNumEntityList = localServerNumEntityList;
    }
}
