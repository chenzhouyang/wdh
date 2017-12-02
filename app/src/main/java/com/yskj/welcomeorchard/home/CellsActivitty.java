package com.yskj.welcomeorchard.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.entity.CellsEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.widget.NoScrollGridView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by YSKJ-02 on 2017/3/20.
 */

public class CellsActivitty extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.cells_image)
    ImageView cellsImage;
    @Bind(R.id.cell_gridview)
    NoScrollGridView cellGridview;
    @Bind(R.id.cells_date)
    WebView cellsDate;
    private CellsEntity cellsEntity;
    private LoadingCaches caches = LoadingCaches.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cells);
        ButterKnife.bind(this);
        txtTitle.setText("详情");
        txtTitle.setPadding(20,0,0,0);
        imageRight.setVisibility(View.VISIBLE);
        imageRight.setBackgroundResource(R.color.white);
        imageRight.setImageResource(R.mipmap.lift_order);
        indate();
    }
private void indate(){
    OkHttpUtils.get().url(getIntent().getStringExtra("id")).build().execute(new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(String response, int id) {
             cellsEntity = new Gson().fromJson(response,new TypeToken<CellsEntity>(){}.getType());
            if(cellsEntity.errorCode.equals("000")){
                GlideImage.loadImage(context,cellsImage,cellsEntity.goodsBean.originalImg,R.mipmap.img_error);
                cellsDate.loadUrl(Ips.PHPURL+"/App/Goods/goodsDetail/id/"+cellsEntity.goodsBean.goodsId);
                CellsactivityAdapter adapter = new CellsactivityAdapter(context,cellsEntity.getPrcklistX());
                cellGridview.setAdapter(adapter);
            }
        }
    });

}
    public class CellsactivityAdapter extends BaseAdapter {
        private Context context;
        private List<CellsEntity.PrckBean> list;
        public CellsactivityAdapter(Context context,List<CellsEntity.PrckBean> list){
            super();
            this.context = context;
            this.list = list;
        }
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_cells,null);
                holder.item_cell_tv = (TextView) convertView.findViewById(R.id.item_cell_tv);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.item_cell_tv.setText(list.get(position).item);
            holder.item_cell_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,IntroActivity.class)
                            .putExtra("key_name",cellsEntity.specgoodsBeanMap.get(list.get(position).itemId).keyName)
                            .putExtra("price", cellsEntity.specgoodsBeanMap.get(list.get(position).itemId).price+"")
                            .putExtra("description",cellsEntity.specgoodsBeanMap.get(list.get(position).itemId).description)
                            .putExtra("goods_id",cellsEntity.goodsBean.goodsId+"")
                            .putExtra("key",cellsEntity.specgoodsBeanMap.get(list.get(position).itemId).key+"")
                            .putExtra("discern","0"));
                }
            });
            return convertView;
        }
        public class ViewHolder{
            TextView item_cell_tv;
        }
    }

    @OnClick({R.id.img_back,R.id.image_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.image_right:
                if(caches.get("access_token").equals("null")){
                    startActivity(new Intent(context, LoginActivity.class));
                }else {
                    startActivity(new Intent(context,IntroOrderActivity.class));
                }

                break;
        }
    }
}
