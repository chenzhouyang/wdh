package com.yskj.welcomeorchard.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;


/**
 * 为子类添加主题，并添加返回键
 * 所有需要返回键并且要通知栏主题的类都需继承此类，以便于管理
 * */
public class BackBaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
