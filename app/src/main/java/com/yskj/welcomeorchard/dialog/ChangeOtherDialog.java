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
 * 创建日期 2017/5/4on 14:58.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class ChangeOtherDialog extends Dialog implements OnClickListener{

    private Context context;//上下文对象

    private TextView btnSure;//确定按钮
    private TextView btnCancle;//取消按钮

    //回调方法
    private OnAddressCListener onAddressCListener;

    //显示文字的字体大小
    private int maxsize = 18;
    private int minsize = 14;
    private String data = "";
    private List<String> mDataList = new ArrayList<>();
    private AddressTextAdapter dataAdapter;
    private WheelView wheelView;

    public ChangeOtherDialog(@NonNull Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_other_change);
        initView();
    }

    private void initView() {
        wheelView = (WheelView) findViewById(R.id.wheel_view);
        btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
        btnCancle = (TextView) findViewById(R.id.btn_myinfo_cancel);

        btnSure.setOnClickListener(this);
        btnCancle.setOnClickListener(this);

        wheelView.setVisibleItems(5);

        wheelView.addScrollingListener(new OnWheelScrollListener() {
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
    private void updateSize()
    {
        String currentText = (String) dataAdapter.getItemText(wheelView.getCurrentItem());
        data = (String) dataAdapter.getItemObject(wheelView.getCurrentItem());
        setTextviewSize(currentText, dataAdapter);
    }
    @Override
    public void onClick(View v) {
        if (v == btnSure) {
            if (onAddressCListener != null) {
                onAddressCListener.onClick(data);
            }
        }
        if (v == btnCancle) {
            dismiss();
        }
        dismiss();
    }
    public void setAddressData(List<String> dataList) {
        mDataList = dataList;

        data = mDataList.get(0);
        dataAdapter = new AddressTextAdapter(context, mDataList, 0, maxsize, minsize);
        wheelView.setViewAdapter(dataAdapter);
        wheelView.setCurrentItem(0);
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

    public void setAddresskListener(OnAddressCListener onAddressCListener) {
        this.onAddressCListener = onAddressCListener;
    }

    public interface OnAddressCListener {
        void onClick(String data);
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
