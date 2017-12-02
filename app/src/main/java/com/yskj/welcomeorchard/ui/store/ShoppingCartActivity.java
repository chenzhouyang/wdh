package com.yskj.welcomeorchard.ui.store;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.ShoppingCartAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.ShoppingCartEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.ui.order.ConfirmOrderActivity;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.NumberFormat;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by YSKJ-JH on 2017/1/13.
 */

public class ShoppingCartActivity extends BaseActivity implements View.OnClickListener, ShoppingCartAdapter.CheckInterface, ShoppingCartAdapter.ModifyCountInterface {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.list_shopping_cart)
    ListView listShoppingCart;
    @Bind(R.id.ck_all)
    CheckBox ckAll;
    @Bind(R.id.tv_settlement)
    TextView tvSettlement;
    @Bind(R.id.tv_show_price)
    TextView tvShowPrice;
    @Bind(R.id.shopping_clear_away)
    TextView shoppingClearAway;
    @Bind(R.id.cart_clear_away)
    LinearLayout cartClearAway;
    @Bind(R.id.rl_bottom)
    LinearLayout rlBottom;
    private ShoppingCartAdapter shoppingCartAdapter;
    private ArrayList<ShoppingCartEntity.CartListBean> shoppingCartBeanList = new ArrayList<>();
    private boolean mSelect;
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0, id;// 购买的商品总数量
    private UserInfoEntity userInfoEntity;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String type;
    private LinearLayout llTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止所有activity横屏
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("type");
        if (type.equals("0")) {
            imgBack.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initData();
        ckAll.setChecked(false);
        tvSettlement.setText("结算(0)");
    }

    private void initView() {
        if (!caches.get("userinfo").equals("null")) {
            userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
            }.getType());
            id = userInfoEntity.data.userVo.id;
            txtTitle.setText("购物车");
            ckAll.setOnClickListener(this);
            tvSettlement.setOnClickListener(this);
            imgBack.setOnClickListener(this);
            shoppingCartAdapter = new ShoppingCartAdapter(this);
            shoppingCartAdapter.setCheckInterface(this);
            shoppingCartAdapter.setModifyCountInterface(this);
            shoppingClearAway.setOnClickListener(this);
            listShoppingCart.setAdapter(shoppingCartAdapter);
            shoppingCartAdapter.setShoppingCartBeanList(shoppingCartBeanList);
            String str = "总金额：<font color=\"#e42020\">￥" + 0.00 + "</font>";
            tvShowPrice.setText(Html.fromHtml(str));
        } else {
            new AlertDialog.Builder(ShoppingCartActivity.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("服务器忙着哩，请耐心等待")//设置显示的内容
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

    protected void initData() {
        shoppingCartBeanList.clear();
        OkHttpUtils.get().url(Urls.QUERYSHOPPINGCART + id).build().execute(new queryCartListCallBack());
    }

    private class queryCartListCallBack extends Callback<ShoppingCartEntity> {
        @Override
        public void onBefore(Request request, int id) {
            super.onBefore(request, id);
            startMyDialog();
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            stopMyDialog();
        }

        @Override
        public ShoppingCartEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            ShoppingCartEntity shoppingCartEntity = new Gson().fromJson(s, new TypeToken<ShoppingCartEntity>() {
            }.getType());
            return shoppingCartEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
        }

        @Override
        public void onResponse(ShoppingCartEntity response, int id) {
            if (response.errorCode.equals("000")) {
                shoppingCartBeanList.addAll(response.cartList);

               /* for (ShoppingCartEntity.CartListBean group : shoppingCartBeanList) {
                    if (group.is_fail!=null&&group.is_fail.equals("1")){
                        cartClearAway.setVisibility(View.VISIBLE);
                    }
                }*/
                shoppingCartAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //全选按钮
            case R.id.ck_all:
                if (shoppingCartBeanList.size() != 0) {
                    if (ckAll.isChecked()) {
                        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                            shoppingCartBeanList.get(i).setChoosed(true);
                        }
                        shoppingCartAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                            shoppingCartBeanList.get(i).setChoosed(false);
                        }
                        shoppingCartAdapter.notifyDataSetChanged();
                    }
                }
                statistics();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_settlement:
                //向后台提交数据，让其修改数据库
                postToPhp();
                break;
            case R.id.shopping_clear_away:
                clearaway();
                break;
        }
    }

    /**
     * 清除失效商品
     */
    private void clearaway(){
        OkHttpUtils.get().url(Urls.CLEARAWAY+id).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showToast("网络连接错误");
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String,Object> map = JSONFormat.jsonToMap(response);
                String code = (String) map.get("error_code");
                if(code.equals("000")){
                    cartClearAway.setVisibility(View.GONE);
                    initData();
                }
            }
        });
    }
    private void postToPhp() {
        StringBuilder goodsNumJson = new StringBuilder();
        StringBuilder cartSelectJson = new StringBuilder();
        String selectAll = "0";
        final ArrayList<ShoppingCartEntity.CartListBean> cartListBeenIsChecked = new ArrayList<>();
        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
            ShoppingCartEntity.CartListBean shoppingCartBean = shoppingCartBeanList.get(i);
            if (i == 0) {
                goodsNumJson.append("{\"" + shoppingCartBean.id + "\":\"" + shoppingCartBean.goodsNum + "\",");
            }
            if (i != 0 && i != shoppingCartBeanList.size() - 1) {
                goodsNumJson.append("\"" + shoppingCartBean.id + "\":\"" + shoppingCartBean.goodsNum + "\",");
            }
            if (i == shoppingCartBeanList.size() - 1) {
                goodsNumJson.append("\"" + shoppingCartBean.id + "\":\"" + shoppingCartBean.goodsNum + "\"}");
            }
            if (shoppingCartBean.isChoosed()) {
                cartListBeenIsChecked.add(shoppingCartBean);
            }
        }
        if (cartListBeenIsChecked == null || cartListBeenIsChecked.size() == 0) {
            showToast("请选择结算商品");
            return;
        }
        cartSelectJson.append("{");
        for (int i = 0; i < cartListBeenIsChecked.size(); i++) {
            ShoppingCartEntity.CartListBean shoppingCartBean = cartListBeenIsChecked.get(i);
            if (i != cartListBeenIsChecked.size() - 1) {
                cartSelectJson.append("\"" + shoppingCartBean.id + "\":\"" + shoppingCartBean.goodsNum + "\",");
            } else {
                cartSelectJson.append("\"" + shoppingCartBean.id + "\":\"" + shoppingCartBean.goodsNum + "\"");
            }
        }
        cartSelectJson.append("}");
        if (shoppingCartBeanList.size() == cartListBeenIsChecked.size()) {
            selectAll = "1";
        }
        StringBuilder strJson = new StringBuilder();
        strJson.append("{\"goods_num\":" + goodsNumJson + "," + "\"cart_select\":" + cartSelectJson + "," + "\"select_all\":\"" + selectAll + "\"}");
        Logger.d(strJson.toString());
        OkHttpUtils.postString().url(Urls.QUERYSHOPPINGCART + id).content(strJson.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8")).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showToast("请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                String code = map.get("error_code").toString();
                String error_msg = map.get("error_msg").toString();
                if (code.equals("000")) {
                    Intent intent = new Intent(context, ConfirmOrderActivity.class);
                    intent.putExtra("cartlist", cartListBeenIsChecked);
                    startActivity(intent);
                } else {
                    showToast(error_msg);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * 统计操作
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作
     * 3.给底部的textView进行数据填充
     */
    public void statistics() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
            ShoppingCartEntity.CartListBean shoppingCartBean = shoppingCartBeanList.get(i);
            if (shoppingCartBean.isChoosed()) {
                totalCount++;
                totalPrice += shoppingCartBean.goodsPrice * shoppingCartBean.goodsNum;
            }
        }
        String str = "总金额：<font color=\"#e42020\">￥" + String.format("%.2f", NumberFormat.convertToDouble(totalPrice, 0.00)) + "</font>";
        tvShowPrice.setText(Html.fromHtml(str));
        tvSettlement.setText("结算(" + totalCount + ")");
    }

    /**
     * 单选
     *
     * @param position  组元素位置
     * @param isChecked 组元素选中与否
     */
    @Override
    public void checkGroup(int position, boolean isChecked) {
        shoppingCartBeanList.get(position).setChoosed(isChecked);
        if (isAllCheck()) {
            ckAll.setChecked(true);
        } else {
            ckAll.setChecked(false);
        }
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }

    /**
     * 遍历list集合
     *
     * @return
     */
    private boolean isAllCheck() {
        for (ShoppingCartEntity.CartListBean group : shoppingCartBeanList) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

    /**
     * 增加
     *
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doIncrease(int position, View showCountView, String limit, boolean isChecked) {
        ShoppingCartEntity.CartListBean shoppingCartBean = shoppingCartBeanList.get(position);
        int currentCount = shoppingCartBean.goodsNum;
        currentCount++;
        if (limit != null && !limit.equals("null") && limit.length() != 0) {
            if (currentCount > Integer.parseInt(limit)) {
                showToast("超出限购");
            } else {
                shoppingCartBean.goodsNum = currentCount;
                ((EditText) showCountView).setText(currentCount + "");
                shoppingCartAdapter.notifyDataSetChanged();
                modifyCartNum(position, currentCount);
                statistics();
            }
        } else {
            shoppingCartBean.goodsNum = currentCount;
            ((EditText) showCountView).setText(currentCount + "");
            shoppingCartAdapter.notifyDataSetChanged();
            modifyCartNum(position, currentCount);
            statistics();
        }

    }

    /**
     * 删减
     *
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {
        ShoppingCartEntity.CartListBean shoppingCartBean = shoppingCartBeanList.get(position);
        int currentCount = shoppingCartBean.goodsNum;
        if (currentCount == 1) {
            return;
        }
        currentCount--;
        shoppingCartBean.goodsNum = currentCount;
        ((EditText) showCountView).setText(currentCount + "");
        shoppingCartAdapter.notifyDataSetChanged();
        modifyCartNum(position, currentCount);
        statistics();

    }

    /**
     * 修改购物车数量
     */
    private void modifyCartNum(int position, int currentCount) {
        StringBuilder goodsNumJson = new StringBuilder();
        StringBuilder cartSelectJson = new StringBuilder();
        String selectAll = "0";
        ArrayList<ShoppingCartEntity.CartListBean> cartListBeenIsChecked = new ArrayList<>();
        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
            ShoppingCartEntity.CartListBean shoppingCartBean = shoppingCartBeanList.get(i);
            if (i == 0) {
                if (position == i)
                    goodsNumJson.append("{\"" + shoppingCartBean.id + "\":\"" + currentCount + "\",");
                else
                    goodsNumJson.append("{\"" + shoppingCartBean.id + "\":\"" + shoppingCartBean.goodsNum + "\",");
            }
            if (i != 0 && i != shoppingCartBeanList.size() - 1) {
                if (position == i)
                    goodsNumJson.append("\"" + shoppingCartBean.id + "\":\"" + currentCount + "\",");
                else
                    goodsNumJson.append("\"" + shoppingCartBean.id + "\":\"" + shoppingCartBean.goodsNum + "\",");
            }
            if (i == shoppingCartBeanList.size() - 1) {
                if (position == i)
                    goodsNumJson.append("\"" + shoppingCartBean.id + "\":\"" + currentCount + "\"}");
                else
                    goodsNumJson.append("\"" + shoppingCartBean.id + "\":\"" + shoppingCartBean.goodsNum + "\"}");
            }
        }
        cartSelectJson.append("{");
        for (int i = 0; i < cartListBeenIsChecked.size(); i++) {
            ShoppingCartEntity.CartListBean shoppingCartBean = cartListBeenIsChecked.get(i);
            if (i != cartListBeenIsChecked.size() - 1) {
                cartSelectJson.append("\"" + shoppingCartBean.id + "\":\"" + "1" + "\",");
            } else {
                cartSelectJson.append("\"" + shoppingCartBean.id + "\":\"" + "1" + "\"");
            }
        }
        cartSelectJson.append("}");
        if (shoppingCartBeanList.size() == cartListBeenIsChecked.size()) {
            selectAll = "1";
        }
        StringBuilder strJson = new StringBuilder();
        strJson.append("{\"goods_num\":" + goodsNumJson + "," + "\"cart_select\":" + cartSelectJson + "," + "\"select_all\":" + selectAll + "}");
        Logger.d(strJson.toString());
        OkHttpUtils.postString().url(Urls.QUERYSHOPPINGCART + id).content(strJson.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8")).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showToast("请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                String code = map.get("error_code").toString();
                String error_msg = map.get("error_msg").toString();
                if (code.equals("000")) {
                    showToast("修改成功");
                } else {
                    showToast(error_msg);
                }
            }
        });
    }

    /**
     * 删除
     *
     * @param position
     */
    @Override
    public void childDelete(int position) {
        //通知后台修改数据库
        delData(position);
    }

    //通知后台修改数据库
    private void delData(final int position) {
        OkHttpUtils.get().url(Urls.ADDTOSHOPPINGCART + "2/id/" + shoppingCartBeanList.get(position).id).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showToast("请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                String code = map.get("error_code").toString();
                String error_msg = map.get("error_msg").toString();
                if (code.equals("000")) {
                    showToast("删除成功");
                    shoppingCartBeanList.remove(position);
                    shoppingCartAdapter.notifyDataSetChanged();
                    statistics();
                } else {
                    showToast(error_msg);
                }
            }
        });
    }
}
