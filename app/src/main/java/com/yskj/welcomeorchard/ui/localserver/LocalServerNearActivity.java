package com.yskj.welcomeorchard.ui.localserver;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.LocalServerNearListAdapter;
import com.yskj.welcomeorchard.adapter.RootListViewAdapter;
import com.yskj.welcomeorchard.adapter.SubListViewAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.LocalServerMainListBean;
import com.yskj.welcomeorchard.entity.LocalServerNearPopBean;
import com.yskj.welcomeorchard.utils.ScreenUtils;
import com.yskj.welcomeorchard.widget.HorizontalListView;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Administrator on 2016/11/7 0007.
 */
public class LocalServerNearActivity extends BaseActivity {
    @Bind(R.id.tv_address1)
    TextView tvAddress;
    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.tv_kind)
    TextView tvKind;
    @Bind(R.id.ll_kinds)
    LinearLayout llKinds;
    @Bind(R.id.check_distance)
    TextView checkDistance;
    @Bind(R.id.tv_more)
    TextView tvMore;
    @Bind(R.id.hor_list)
    HorizontalListView horList;
    @Bind(R.id.no_scroll_listView)
    NoScrollListView noScrollListView;
    @Bind(R.id.scrollView)
    PullToRefreshScrollView scrollView;
    @Bind(R.id.sever_near_close)
    ImageView severNearClose;
    /**
     * 分类popupwindow
     */
    private PopupWindow mPopupWindow;
    /**
     * 弹出的分类popupWindow布局
     */
    private LinearLayout popupLayout;
    /**
     * 二级菜单的根目录
     */
    private ListView rootListView;
    /**
     * 根目录的节点
     */
    private ArrayList<String> roots = new ArrayList<>();
    /**
     * 子目录的布局
     */
    private FrameLayout subLayout;
    /**
     * 子目录的布局
     */
    private ListView subListView;
    /**
     * 根目录被选中的节点
     */
    private int selectedPosition;
    /**
     * 子目录节点
     */
    private ArrayList<ArrayList<LocalServerNearPopBean.Children>> sub_items = new ArrayList<>();

    //距离
    private PopupWindow distancePopupWindow;

    private ArrayList<LocalServerNearPopBean.Children> arrayList = new ArrayList<LocalServerNearPopBean.Children>();

    private ArrayList<LocalServerMainListBean.LocalLifesBean> localLifes = new ArrayList<>();
    private ArrayList<LocalServerMainListBean.LocalLifesBean> list = new ArrayList<>();
    private LocalServerNearListAdapter adapter;
    private String category_id = "";
    private String search_type = "1";
    private String distance = "10";
    private int offset = 0;

    private static final int REQUESRCODE = 1003;
    private String strAddress;
    private String cityId = "";
    private LocalServerNearPopBean localServerNearPopBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_server_near);
        ButterKnife.bind(this);
        initView();
        initData();
        initList();
    }

    private void initData() {
        //弹框数据请求
        OkHttpUtils.get().url(Urls.LOCALSERVERPOPWIN).build().execute(new LocalServerPopwinCallBack());
    }

    private class LocalServerPopwinCallBack extends Callback<LocalServerNearPopBean> {
        @Override
        public void onBefore(Request request, int id) {
            startMyDialog();
            super.onBefore(request, id);
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            stopMyDialog();
            scrollView.onRefreshComplete();
        }

        @Override
        public LocalServerNearPopBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            localServerNearPopBean = new Gson().fromJson(s, new TypeToken<LocalServerNearPopBean>() {
            }.getType());
            return localServerNearPopBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
            stopMyDialog();
            scrollView.onRefreshComplete();
        }

        @Override
        public void onResponse(LocalServerNearPopBean response, int id) {
            for (LocalServerNearPopBean.RetData retData : response.ret_data) {
                roots.add(retData.name);
            }
        }
    }

    private class LocalServerNearCallBack extends Callback<LocalServerMainListBean> {
        @Override
        public void onBefore(Request request, int id) {
            startMyDialog();
            super.onBefore(request, id);
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            stopMyDialog();
            scrollView.onRefreshComplete();
        }

        @Override
        public LocalServerMainListBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.d(s);
            LocalServerMainListBean localServerMainListBean = new Gson().fromJson(s, new TypeToken<LocalServerMainListBean>() {
            }.getType());
            return localServerMainListBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
            stopMyDialog();
            scrollView.onRefreshComplete();
        }

        @Override
        public void onResponse(LocalServerMainListBean response, int id) {
            if (response.code == 0) {
                list.clear();
                Logger.json(response.data.localLifes.toString());
                list = response.data.localLifes;
                localLifes.addAll(list);
                if (list == null || list.size() == 0) {
                    showToast("暂时没有数据");
                }
                adapter.notifyDataSetChanged();
                stopMyDialog();
                scrollView.onRefreshComplete();
                noScrollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(LocalServerNearActivity.this, LocalServerDetailActivity.class);
                        intent.putExtra("life_id", localLifes.get(position).lifeId + "");
                        intent.putExtra("sell", localLifes.get(position).saleCount + "");
                        intent.putExtra("distance", localLifes.get(position).distanceString + "");
                        startActivity(intent);
                    }
                });
            }
        }
    }

    private void initList() {
        OkHttpUtils.get().url(Urls.LOCALSERVERFIND).addParams("latitude", sp.getString(Config.SPKEY_LATITUDE, "0"))
                .addParams("longitude", sp.getString(Config.SPKEY_LONGITUDE, "0")).addParams("categoryId", category_id)
                .addParams("searchType", search_type).addParams("distance", distance).addParams("offset", offset * 10 + "").build().execute(new LocalServerNearCallBack());
    }


    private void initView() {
        severNearClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvAddress.setText(sp.getString(Config.SPKEY_CITYNAME, "许昌"));
        etSearch.setFocusable(false);
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, LookUpActivity.class));
            }
        });
        scrollView.setMode(PullToRefreshBase.Mode.BOTH);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                offset = 0;
                localLifes.clear();
                initList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                offset++;
                initList();
            }
        });

        llKinds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offset = 0;
                search_type = "1";
                distance = "10";
                checkDistance.setText("10km");
                if (roots == null || roots.size() == 0) {
                    showToast("暂时没有分类");
                    return;
                }
                showPopBtn(ScreenUtils.getScreenWidth(LocalServerNearActivity.this),
                        ScreenUtils.getScreenHeight(LocalServerNearActivity.this));
            }
        });
        checkDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDistancePopupWindow();
            }
        });
        adapter = new LocalServerNearListAdapter(context, localLifes);
        noScrollListView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESRCODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    strAddress = bundle.getString("name");
                    cityId = bundle.getString("cityId");
                    tvAddress.setText(strAddress + "");
                    localLifes.clear();
                    initList();
                }

            }
        }
    }

    //距离
    private void getDistancePopupWindow() {
        final ArrayList distanceList = new ArrayList();
        distanceList.add("0.5km");
        distanceList.add("1.5km");
        distanceList.add("2.5km");
        distanceList.add("3.5km");
        distanceList.add("10km");
        LayoutInflater inflater = LayoutInflater.from(this);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.pop_local_kind, null);
        // 创建PopupWindow对象
        distancePopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        distancePopupWindow.setWidth(checkDistance.getWidth());
        final NoScrollListView listView = (NoScrollListView) view.findViewById(R.id.list);
        PopAdapter adapter = new PopAdapter(LocalServerNearActivity.this, distanceList);
        listView.setAdapter(adapter);

        // 需要设置一下此参数，点击外边可消失
        distancePopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失
        distancePopupWindow.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        distancePopupWindow.setFocusable(true);

        distancePopupWindow.showAsDropDown(checkDistance);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkDistance.setText(distanceList.get(i).toString());
                search_type = "1";
                if (distanceList.get(i).toString().equals("0.5km")) {
                    distance = "0.5";
                } else if (distanceList.get(i).toString().equals("10km")) {
                    distance = "10";
                } else {
                    distance = distanceList.get(i).toString().substring(0, 3);
                }
                offset = 0;
                localLifes.clear();
                initList();
                distancePopupWindow.dismiss();
            }
        });
    }

    //分类
    private void showPopBtn(int screenWidth, int screenHeight) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(LocalServerNearActivity.this);
        popupLayout = (LinearLayout) inflater.inflate(
                R.layout.pop_layout_local_server, null, false);
        rootListView = (ListView) popupLayout.findViewById(R.id.root_listview);
        final RootListViewAdapter adapter = new RootListViewAdapter(
                LocalServerNearActivity.this);
        adapter.setItems(roots);
        rootListView.setAdapter(adapter);

        /**
         * 子popupWindow
         */
        subLayout = (FrameLayout) popupLayout.findViewById(R.id.sub_popupwindow);

        /**
         * 初始化subListview
         */
        subListView = (ListView) popupLayout.findViewById(R.id.sub_listview);

        /**
         * 弹出popupwindow时，二级菜单默认隐藏，当点击某项时，二级菜单再弹出
         */
        subLayout.setVisibility(View.INVISIBLE);

        /**
         * 初始化mPopupWindow
         */
        mPopupWindow = new PopupWindow(popupLayout, screenWidth,
                FrameLayout.LayoutParams.WRAP_CONTENT, true);

        /**
         * 有了mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
         * 这句可以使点击popupwindow以外的区域时popupwindow自动消失 但这句必须放在showAsDropDown之前
         */
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

        /**
         * popupwindow的位置，第一个参数表示位于哪个控件之下 第二个参数表示向左右方向的偏移量，正数表示向左偏移，负数表示向右偏移
         * 第三个参数表示向上下方向的偏移量，正数表示向下偏移，负数表示向上偏移
         *
         */
        mPopupWindow.showAsDropDown(tvKind, -5, 20);// 在控件下方显示popwindow

        mPopupWindow.update();

        rootListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // TODO Auto-generated method stub

                        /**
                         * 选中root某项时改变该ListView item的背景色
                         */
                        adapter.setSelectedPosition(position);
                        adapter.notifyDataSetInvalidated();
                        selectedPosition = position;
                        for (int i = 0; i < localServerNearPopBean.ret_data.size(); i++) {
                            arrayList.clear();
                            for (int n = 0; n < localServerNearPopBean.ret_data.get(selectedPosition).childrens.size(); n++) {
                                arrayList.add(localServerNearPopBean.ret_data.get(selectedPosition).childrens.get(n));
                            }
                            sub_items.add(arrayList);
                        }
                        //如果二级分类中数据为空，直接请求一级分类数据
                        if (arrayList.size() == 0) {
                            mPopupWindow.dismiss();
                            category_id = localServerNearPopBean.ret_data.get(selectedPosition).cateId;
                            tvKind.setText(localServerNearPopBean.ret_data.get(selectedPosition).name);
                            localLifes.clear();
                            initList();
                            return;
                        }
                        SubListViewAdapter subAdapter = new SubListViewAdapter(
                                LocalServerNearActivity.this, sub_items, position);
                        subListView.setAdapter(subAdapter);
                        subAdapter.notifyDataSetChanged();
                        /**
                         * 选中某个根节点时，使显示相应的子目录可见
                         */
                        subLayout.setVisibility(View.VISIBLE);
                        subListView
                                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(
                                            AdapterView<?> parent, View view,
                                            int position, long id) {
                                        // TODO Auto-generated method stub
                                        mPopupWindow.dismiss();
                                        category_id = sub_items.get(selectedPosition).get(position).cateId;
                                        tvKind.setText(sub_items.get(selectedPosition).get(position).name);
                                        localLifes.clear();
                                        initList();
                                    }
                                });

                    }
                });
    }

    //pop适配器
    private class PopAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> list;

        public PopAdapter(Context context, ArrayList<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list == null || list.size() == 0) {
                return 0;
            }
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_pop_local_kind, null);
                holder.textView = (TextView) convertView.findViewById(R.id.item_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(list.get(i).toString());
            return convertView;
        }

        private class ViewHolder {
            TextView textView;
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//                Intent intent = new Intent(LocalServerNearActivity.this, BottomMenu.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                break;
//        }
//        return false;
//    }
}
