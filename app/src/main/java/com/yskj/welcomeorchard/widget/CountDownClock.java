package com.yskj.welcomeorchard.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.utils.Util;

/**
 * 倒计时控件
 * 
 * @author Administrator
 *
 */
public class CountDownClock extends LinearLayout implements Runnable{

	private static final int WHITE = 0Xffffffff;
	private static final int GRAY = 0Xff666666;

	private TextView mDayTxt1,mDayTxt2;
	private TextView mHourTxt1;
	private TextView mHourTxt2;
	private TextView mMinuteTxt1;
	private TextView mMinuteTxt2;
	private TextView mSecondTxt1;
	private TextView mSecondTxt2;

	private TextView mTxtSem1;
	private TextView mTxtSem2;
	private TextView mTxtSem3;
	private TextView mTxtSem4;
	private long mTotalSeconds;
	private CountDownListener mListener;

	private static final int RUN = 1001;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case RUN:
					run();
					break;
			}
		}
	};

	public CountDownClock(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context, null);
	}

	public CountDownClock(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context, attrs);
	}

	public CountDownClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		// setOrientation(HORIZONTAL);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = Util.dip2px(context, 2);
		mDayTxt1 = new TextView(context);
		mDayTxt2 = new TextView(context);
		mHourTxt1 = new TextView(context);
		mHourTxt2 = new TextView(context);
		mMinuteTxt1 = new TextView(context);
		mMinuteTxt2 = new TextView(context);
		mSecondTxt1 = new TextView(context);
		mSecondTxt2 = new TextView(context);
		mTxtSem1 = new TextView(context);
		mTxtSem2 = new TextView(context);
		mTxtSem3 = new TextView(context);
		mTxtSem4 = new TextView(context);

		mTxtSem1.getPaint().setFakeBoldText(true);
		mTxtSem2.getPaint().setFakeBoldText(true);
		mTxtSem3.getPaint().setFakeBoldText(true);
		mTxtSem4.getPaint().setFakeBoldText(true);

		mTxtSem1.setText("天");
		mTxtSem2.setText("时");
		mTxtSem3.setText("分");
		mTxtSem4.setText("秒");

//		mDayTxt.setTextColor(RED);
//		mDayTxt.setTextSize(12);
//		mDayTxt.setPadding(0, 0, Util.dip2px(context, 5), 0);
		mDayTxt1.setTextColor(WHITE);
		mDayTxt2.setTextColor(WHITE);
		mHourTxt1.setTextColor(WHITE);
		mHourTxt2.setTextColor(WHITE);
		mMinuteTxt1.setTextColor(WHITE);
		mMinuteTxt2.setTextColor(WHITE);
		mSecondTxt1.setTextColor(WHITE);
		mSecondTxt2.setTextColor(WHITE);
		mTxtSem1.setTextColor(GRAY);
		mTxtSem2.setTextColor(GRAY);
		mTxtSem3.setTextColor(GRAY);
		mTxtSem4.setTextColor(GRAY);

		mDayTxt1.setTextSize(8);
		mDayTxt2.setTextSize(8);
		mHourTxt1.setTextSize(8);
		mHourTxt2.setTextSize(8);
		mMinuteTxt1.setTextSize(8);
		mMinuteTxt2.setTextSize(8);
		mSecondTxt1.setTextSize(8);
		mSecondTxt2.setTextSize(8);
		mTxtSem1.setTextSize(9);
		mTxtSem2.setTextSize(9);
		mTxtSem3.setTextSize(9);
		mTxtSem4.setTextSize(9);


		int txtPadding = Util.dip2px(context, 2);
		mDayTxt1.setPadding(txtPadding, 0, txtPadding, 0);
		mDayTxt2.setPadding(txtPadding, 0, txtPadding, 0);
		mHourTxt1.setPadding(txtPadding, 0, txtPadding, 0);
		mHourTxt2.setPadding(txtPadding, 0, txtPadding, 0);
		mMinuteTxt1.setPadding(txtPadding, 0, txtPadding, 0);
		mMinuteTxt2.setPadding(txtPadding, 0, txtPadding, 0);
		mSecondTxt1.setPadding(txtPadding, 0, txtPadding, 0);
		mSecondTxt2.setPadding(txtPadding, 0, txtPadding, 0);

		mDayTxt1.setBackgroundResource(R.drawable.countdown_txt_bg);
		mDayTxt2.setBackgroundResource(R.drawable.countdown_txt_bg);
		mHourTxt1.setBackgroundResource(R.drawable.countdown_txt_bg);
		mHourTxt2.setBackgroundResource(R.drawable.countdown_txt_bg);
		mMinuteTxt1.setBackgroundResource(R.drawable.countdown_txt_bg);
		mMinuteTxt2.setBackgroundResource(R.drawable.countdown_txt_bg);
		mSecondTxt1.setBackgroundResource(R.drawable.countdown_txt_bg);
		mSecondTxt2.setBackgroundResource(R.drawable.countdown_txt_bg);

		addView(mDayTxt1, lp);
		addView(mDayTxt2, lp);
		addView(mTxtSem1, lp);
		addView(mHourTxt1, lp);
		addView(mHourTxt2, lp);
		addView(mTxtSem2, lp);
		addView(mMinuteTxt1, lp);
		addView(mMinuteTxt2, lp);
		addView(mTxtSem3, lp);
		addView(mSecondTxt1, lp);
		addView(mSecondTxt2, lp);
		addView(mTxtSem4, lp);
	}

	long mday;
	long mhour;
	private long mmin;// 天，小时，分钟，秒
	long msecond;
	private boolean run = false; // 是否启动了

	public void setTimes(long seconds) {
		mTotalSeconds=seconds;
		msecond = seconds % 60;
		mday = seconds / (24 * 60 * 60);
		mhour = (seconds / (60 * 60)) % 24;
		mmin = (seconds / 60) % 60;

	}

	/**
	 * 倒计时计算
	 */
	private void ComputeTime() {
		mTotalSeconds--;
		msecond--;
		if (msecond < 0) {
			mmin--;
			msecond = 59;
			if (mmin < 0) {
				mmin = 59;
				mhour--;
				if (mhour < 0) {
					// 倒计时结束，一天有24个小时
					mhour = 23;
					mday--;

				}
			}
		}
	}

	public boolean isRun() {
		return run;
	}

	public void beginRun() {
		this.run = true;
		run();
	}

	public void stopRun() {
		this.run = false;
	}
	
	public void setCountDownListener(CountDownListener listener) {
		mListener=listener;
	}

	@Override
	public void run() {
		// 标示已经启动
		if (run) {
			ComputeTime();
			if (mday < 10) {
				mDayTxt1.setText("0");
				mDayTxt2.setText(mday + "");
			} else {
				mDayTxt1.setText(mday / 10 + "");
				mDayTxt2.setText(mday % 10 + "");
			}
			if (mhour < 10) {
				mHourTxt1.setText("0");
				mHourTxt2.setText(mhour + "");
			} else {
				mHourTxt1.setText(mhour / 10 + "");
				mHourTxt2.setText(mhour % 10 + "");
			}

			if (mmin < 10) {
				mMinuteTxt1.setText("0");
				mMinuteTxt2.setText(mmin + "");
			} else {
				mMinuteTxt1.setText(mmin / 10 + "");
				mMinuteTxt2.setText(mmin % 10 + "");
			}

			if (msecond < 10) {
				mSecondTxt1.setText("0");
				mSecondTxt2.setText(msecond + "");
			} else {
				mSecondTxt1.setText(msecond / 10 + "");
				mSecondTxt2.setText(msecond % 10 + "");
			}

			if (mTotalSeconds<=0) {
				stopRun();
				if (mListener!=null) {
					mListener.onStop();
				}
			}
			handler.sendEmptyMessageDelayed(RUN,1000);
		} else {
			removeCallbacks(this);
		}
	}

	public interface CountDownListener {

		/**
		 * 倒计时结束
		 */
		public abstract void onStop();
	}
}
