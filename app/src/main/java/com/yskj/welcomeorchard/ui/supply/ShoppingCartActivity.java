package com.yskj.welcomeorchard.ui.supply;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.MyExpandableListAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.entity.LocalServerEntity;
import com.yskj.welcomeorchard.entity.LocalServerNumEntity;
import com.yskj.welcomeorchard.interfaces.OnShoppingCartChangeListener;
import com.yskj.welcomeorchard.utils.LoadingCaches;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  陈宙洋
 * 2017/8/4.
 * 描述：购物车界面
 */
public class ShoppingCartActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.expandableListView)
    ExpandableListView expandableListView;
    @Bind(R.id.ivSelectAll)
    ImageView ivSelectAll;
    @Bind(R.id.btnSettle)
    TextView btnSettle;
    @Bind(R.id.tvCountMoney)
    TextView tvCountMoney;
    @Bind(R.id.txt_title)
    TextView tvTitle;
    @Bind(R.id.rlShoppingCartEmpty)
    RelativeLayout rlShoppingCartEmpty;
    @Bind(R.id.rlBottomBar)
    RelativeLayout rlBottomBar;
    private List<LocalServerEntity> mListGoods = new ArrayList<>();
    private MyExpandableListAdapter adapter;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String listgoods = "null";
    private String allAmount,allCloudOffse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_shopping_cart);
        ButterKnife.bind(this);
        tvTitle.setText("购物车");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ivSelectAll.setImageResource(R.mipmap.fzf);
        requestShoppingCartList();
    }

    private void setAdapter() {
        adapter = new MyExpandableListAdapter(this);
        expandableListView.setAdapter(adapter);
        adapter.setOnShoppingCartChangeListener(new OnShoppingCartChangeListener() {
            @Override
            public void onDataChange(String selectCount, String selectMoney) {
                if (mListGoods.size() == 0) {
                    showEmpty(true);
                } else {
                    showEmpty(false);//其实不需要做这个判断，因为没有商品的时候，必须退出去添加商品；
                }

                String countMoney = String.format(getResources().getString(R.string.count_money), selectMoney);
                String countGoods = String.format(getResources().getString(R.string.count_goods), selectCount);
                tvCountMoney.setText(countMoney);
                btnSettle.setText(countGoods);
                tvTitle.setText("购物车");
            }

            @Override
            public void onSelectItem(boolean isSelectedAll) {
                ShoppingCartBiz.checkItem(isSelectedAll, ivSelectAll);
            }
        });
        //通过监听器关联Activity和Adapter的关系，解耦；
        View.OnClickListener listener = adapter.getAdapterListener();
        if (listener != null) {
            //即使换了一个新的Adapter，也要将“全选事件”传递给adapter处理；
            ivSelectAll.setOnClickListener(adapter.getAdapterListener());
            //结算时，一般是需要将数据传给订单界面的
            btnSettle.setOnClickListener(adapter.getAdapterListener());
        }
    }

    public void showEmpty(boolean isEmpty) {
        if (isEmpty) {
            expandableListView.setVisibility(View.GONE);
            rlShoppingCartEmpty.setVisibility(View.VISIBLE);
            rlBottomBar.setVisibility(View.GONE);
        } else {
            expandableListView.setVisibility(View.VISIBLE);
            rlShoppingCartEmpty.setVisibility(View.GONE);
            rlBottomBar.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        img_back.setOnClickListener(this);
        expandableListView.setGroupIndicator(null);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
    }

    /** 获取购物车列表的数据（数据和网络请求也是非通用部分） */
    private void requestShoppingCartList() {
        mListGoods = DataSupport.findAll(LocalServerEntity.class);
        for (LocalServerEntity item : mListGoods) {
            List<LocalServerNumEntity> list = DataSupport.where("shopid=?",item.getShopid()+"").find(LocalServerNumEntity.class);
            item.setLocalServerNumEntityList(list);
         }
        setAdapter();
        updateListView();

    }

    private void updateListView() {
        adapter.setList(mListGoods);
        adapter.notifyDataSetChanged();
        expandAllGroup();
    }

    /**
     * 展开所有组
     */
    private void expandAllGroup() {
        for (int i = 0; i < mListGoods.size(); i++) {
            expandableListView.expandGroup(i);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }
}
