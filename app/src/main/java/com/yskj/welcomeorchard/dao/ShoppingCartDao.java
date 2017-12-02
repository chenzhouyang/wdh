package com.yskj.welcomeorchard.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.yskj.welcomeorchard.entity.LocalServerEntity;
import com.yskj.welcomeorchard.entity.LocalServerNumEntity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * SHz
 */
public class ShoppingCartDao {

    private volatile static ShoppingCartDao instance = null;
    private SQLiteDatabase db;

    /**
     * 获取SimpleDemoDB实例
     */
    public static ShoppingCartDao getInstance() {
        if (instance == null) {
            synchronized (ShoppingCartDao.class) {
                if (instance == null) {
                    instance = new ShoppingCartDao();
                }
            }
        }
        return instance;
    }

    private Cursor cursor;

    public void close() {
        if (db != null) {
            db.close();
        }
        if (cursor != null) {
            cursor.close();
        }
    }


 /*   public boolean isExistGood(String productID) {
        if (productID == null) {
            return false;
        }
        db = DBHelper.getInstance().getReadableDatabase();
        cursor = db.query(DBHelper.TB_SHOPPING_CART, null, ShoppingCartBean.KEY_PRODUCT_ID + "=?", new String[]{productID}, null, null, null);
        boolean isExist = cursor.moveToFirst();
        close();
        return isExist;
    }*/

    /**
     * 添加购物车商品信息
     *
     * @param productID 规格ID
     * @param num       商品数量
     */
    public void saveShoppingInfo(String productID, String num) {
       /* if (productID == null || "".equals(productID) || num == null || "".equals(num)) {
            return;
        }
        db = DBHelper.getInstance().getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShoppingCartBean.KEY_PRODUCT_ID, productID);
        values.put(ShoppingCartBean.KEY_NUM, num);
        db.insert(DBHelper.TB_SHOPPING_CART, null, values);
        close();*/
    }

    /**
     * 删除购物车商品
     *
     * @param productID 规格ID
     */
    public void deleteShoppingInfo(String productID) {
      if (productID == null) {
            return;
        }
        DataSupport.deleteAll(LocalServerNumEntity.class,"parameterid=?",productID);
    }

    public void delAllGoods(String goodsid) {
        DataSupport.deleteAll(LocalServerEntity.class,"goodsid=?",goodsid);
    }
    /**
     * 修改购物车中某件商品的信息
     *
     * @param productID 规格ID
     * @param num       商品数量
     */
    public void updateGoodsNum(int productID, int num) {
        LocalServerNumEntity localServerUpdateNumEntity = new LocalServerNumEntity();
        localServerUpdateNumEntity.setCount(num);
        localServerUpdateNumEntity.updateAll("parameterid = ?",productID+"");
    }

  /*  public String getNumByProductID(String productID) {
        if (productID == null) {
            return "1";
        }
        db = DBHelper.getInstance().getReadableDatabase();
        cursor = db.query(DBHelper.TB_SHOPPING_CART, new String[]{ShoppingCartBean.KEY_NUM}, ShoppingCartBean.KEY_PRODUCT_ID + "=?", new String[]{productID}, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        close();
        return "1";
    }*/

    /**
     * 查询数据库中的购物车中的商品信息
     *
     * @return 购物车中的商品信息
     */
 /*   public List<String> getProductList() {
        db = DBHelper.getInstance().getReadableDatabase();
        List<String> mList = new ArrayList<>();
        Cursor cursor = db.query(DBHelper.TB_SHOPPING_CART, new String[]{ShoppingCartBean.KEY_PRODUCT_ID}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String productID = cursor.getString(0);
                if (productID != null && !"".equals(productID)) {
                    mList.add(productID);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return mList;
    }*/


}
