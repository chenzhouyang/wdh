package com.yskj.welcomeorchard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.widget.wheel.adapters.AbstractWheelTextAdapter;
import com.yskj.welcomeorchard.widget.wheel.views.OnWheelScrollListener;
import com.yskj.welcomeorchard.widget.wheel.views.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建日期 2017/10/11on 11:50.
 * 描述：另类日期选择。。。。。只有年月滑轮形式
 * 作者：陈宙洋
 */

public class DateChooserDialog extends Dialog implements OnClickListener{

    private Context context;//上下文对象

    private TextView btnSure;//确定按钮
    private TextView btnCancle;//取消按钮

    //回调方法
    private OnAddressCListener onAddressCListener;

    //显示文字的字体大小
    private int maxsize = 18;
    private int minsize = 14;
    private String year = "",mount = "";
    private List<String> mYearList = new ArrayList<>();
    private List<String> mMountList = new ArrayList<>();
    private AddressTextAdapter yearAdapter,mountAdapter;
    private WheelView wheelViewYear,wheelViewMount;

    public DateChooserDialog(@NonNull Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_date_chooser);
        initView();
    }

    private void initView() {
        wheelViewYear = (WheelView) findViewById(R.id.wheel_view_year);
        btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
        btnCancle = (TextView) findViewById(R.id.btn_myinfo_cancel);
        wheelViewMount = (WheelView) findViewById(R.id.wheel_view_mount);
        btnSure.setOnClickListener(this);
        btnCancle.setOnClickListener(this);

        wheelViewYear.setVisibleItems(5);
        wheelViewMount.setVisibleItems(5);
        wheelViewYear.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                updateSize();
            }
        });
        wheelViewMount.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                updateSize();
            }
        });
    }

    //更新wheel的状态选中状态字体大小
    private void updateSize(){
        String currentText = (String) yearAdapter.getItemText(wheelViewYear.getCurrentItem());
        String mountText = (String) mountAdapter.getItemText(wheelViewMount.getCurrentItem());
        year = (String) yearAdapter.getItemObject(wheelViewYear.getCurrentItem());
        mount = (String) mountAdapter.getItemObject(wheelViewMount.getCurrentItem());
        setTextviewSize(currentText, yearAdapter);
        setTextviewSize(mountText, mountAdapter);
    }
    @Override
    public void onClick(View v) {
        if (v == btnSure) {
            if (onAddressCListener != null) {
                onAddressCListener.onClick(year,mount);
            }
        }
        if (v == btnCancle) {
            dismiss();
        }
        dismiss();
    }

    /**
     * 初始化显示
     * @param yearList
     * @param mountList
     */
    public void setAddressData(List<String> yearList,List<String> mountList) {
        mYearList = yearList;
        mMountList = mountList;
        year = mYearList.get(yearList.size()-1);
        mount = mMountList.get(0);
        yearAdapter = new AddressTextAdapter(context, mYearList, 0, maxsize, minsize);
        wheelViewYear.setViewAdapter(yearAdapter);
        wheelViewYear.setCurrentItem(yearList.size()-1);
        mountAdapter = new AddressTextAdapter(context, mMountList, 0, maxsize, minsize);
        wheelViewMount.setViewAdapter(mountAdapter);
        wheelViewMount.setCurrentItem(0);
    }



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

    public void setAddresskListener(OnAddressCListener onAddressCListener) {
        this.onAddressCListener = onAddressCListener;
    }

    public interface OnAddressCListener {
        void onClick(String year,String mount);
    }

    private class AddressTextAdapter extends AbstractWheelTextAdapter {
        List<String> list;

        protected AddressTextAdapter(Context context, List<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        public void update(List<String> list) {
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
                return list.get(index);
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
