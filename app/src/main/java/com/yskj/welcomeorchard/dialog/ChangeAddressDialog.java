package com.yskj.welcomeorchard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.CityEntity;
import com.yskj.welcomeorchard.entity.DistrictEntity;
import com.yskj.welcomeorchard.entity.ProvenceEntity;
import com.yskj.welcomeorchard.widget.wheel.adapters.AbstractWheelTextAdapter;
import com.yskj.welcomeorchard.widget.wheel.views.OnWheelScrollListener;
import com.yskj.welcomeorchard.widget.wheel.views.WheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChangeAddressDialog extends Dialog implements View.OnClickListener {

    //省市区控件
    private WheelView wvProvince;
    private WheelView wvCitys;

    private TextView btnSure;//确定按钮
    private TextView btnCancle;//取消按钮

    private Context context;//上下文对象


    private List<ProvenceEntity> mArrProvinces;
    private Map<String, List<CityEntity>> mCitisDatasMap = new HashMap<>();

    private AddressTextAdapter provinceAdapter;
    private AddressTextCityAdapter cityAdapter;

    //选中的省市区信息
    private ProvenceEntity strProvince;
    private CityEntity strCity;
    private DistrictEntity strArea;

    //回调方法
    private OnAddressCListener onAddressCListener;

    //显示文字的字体大小
    private int maxsize = 18;
    private int minsize = 14;

    private ArrayList<DistrictEntity> districts;


    public ChangeAddressDialog(Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_myinfo_changeaddress);

        wvProvince = (WheelView) findViewById(R.id.wv_address_province);
        wvCitys = (WheelView) findViewById(R.id.wv_address_city);
        btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
        btnCancle = (TextView) findViewById(R.id.btn_myinfo_cancel);

        btnSure.setOnClickListener(this);
        btnCancle.setOnClickListener(this);

        wvProvince.setVisibleItems(5);

        wvCitys.setVisibleItems(5);

        wvProvince.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                updateCities();
            }
        });

        wvCitys.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                updateAreas();
            }
        });
    }


    public void setAddressData(List<ProvenceEntity> arrProvinces, Map<String, List<CityEntity>> citisDatasMap) {
        mArrProvinces = arrProvinces;
        mCitisDatasMap = citisDatasMap;

        strProvince = mArrProvinces.get(0);
        strCity = strProvince.getCitys().get(0);
        List<CityEntity> citys = mCitisDatasMap.get(strProvince.getId());
        provinceAdapter = new AddressTextAdapter(context, mArrProvinces, 0, maxsize, minsize);
        wvProvince.setViewAdapter(provinceAdapter);
        wvProvince.setCurrentItem(0);
        cityAdapter = new AddressTextCityAdapter(context, mCitisDatasMap.get(strProvince.getId()), 0, maxsize, minsize);
        wvCitys.setViewAdapter(cityAdapter);
        wvCitys.setCurrentItem(0);
    }


    //根据省来更新wheel的状态
    private void updateCities() {
        String currentText = (String) provinceAdapter.getItemText(wvProvince.getCurrentItem());
        strProvince = (ProvenceEntity) provinceAdapter.getItemObject(wvProvince.getCurrentItem());
        setTextviewSize(currentText, provinceAdapter);
        cityAdapter.update(mCitisDatasMap.get(strProvince.getId()));
        wvCitys.setViewAdapter(cityAdapter);
        wvCitys.setCurrentItem(0);
        updateAreas();
    }

    //根据城市来更新wheel的状态
    private void updateAreas()
    {
        String currentText = (String) cityAdapter.getItemText(wvCitys.getCurrentItem());
        strCity = (CityEntity) cityAdapter.getItemObject(wvCitys.getCurrentItem());
        setCityTextviewSize(currentText, cityAdapter);
    }


    ////////////////////////////////////////////////////华丽的分界线

    public void setTextviewSize(String curriteItemText, AddressTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(maxsize);
            } else {
                textvew.setTextSize(minsize);
            }
        }
    }

    public void setCityTextviewSize(String curriteItemText, AddressTextCityAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(maxsize);
            } else {
                textvew.setTextSize(minsize);
            }
        }
    }

    public void setAddresskListener(OnAddressCListener onAddressCListener) {
        this.onAddressCListener = onAddressCListener;
    }

    @Override
    public void onClick(View v) {
        if (v == btnSure) {
            if (onAddressCListener != null) {
                onAddressCListener.onClick(strProvince, strCity);
            }
        }
        if (v == btnCancle) {
            dismiss();
        }
        dismiss();
    }


    public interface OnAddressCListener {
        void onClick(ProvenceEntity province, CityEntity city);
    }

    private class AddressTextAdapter extends AbstractWheelTextAdapter {
        List<ProvenceEntity> list;

        protected AddressTextAdapter(Context context, List<ProvenceEntity> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        public void update(List<ProvenceEntity> list) {
            this.list = list;
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        @Override
        protected CharSequence getItemText(int index) {
            if (list != null && list.size() > 0) {
                return list.get(index).getName();
            }
            return "";
        }

        @Override
        protected Object getItemObject(int index) {
            if (list != null && list.size() > 0) {
                return list.get(index);
            }
            return null;
        }
    }

    private class AddressTextCityAdapter extends AbstractWheelTextAdapter {
        List<CityEntity> list;

        protected AddressTextCityAdapter(Context context, List<CityEntity> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        public void update(List<CityEntity> list) {
            this.list = list;
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        @Override
        protected CharSequence getItemText(int index) {
            if (list != null && list.size() > 0) {
                return list.get(index).getName();
            }
            return "";
        }

        @Override
        protected Object getItemObject(int index) {
            if (list != null && list.size() > 0) {
                return list.get(index);
            }
            return null;
        }
    }
}