package com.yskj.welcomeorchard.ui.supply;

import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.dao.ShoppingCartDao;
import com.yskj.welcomeorchard.entity.LocalServerEntity;
import com.yskj.welcomeorchard.entity.LocalServerNumEntity;
import com.yskj.welcomeorchard.utils.DecimalUtil;
import com.yskj.welcomeorchard.utils.StringUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 绯若虚无（https://github.com/joefei） on 2015/10/12.
 */
public class ShoppingCartBiz {

    /**
     * 选择全部，点下全部按钮，改变所有商品选中状态
     */
    public static boolean selectAll(List<LocalServerEntity> list, boolean isSelectAll, ImageView ivCheck) {
        isSelectAll = !isSelectAll;
        ShoppingCartBiz.checkItem(isSelectAll, ivCheck);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setGroupSelected(isSelectAll);
            for (int j = 0; j < list.get(i).getLocalServerNumEntityList().size(); j++) {
                list.get(i).getLocalServerNumEntityList().get(j).setChildSelected(isSelectAll);
            }
        }
        return isSelectAll;
    }

    /**
     * 族内的所有组，是否都被选中，即全选
     *
     * @param list
     * @return
     */
    private static boolean isSelectAllGroup(List<LocalServerEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            boolean isSelectGroup = list.get(i).isGroupSelected();
            if (!isSelectGroup) {
                return false;
            }
        }
        return true;
    }

    /**
     * 组内所有子选项是否全部被选中
     *
     * @param list
     * @return
     */
    private static boolean isSelectAllChild(List<LocalServerNumEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            boolean isSelectGroup = list.get(i).isChildSelected();
            if (!isSelectGroup) {
                return false;
            }
        }
        return true;
    }

    /**
     * 单选一个，需要判断整个组的标志，整个族的标志，是否被全选，取消，则
     * 除了选择全部和选择单个可以单独设置背景色，其他都是通过改变值，然后notify；
     *
     * @param list
     * @param groudPosition
     * @param childPosition
     * @return 是否选择全部
     */
    public static boolean selectOne(List<LocalServerEntity> list, int groudPosition, int childPosition) {
        boolean isSelectAll;
        boolean isSelectedOne = !(list.get(groudPosition).getLocalServerNumEntityList().get(childPosition).isChildSelected());
        list.get(groudPosition).getLocalServerNumEntityList().get(childPosition).setChildSelected(isSelectedOne);//单个图标的处理
        boolean isSelectCurrentGroup = isSelectAllChild(list.get(groudPosition).getLocalServerNumEntityList());
        list.get(groudPosition).setGroupSelected(isSelectCurrentGroup);//组图标的处理
        isSelectAll = isSelectAllGroup(list);
        return isSelectAll;
    }

    public static boolean selectGroup(List<LocalServerEntity> list, int groudPosition) {
        boolean isSelectAll;
        boolean isSelected = !(list.get(groudPosition).isGroupSelected());
        list.get(groudPosition).setGroupSelected(isSelected);
        for (int i = 0; i < list.get(groudPosition).getLocalServerNumEntityList().size(); i++) {
            list.get(groudPosition).getLocalServerNumEntityList().get(i).setChildSelected(isSelected);
        }
        isSelectAll = isSelectAllGroup(list);
        return isSelectAll;
    }

    /**
     * 勾与不勾选中选项
     *
     * @param isSelect 原先状态
     * @param ivCheck
     * @return 是否勾上，之后状态
     */
    public static boolean checkItem(boolean isSelect, ImageView ivCheck) {
        if (isSelect) {
            ivCheck.setImageResource(R.mipmap.fzg);

        } else {
            ivCheck.setImageResource(R.mipmap.fzf);
        }
        return isSelect;
    }

    /**=====================上面是界面改动部分，下面是数据变化部分=========================*/

    /**
     * 获取结算信息，肯定需要获取总价和数量，但是数据结构改变了，这里处理也要变；
     *
     * @return 0=选中的商品数量；1=选中的商品总价
     */
    public static String[] getShoppingCount(List<LocalServerEntity> listGoods) {
        String[] infos = new String[2];
        String selectedCount = "0";
        String selectedMoney = "0";
        for (int i = 0; i < listGoods.size(); i++) {
            for (int j = 0; j < listGoods.get(i).getLocalServerNumEntityList().size(); j++) {
                boolean isSelectd = listGoods.get(i).getLocalServerNumEntityList().get(j).isChildSelected();
                if (isSelectd) {
                    String price = StringUtils.getStringtodouble(listGoods.get(i).getLocalServerNumEntityList().get(j).getPrice());
                    String num = listGoods.get(i).getLocalServerNumEntityList().get(j).getCount()+"";
                    String countMoney = DecimalUtil.multiply(price, num);
                    selectedMoney = DecimalUtil.add(selectedMoney, countMoney);
                    selectedCount = DecimalUtil.add(selectedCount, "1");
                }
            }
        }
        infos[0] = selectedCount;
        infos[1] = selectedMoney;
        return infos;
    }


    public static boolean hasSelectedGoods(List<LocalServerEntity> listGoods) {
        String count = getShoppingCount(listGoods)[0];
        if ("0".equals(count)) {
            return false;
        }
        return true;
    }

    /**
     * 添加某商品的数量到数据库（非通用部分，都有这个动作，但是到底存什么，未可知）
     *
     * @param productID 此商品的规格ID
     * @param num       此商品的数量
     */
    public static void addGoodToCart(String productID, String num) {
        ShoppingCartDao.getInstance().saveShoppingInfo(productID, num);
    }

    /**
     * 删除某个商品,即删除其ProductID
     *
     * @param productID 规格ID
     */
    public static void delGood(String productID) {
        ShoppingCartDao.getInstance().deleteShoppingInfo(productID);
    }

    /** 删除全部商品 */
    public static void delAllGoods(String shopid) {
        ShoppingCartDao.getInstance().delAllGoods(shopid);
    }

    /** 增减数量，操作通用，数据不通用 */
    public static void addOrReduceGoodsNum(boolean isPlus, LocalServerNumEntity goods, TextView tvNum) {
        int currentNum = goods.getCount();
        int num = 1;
        if (isPlus) {
            num = currentNum + 1;
        } else {
            int i = currentNum;
            if (i > 1) {
                num = i - 1;
            } else {
                num = 1;
            }
        }
        int productID = goods.getParameterid();
        tvNum.setText(num+"");
        goods.setCount(num);
        updateGoodsNumber(productID, num);
    }

    /**
     * 更新购物车的单个商品数量
     *
     * @param productID
     * @param num
     */
    public static void updateGoodsNumber(int productID, int num) {
        ShoppingCartDao.getInstance().updateGoodsNum(productID, num);
    }

    /**
     * 查询购物车商品总数量
     * <p/>
     * 统一使用该接口，而就行是通过何种方式获取数据，数据库、SP、文件、网络，都可以
     *
     * @return
     */

    /**
     * 获取所有商品ID，用于向服务器请求数据（非通用部分）
     *
     * @return
     */
    /*public static List<String> getAllProductID() {
        return ShoppingCartDao.getInstance().getProductList();
    }*/


}
