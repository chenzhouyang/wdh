package com.yskj.welcomeorchard.receiver;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class UpdateInfoService {
    UpdateDialog updateDialog;
    Context context;
    private String downLoadUrl;

    public UpdateInfoService(Context context) {
        this.context = context;
    }
    public void downLoadFile(String url) {
        downLoadUrl = url;
        updateDialog = new UpdateDialog(context,downLoadUrl);
        updateDialog.setCanceledOnTouchOutside(false);
        updateDialog.setCancelable(false);
        updateDialog.show();
    }

    public class UpdateDialog extends Dialog {
        private Context context;
        private ProgressBar progressView;
        private String url;

        private TextView tvProgress;
        private float percent = 0;
        private static final int DIALOGUI = 1000;

        private Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case DIALOGUI:
                        updateDialog.progressView.setProgress((int) percent);
                        tvProgress.setText((int)percent+"/100");
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public UpdateDialog(Context context,String url) {
            super(context, R.style.ShareDialog);
            this.context = context;
            this.url = url;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_update);
            initView();
        }

        private void initView() {
            progressView = (ProgressBar) findViewById(R.id.progressView);
            tvProgress = (TextView) findViewById(R.id.tv_progress);
            progressView.setProgress(0);
            new Thread(new DownloadFileThread()).start();
        }

        private class DownloadFileThread implements Runnable {
            @Override
            public void run() {
                try {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/wdh.apk");
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept-Encoding", "identity");
                    long sum = 0;
                    if (file.exists()) {
                        file.delete();
                        /*sum = file.length();
                        // 设置断点续传的开始位置
                        connection.setRequestProperty("Range", "bytes=" + file.length() + "-");*/
                    }
                    int code = connection.getResponseCode();
                    if (code == 200 || code == 206) {
                        int contentLength = connection.getContentLength();
                        Logger.e("contentLength = " + contentLength);
                        contentLength += sum;
                        InputStream is = connection.getInputStream();
                /*
                *
                * 创建一个向具有指定 name 的文件中写入数据的输出文件流。
                * true表示当文件在下载过程中出现中断，
                * 当再次链接网络时，将会从断点处追加。
                *
                * */
                        FileOutputStream fos = new FileOutputStream(file, true);

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, length);
                            sum += length;
                            percent = sum * 100.0f / contentLength;
                            /**
                             * 实现进度条
                             */
                            handler.sendEmptyMessage(DIALOGUI);
                        }
                        updateDialog.dismiss();
                        update();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        void update() {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "wdh.apk")), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
