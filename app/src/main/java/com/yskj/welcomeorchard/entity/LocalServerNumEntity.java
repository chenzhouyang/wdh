package com.yskj.welcomeorchard.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;

import javax.sql.DataSource;

/**
 * Created by chen on 2017/8/24.
 */
public class LocalServerNumEntity extends DataSupport implements Serializable {
    @Column(unique = true,nullable = false)
    private long id;
    @Column(nullable = false)
    private String parametername;//规格名称
    @Column(nullable = false)
    private int parameterid;//规格Id
    @Column(nullable = false)
    private String goodsname;//商品名称
    @Column(nullable = false)
    private double price;//商品价格
    @Column(nullable = false)
    private String goodsid;//商品ID
    @Column(nullable = false)
    private int count = 1;//数量
    @Column(nullable = false)
    private String shopid;//商铺ID
    @Column(nullable = false)
    private String goodsimage;//商品图片
    @Column(nullable = false)
    private double maccount;//商品图片
    /** 是否是编辑状态 */
    @Column(nullable = false)
    private boolean isEditing;
    /** 是否被选中 */
    @Column(nullable = false)
    private boolean isChildSelected;

    public double getMaccount() {
        return maccount;
    }

    public void setMaccount(double maccount) {
        this.maccount = maccount;
    }

    public String getParametername() {
        return parametername;
    }

    public void setParametername(String parametername) {
        this.parametername = parametername;
    }

    public int getParameterid() {
        return parameterid;
    }

    public void setParameterid(int parameterid) {
        this.parameterid = parameterid;
    }
    public String getGoodsimage() {
        return goodsimage;
    }

    public void setGoodsimage(String goodsimage) {
        this.goodsimage = goodsimage;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    public boolean isChildSelected() {
        return isChildSelected;
    }

    public void setChildSelected(boolean childSelected) {
        isChildSelected = childSelected;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    private LocalServerEntity localServerEntity;

    public LocalServerEntity getLocalServerEntity() {
        return localServerEntity;
    }

    public void setLocalServerEntity(LocalServerEntity localServerEntity) {
        this.localServerEntity = localServerEntity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
