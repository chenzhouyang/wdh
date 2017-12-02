package com.yskj.welcomeorchard.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.entity.CityTestEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.yskj.welcomeorchard.R.id.img_back;


public class CitySelect1Activity extends BaseActivity  {
    private ListView city_lv;
    private ArrayList<CityTestEntity> list = new ArrayList<>();
    private String Province, City, Area;
    private int ProvinceId, CityId, AreaId;

    private final static String fileName = "city_full.json";
    private ImageView imag_back;
    private TextView txt_title;
    Handler dataHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            CityAdapter adapter = new CityAdapter(list, CitySelect1Activity.this);
            city_lv.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        setTitle("返回");
        imag_back = (ImageView) findViewById(img_back);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("城市选择");
        imag_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        city_lv = (ListView) findViewById(R.id.city_lv);
        new DataThread().start();
    }

    class DataThread extends Thread {
        @Override
        public void run() {
            String jsonStr = AppJsonFileReader.getJson(CitySelect1Activity.this, fileName);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<CityTestEntity>>() {
            }.getType();
            list = gson.fromJson(jsonStr, type);
            Logger.json(list.toString());
            dataHandler.sendMessage(dataHandler.obtainMessage());
        }
    }

    /**
     * 省适配器
     */
    public class CityAdapter extends BaseAdapter {
        private ArrayList<CityTestEntity> arrayList;
        private Context context;

        public CityAdapter(ArrayList<CityTestEntity> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @Override
        public int getCount() {
            if (arrayList != null || arrayList.size() != 0) {
                return arrayList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
           ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_city, null);
                holder.tv_city = (TextView) convertView.findViewById(R.id.tv_city);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_city.setText(arrayList.get(position).name);
            holder.tv_city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<CityTestEntity.CityBeanX> list = new ArrayList();
                    list = arrayList.get(position).city;
                    Province = arrayList.get(position).name;
                    ProvinceId = arrayList.get(position).id;
                    CityBeanXAdapter adapter = new CityBeanXAdapter(list,context);
                    city_lv.setAdapter(adapter);
                }
            });
            return convertView;
        }
    }
    /**
     * 市适配器
     */
    public class CityBeanXAdapter extends BaseAdapter {
        private ArrayList<CityTestEntity.CityBeanX> arrayList;
        private Context context;

        public CityBeanXAdapter(ArrayList<CityTestEntity.CityBeanX> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }
        @Override
        public int getCount() {
            if (arrayList != null || arrayList.size() != 0) {
                return arrayList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
           ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_city, null);
                holder.tv_city = (TextView) convertView.findViewById(R.id.tv_city);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_city.setText(arrayList.get(position).name);
            holder.tv_city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<CityTestEntity.CityBeanX.CityBean> list = new ArrayList();
                    list = arrayList.get(position).city;
                    City = arrayList.get(position).name;
                    CityId = arrayList.get(position).id;
                    if(list!=null){
                        CityBeanAdapter adapter = new CityBeanAdapter(list,context);
                        city_lv.setAdapter(adapter);
                    }else {
                        Area = arrayList.get(position).name;
                        AreaId = arrayList.get(position).id;
                        Intent in = new Intent();
                        in.putExtra("Province", Province);
                        in.putExtra("City", City);
                        in.putExtra("Area", Area);
                        in.putExtra("AreaId", String.valueOf(AreaId));
                        setResult(RESULT_OK, in);
                        finish();
                    }
                }
            });
            return convertView;
        }

    }
    /**
     * 县适配器
     */
    public class CityBeanAdapter extends BaseAdapter {
        private ArrayList<CityTestEntity.CityBeanX.CityBean> arrayList;
        private Context context;

        public CityBeanAdapter(ArrayList<CityTestEntity.CityBeanX.CityBean> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @Override
        public int getCount() {
            if (arrayList != null || arrayList.size() != 0) {
                return arrayList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
           ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_city, null);
                holder.tv_city = (TextView) convertView.findViewById(R.id.tv_city);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_city.setText(arrayList.get(position).name);
            holder.tv_city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Area = arrayList.get(position).name;
                    AreaId = arrayList.get(position).id;
                    Intent in = new Intent();
                    in.putExtra("Province", Province);
                    in.putExtra("City", City);
                    in.putExtra("Area", Area);
                    in.putExtra("AreaId", String.valueOf(AreaId));
                    setResult(RESULT_OK, in);
                    finish();
                }
            });
            return convertView;
        }

    }

    private class ViewHolder {
        private TextView tv_city;
    }
}
