package com.yskj.welcomeorchard.zxing.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.DectionAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.DectionEntity;
import com.yskj.welcomeorchard.entity.DistriOrderEntity;
import com.yskj.welcomeorchard.entity.MoneyInfoEntity;
import com.yskj.welcomeorchard.entity.OrderPayEntity;
import com.yskj.welcomeorchard.entity.ScanCodeTmpEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.payment.zfb.PayResult;
import com.yskj.welcomeorchard.register.RegisterActivity;
import com.yskj.welcomeorchard.ui.qrcode.QrCodeActivity;
import com.yskj.welcomeorchard.utils.DoubleUtils;
import com.yskj.welcomeorchard.utils.Extension;
import com.yskj.welcomeorchard.utils.GridPasswordView;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.StringUtils;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.yskj.welcomeorchard.widget.SimplexToast;
import com.yskj.welcomeorchard.zxing.camera.CameraManager;
import com.yskj.welcomeorchard.zxing.decoding.CaptureActivityHandler;
import com.yskj.welcomeorchard.zxing.decoding.InactivityTimer;
import com.yskj.welcomeorchard.zxing.view.ViewfinderView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.Request;

public class CaptureActivity extends BaseActivity implements Callback {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ImageView cancelScanButton;
    private String  pay_trade_no, payCode;
    private LoadingCaches aCache = LoadingCaches.getInstance();
    private Intent intent;
     private double copewith = 0.00;
    private int pay_type = 1;
    private static final int SDK_PAY_FLAG = 1;
     private double amount;

    private int type = 0;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                            orderpay();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            showToast("支付结果确认中");
                        } else if (TextUtils.equals(resultStatus, "4000")) {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            showToast("您还未安装支付宝客户端");
                        } else {
                            showToast("支付失败");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    private ArrayList<DectionEntity> dectionlist;
    private DectionEntity dectionEntity;
    private String orderid;
    private MoneyInfoEntity moneyInfoEntity;
    private String url;
    private String payordertype;
    private String orderType = "5";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        cancelScanButton = (ImageView) this.findViewById(R.id.btn_cancel_scan);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        intent = getIntent();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getmoenyinfor();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
        cancelScanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
    }
    /**
     * 请求资金
     */
    private void getmoenyinfor(){
        OkHttpUtils.get().url(Urls.MONEYINFO).addHeader("Authorization", aCache.get("access_token"))
                .build().execute(new StringCallback() {
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
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                moneyInfoEntity = new Gson().fromJson(response,new TypeToken<MoneyInfoEntity>(){}.getType());
                if(moneyInfoEntity.code == 0){
                    amount = moneyInfoEntity.data.fundAccount;
                }

            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();

    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        boolean paycode = resultString.contains("payCode#");//付款
        boolean bs = resultString.contains("Code#");
        boolean dection = resultString.contains("$");
        boolean directsupply = resultString.contains("Code@#");
        boolean distriqecode = resultString.contains("Sales");
        boolean Qrcode = resultString.contains("&QrCodeSHARE#");

        if(aCache==null||aCache.get("access_token").equals("null")&&!Qrcode){
            new AlertDialog.Builder(CaptureActivity.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("您还没有登陆，是否登陆？")//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            startActivity(new Intent(context, LoginActivity.class));
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                public void onClick(DialogInterface dialog, int which) {//响应事件
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);

                            SurfaceHolder surfaceHolder = surfaceView.getHolder();

                            initCamera(surfaceHolder);

                            if (handler != null)

                                handler.restartPreviewAndDecode();
                        }

                    }, 3000);
                    dialog.dismiss();
                }
            }).setCancelable(false)
                    .show();//在按键响应事件中显示此对话框
            return;
        }
        //FIXME
        if (resultString.equals("")) {
            Toast.makeText(CaptureActivity.this, "扫描失败", Toast.LENGTH_SHORT).show();
        } else if(bs&&!paycode&&!dection&&!directsupply&&!distriqecode&&!Qrcode) {
            //扫描付款
            orderType = "5";
            url = Urls.CODEPAYMENT;
            String[] receivables = resultString.split("Code#");
            payCode = receivables[1];
            aCache.put("paycode", payCode);
            payordertype = "tid";
            GatheringDialog dialog = new GatheringDialog(context);
            dialog.show();
        }else if(bs&&!paycode&&dection&&!directsupply&&!distriqecode&&!Qrcode){
            //仪器检测
            orderType = "6";
            url = Urls.IESCPF;
            String[] receivables = resultString.split("Code#");
            payCode = receivables[1];
            payordertype = "orderId";
            DetectionDialog dialog = new DetectionDialog(context, payCode);
            dialog.show();
        }else if(!bs&&!paycode&&!dection&&directsupply&&!distriqecode&&!Qrcode){
            //收银付款二维码
            orderType = "7";
            url =Urls.GOODORDERPAY;
            String[] receivables = resultString.split("#");
             orderid = receivables[1];
            String amounts = receivables[2];
            payordertype = "orderId";
            if(Double.parseDouble(amounts)>amount){
                CaptureDialog  dialog = new CaptureDialog(context,"",Double.parseDouble(amounts));
                dialog.show();
            }else {
                SurePwdDialog dialog = new SurePwdDialog(context,"0",Double.parseDouble(amounts));
                dialog.show();
            }

        }else if(!bs&&!paycode&&!dection&&!directsupply&&distriqecode&&!Qrcode){
            //供应商
            orderType = "8";
            url = Urls.MGORDERPAY;
            String[] receivables = resultString.split("#");
            String ugsid = receivables[1];
            String num = receivables[2];
            payordertype = "orderId";
            getorder(ugsid,num);
        }else if(!bs&&!paycode&&!dection&&!directsupply&&!distriqecode&&Qrcode){
            String[] receivables = resultString.split("&QrCodeSHARE#");
            String mobile = receivables[1];
            //跳转到注册界面
            startActivity(new Intent(context, RegisterActivity.class).putExtra("type","1").putExtra("mobile",mobile));
        }else {
            new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                    .setMessage("无效的二维码")//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                public void onClick(DialogInterface dialog, int which) {//响应事件
                }
            }).show();//在按键响应事件中显示此对话框
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);

                SurfaceHolder surfaceHolder = surfaceView.getHolder();

                initCamera(surfaceHolder);

                if (handler != null)

                    handler.restartPreviewAndDecode();
            }

        }, 3000);
    }
    /**
     * 分销商创建临时订单
     */
        private void getorder(String ugsid,String num){
            OkHttpUtils.post().url(Urls.DISTRI)
                    .addHeader("Authorization", aCache.get("access_token"))
                    .addParams("ugsId",ugsid)
                    .addParams("num",num)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    isLogin(e);
                }

                @Override
                public void onResponse(String response, int id) {
                    DistriOrderEntity distriOrderEntity = new Gson().fromJson(response,DistriOrderEntity.class);
                    if(distriOrderEntity.code == 0){
                        orderid = distriOrderEntity.data.orderId+"";
                        if(distriOrderEntity.data.totalAmount>amount){
                            CaptureDialog  dialog = new CaptureDialog(context,"",distriOrderEntity.data.totalAmount);
                            dialog.show();
                        }else {
                            SurePwdDialog dialog = new SurePwdDialog(context,"0",distriOrderEntity.data.totalAmount);
                            dialog.show();
                        }
                    }else {
                        showToast(MessgeUtil.geterr_code(distriOrderEntity.code));
                    }
                }
            });

        }
    /**
     * 输入支付金额
     */
    public class GatheringDialog extends Dialog implements View.OnClickListener {
        private Button btnCancle;
        private Button btnSure;
        private Context context;
        private EditText message;

        public GatheringDialog(Context context) {
            super(context, R.style.ShareDialog);
            this.context = context;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_gathering);

            initView();
        }

        private void initView() {
            btnCancle = (Button) findViewById(R.id.btn_cancle);
            btnSure = (Button) findViewById(R.id.btn_sure);
            message = (EditText) findViewById(R.id.message);
            message.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                            s = s.toString().subSequence(0,
                                    s.toString().indexOf(".") + 3);
                            message.setText(s);
                            message.setSelection(s.length());
                        }
                    }
                    if (s.toString().trim().substring(0).equals(".")) {
                        s = "0" + s;
                        message.setText(s);
                        message.setSelection(2);
                    }

                    if (s.toString().startsWith("0")
                            && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            message.setText(s.subSequence(0, 1));
                            message.setSelection(1);
                            return;
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            btnSure.setOnClickListener(this);
            btnCancle.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sure:
                    if(message.length() == 0){
                        SimplexToast.show(context,"请填写付款金额");
                        return;
                    }else {
                        copewith= Double.parseDouble(message.getText().toString());
                        if(orderid!=null){
                            if(amount >= copewith){
                                SurePwdDialog dialog = new SurePwdDialog(context,payCode,copewith);
                                dialog.show();
                            }else {
                                CaptureDialog dialog = new CaptureDialog(context,payCode,copewith);
                                dialog.show();
                            }
                        }else {
                            SetUp(copewith);
                        }

                    }
                    dismiss();
                    break;
                case R.id.btn_cancle:
                    dismiss();
                    break;
            }
        }
    }

    /**
     * 检测选取支付项目类型
     *
     */
    public class DetectionDialog extends Dialog {
        private ListView detction_lv;
        private Context context;
        private String code;
        private ImageView close_dialog;
        public DetectionDialog(Context context,String code) {
            super(context, R.style.ShareDialog);
            this.context = context;
            this.code = code;

        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_detction);
            close_dialog = (ImageView) findViewById(R.id.close_dialog);
            close_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            initHeadGridView();
        }
        private void initHeadGridView() {
           OkHttpUtils.get().url(Urls.DECTION) .addHeader("Authorization", aCache.get("access_token"))
                   .addParams("code",code).build().execute(new StringCallback() {
               @Override
               public void onError(Call call, Exception e, int id) {
                   isLogin(e);
               }

               @Override
               public void onResponse(String response, int id) {
                    dectionEntity = new Gson().fromJson(response,new TypeToken<DectionEntity>(){}.getType());
                   if(dectionEntity.code == 0){
                       detction_lv = (ListView) findViewById(R.id.detction_lv);
                       DectionAdapter adapter = new DectionAdapter(context,dectionEntity.data.list);
                       detction_lv.setSelector(new ColorDrawable(Color.TRANSPARENT));
                       detction_lv.setAdapter(adapter);
                       detction_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                           @Override
                           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                               copewith = dectionEntity.data.list.get(position).price;
                               if(orderid!=null){
                                   if(amount >= copewith){
                                       SurePwdDialog dialog = new SurePwdDialog(context,payCode,copewith);
                                       dialog.show();
                                   }else {
                                       CaptureDialog dialog = new CaptureDialog(context,payCode,copewith);
                                       dialog.show();
                                   }
                               }else {
                                   SetUpDection((long) dectionEntity.data.list.get(position).id,copewith);
                               }
                               dismiss();
                           }
                       });
                   }

               }
           });

        }
    }
    //选取支付
    public class CaptureDialog extends Dialog implements View.OnClickListener {
        private TextView btnCancle,excrete_true,detail;
        private TextView btnSure,tv_balance;
        private Context context;
        private CaptureAdapter adapter;
        private int[] imgList;
        private String[] payType;
        private int selectedPosition = 0;
        private NoScrollListView listView;
    private String paycode;
    private double anmount;

        public CaptureDialog(Context context,String paycode,double anmount) {
            super(context, R.style.ShareDialog);
            this.context = context;
            this.paycode = paycode;
            this.anmount = anmount;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_capture_all);
            initView();
        }
        private void initView() {
            copewith = 0.00;
            btnCancle = (TextView) findViewById(R.id.btn_cancle);
            btnSure = (TextView) findViewById(R.id.btn_sure);
            listView = (NoScrollListView) findViewById(R.id.listView);
            tv_balance = (TextView) findViewById(R.id.tv_balance);
            detail = (TextView) findViewById(R.id.detail);
            detail.setText(StringUtils.getStringtodouble(anmount));
            excrete_true = (TextView) findViewById(R.id.excrete_true);
            if (amount >= anmount) {
                copewith = anmount;
                imgList = new int[]{R.mipmap.pay_mark_money};
                payType = new String[]{"余额支付"};
                tv_balance.setText(StringUtils.getStringtodouble(copewith));
                excrete_true.setText(StringUtils.getStringtodouble(0.00));
            } else {
                copewith = DoubleUtils.sub(anmount,amount);
                imgList = new int[]{R.mipmap.zhifu_pay_ico, R.mipmap.wei_pay_ico};
                payType = new String[]{"支付宝支付", "微信支付"};
                tv_balance.setText(StringUtils.getStringtodouble(amount));
                excrete_true.setText(StringUtils.getStringtodouble(copewith));
            }
            final ArrayList<OrderPayEntity> arrayList = new ArrayList<>();
            for (int i = 0; i < imgList.length; i++) {
                OrderPayEntity orderPayEntity = new OrderPayEntity();
                orderPayEntity.setChacked(false);
                orderPayEntity.setImgId(imgList[i]);
                orderPayEntity.setPayType(payType[i]);
                arrayList.add(orderPayEntity);
            }

            adapter = new CaptureAdapter(context, arrayList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedPosition = position;
                    pay_type = position + 1;
                    adapter.notifyDataSetChanged();
                }
            });
            btnSure.setOnClickListener(this);
            btnCancle.setOnClickListener(this);
        }

        public class CaptureAdapter extends BaseAdapter {
            private Context context;
            private ArrayList<OrderPayEntity> arrayList;
            private Map<Integer, Boolean> selectedMap;//保存checkbox是否被选中的状态

            public CaptureAdapter(Context context, ArrayList<OrderPayEntity> arrayList) {
                this.context = context;
                this.arrayList = arrayList;
                selectedMap = new HashMap<Integer, Boolean>();
                initData();
            }

            public void initData() {

                for (int i = 0; i < arrayList.size(); i++) {
                    selectedMap.put(i, false);
                }
            }

            @Override
            public int getCount() {
                if (arrayList == null || arrayList.size() == 0)
                    return 0;
                return arrayList.size();
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
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_order_pay, null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                OrderPayEntity orderPayEntity = arrayList.get(position);
                holder.imageView.setImageResource(orderPayEntity.imgId);
                holder.textView.setText(orderPayEntity.payType);
                if (selectedPosition == 0) {
                    if (position == 0) {
                        holder.img_choose_icon.setImageResource(R.mipmap.fzg);
                    } else {
                        holder.img_choose_icon.setImageResource(R.mipmap.fzf);
                    }
                } else {
                    if (selectedPosition == position) {
                        holder.img_choose_icon.setImageResource(R.mipmap.fzg);
                    } else {
                        holder.img_choose_icon.setImageResource(R.mipmap.fzf);
                    }
                }
                return convertView;
            }

            private class ViewHolder {
                ImageView imageView, img_choose_icon;
                TextView textView;

                public ViewHolder(View itemView) {
                    imageView = (ImageView) itemView.findViewById(R.id.img_type);
                    textView = (TextView) itemView.findViewById(R.id.tv_type);
                    img_choose_icon = (ImageView) itemView.findViewById(R.id.img_choose_icon);
                }
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sure:
                    getexcrete(copewith);
                    dismiss();
                    break;
                case R.id.btn_cancle:
                    dismiss();
                    break;
            }
        }
    }

    /**
     * 扫码支付生成临时订单
     */
    private void SetUp(final double copewith){
        OkHttpUtils.post().url(Urls.SCANCODETMP)
                .addHeader("Authorization", aCache.get("access_token"))
                .addParams("code",payCode)
                .addParams("payAmount",DoubleUtils.toDecimalString2(copewith))
                .build().execute(new StringCallback() {
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
                Map<String,Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if(code == 0){
                    ScanCodeTmpEntity setUpEntity = new Gson().fromJson(response,new TypeToken<ScanCodeTmpEntity>(){}.getType());
                    orderid = setUpEntity.data.orderId+"";
                    aCache.put("tid",orderid);
                    if(amount >= copewith){
                        SurePwdDialog dialog = new SurePwdDialog(context,payCode,copewith);
                        dialog.show();
                    }else {
                        CaptureDialog dialog = new CaptureDialog(context,payCode,copewith);
                        dialog.show();
                    }

                }
            }
        });
    }

    /**
     * 检测生成临时订单
     * @param copewith
     */
    private void SetUpDection( Long lsid, final double copewith){
        OkHttpUtils.post().url(Urls.SCANCODETMPBILL)
                .addHeader("Authorization", aCache.get("access_token"))
                .addParams("code",payCode)
                .addParams("lsid",lsid+"")
                .build().execute(new StringCallback() {
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
                Map<String,Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if(code == 0){
                    ScanCodeTmpEntity setUpEntity = new Gson().fromJson(response,new TypeToken<ScanCodeTmpEntity>(){}.getType());
                    orderid = setUpEntity.data.orderId+"";
                    aCache.put("tid",orderid);
                    if(amount >= copewith){
                        SurePwdDialog dialog = new SurePwdDialog(context,payCode,copewith);
                        dialog.show();
                    }else {
                        CaptureDialog dialog = new CaptureDialog(context,payCode,copewith);
                        dialog.show();
                    }

                }
            }
        });
    }
    //调取支付
    private void getexcrete(final double copewith) {
        OkHttpUtils.post().url(Urls.PAY)
                .addHeader("Authorization", aCache.get("access_token"))
                .addParams("price", DoubleUtils.toDecimalString2(copewith))
                .addParams("order_type",orderType)
                .addParams("order_id",orderid)
                .addParams("pay_type", pay_type + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if (code == 0) {
                    JSONObject payinfor = (JSONObject) map.get("data");
                    final String PY_info;
                    try {
                        pay_trade_no = payinfor.getString("pay_trade_no");
                        if (pay_type == 1) {
                            PY_info = (String) payinfor.get("pay_info");

                            Runnable payRunnable = new Runnable() {
                                public void run() {
                                    // 构造PayTask 对象
                                    PayTask aliPay = new PayTask((Activity) context);
                                    // 调用支付接口，获取支付结果
                                    String result = aliPay.pay(
                                            PY_info, true);
                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                }
                            };
                            // 必须异步调用
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
                        } else if (pay_type == 2) {
                            JSONObject params = payinfor.getJSONObject("pay_info");
                            if (Extension.isWeixinAvilible(context)) {
                                aCache.put("pay_trade_no", pay_trade_no);
                                if(type == 3){
                                    aCache.put("ordertype","orderpay");
                                    aCache.put("orderid",orderid);
                                }else if(type == 2){
                                    aCache.put("ordertype","iescpfinish");
                                    aCache.put("orderid",orderid);
                                }else {
                                    aCache.put("ordertype","3");
                                }

                                aCache.put("amount",DoubleUtils.toDecimalString2(copewith));
                                String appid = params.getString("appid");
                                IWXAPI api = WXAPIFactory.createWXAPI(context, appid, false);
                                api.registerApp(appid);
                                PayReq req = new PayReq();
                                req.appId = params.getString("appid");
                                req.partnerId = params.getString("partnerid");
                                req.prepayId = params.getString("prepayid");
                                req.packageValue = "Sign=WXPay";
                                req.nonceStr = params.getString("noncestr");
                                req.timeStamp = params.getString("timestamp");
                                req.sign = params.getString("sign");
                                api.sendReq(req);
                            } else {
                                Toast.makeText(context, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    showToast(MessgeUtil.geterr_code(code));
                }


            }
        });
    }

    //用通贝支付
    public class SurePwdDialog extends Dialog {
        private ImageView img_dismiss;
        private TextView tv_forget_pwd;
        private GridPasswordView pwdView;
        private String code;
        private double copewith;

        public SurePwdDialog(Context context,String code,double copewith) {
            super(context, R.style.GiftDialog);
            this.code = code;
            this.copewith = copewith;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_suregift_pwd);
            initView();
        }

        private void initView() {
            img_dismiss = (ImageView) findViewById(R.id.img_dismiss);
            tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
            pwdView = (GridPasswordView) findViewById(R.id.pwd);
            //dialog弹出时弹出软键盘
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            pwdView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
                @Override
                public void onChanged(String psw) {
                }
                @Override
                public void onMaxLength(String psw) {
                    dismissJP();
                    dismiss();
                    //调取java接口完成订单
                    OkHttpUtils.post().url(url).addHeader("Authorization", aCache.get("access_token"))
                            .addParams(payordertype,orderid)
                            .addParams("payPassword",pwdView.getPassWord())
                            .build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Map<String,Object> map = JSONFormat.jsonToMap(response);
                            int code = (int) map.get("code");
                            if(code == 0){
                                showToast("支付成功");
                            }else {
                                showToast(MessgeUtil.geterr_code(code));
                            }
                            AppManager.getInstance().killActivity(CaptureActivity.class);
                        }
                    });
                }
            });
            img_dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            tv_forget_pwd.setText(StringUtils.getStringtodouble(copewith));
            Window dialogWindow = getWindow();
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (display.getWidth()); //设置宽度
            dialogWindow.setAttributes(lp);
            dialogWindow.setGravity(Gravity.BOTTOM);
        }
    }
//订单支付
    public void orderpay(){
        OkHttpUtils.post().url(url).addHeader("Authorization", aCache.get("access_token"))
                .addParams(payordertype,orderid)
                .addParams("rechargeOrderNo",pay_trade_no)
                .build().execute(new StringCallback() {
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
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Map<String,Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if(code == 0){
                    showToast("支付成功");
                }else {
                    showToast(MessgeUtil.geterr_code(code));
                }
                AppManager.getInstance().killActivity(CaptureActivity.class);
            }
        });
    }
    //软键盘消失
    public void dismissJP() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) CaptureActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }
    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

}