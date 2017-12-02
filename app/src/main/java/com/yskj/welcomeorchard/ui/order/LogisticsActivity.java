package com.yskj.welcomeorchard.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.LogisticsInfoBean;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by wdx on 2016/10/24 0024.
 */
public class LogisticsActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    private ListView logistics_info;
    private String number, shipping_code, shipping_name;
    private TextView txt_number, txt_shiping_code, txt_logistics_null;
    private ArrayList<LogisticsInfoBean> list;
    private int q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_layout);
        ButterKnife.bind(this);
        txtTitle.setText("物流查询");
        Intent intent = getIntent();
        number = intent.getStringExtra("number");
        shipping_code = intent.getStringExtra("shipping_code");
        shipping_name = intent.getStringExtra("shipping_name");
        list = new ArrayList();
        initView();
    }


    public void initView() {
        txt_number = (TextView) findViewById(R.id.txt_number);
        txt_number.setText(number + "");
        txt_logistics_null = (TextView) findViewById(R.id.txt_logistics_null);
        txt_shiping_code = (TextView) findViewById(R.id.txt_shipping_name);
        txt_shiping_code.setText(shipping_name + "");
        logistics_info = (ListView) findViewById(R.id.logistics_info);
        OkHttpUtils.get().url(Urls.LOGISTICS).addParams("type", shipping_code)
                .addParams("postid", number).build().execute(new StringCallback() {
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                stopMyDialog();
            }

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                startMyDialog();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                txt_logistics_null.setVisibility(View.GONE);
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                if(map!=null){
                    JSONArray array = (JSONArray) map.get("data");
                    String status = (String) map.get("status");
                    String message = (String) map.get("message");
                    LogisticsInfoBean bean = null;
                    if (status.equals("200")) {
                        for (int q = 0; q < array.length(); q++) {
                            bean = new LogisticsInfoBean();
                            try {
                                JSONObject object1 = array.getJSONObject(q);
                                String context = object1.getString("context");
                                String time = object1.getString("time");
                                String ftime = object1.getString("ftime");
                                bean.context = context;
                                bean.time = time;
                                bean.ftime = ftime;
                                list.add(bean);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        logistics_info.setAdapter(new LogisticsAdapter());
                    } else {
                        txt_logistics_null.setVisibility(View.VISIBLE);
                        txt_logistics_null.setText("物流信息暂未更新");
                    }
                }

            }
        });

    }

    @OnClick(R.id.img_back)
    public void onClick() {
        finish();
    }

    private class LogisticsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(getBaseContext(), R.layout.item_logistics, null);
                holder.content = (TextView) convertView.findViewById(R.id.content);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.view = (View) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.content.setText(list.get(position).context);
            holder.time.setText(list.get(position).time);
            if (position == 0) {
                holder.view.setBackgroundResource(R.mipmap.yuan_green);
            }
            return convertView;
        }

        public class ViewHolder {
            TextView content, time, ftime;
            View view;
        }
    }
}


