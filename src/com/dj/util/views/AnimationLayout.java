package com.dj.util.views;

import com.dj.util.R;
import com.dj.util.Util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class AnimationLayout extends RelativeLayout implements OnClickListener
{
	private final String TAG = "AnimationLayout";
	/**初始的高度**/
	private int mHeight;
	/**初始的宽度*/
	private int mWidth;
	private int mMillis;
	private final int mAnimationSleep = 10;
	private UpdateHandle mUpdateHandle = new UpdateHandle();
	private Button mBtnLeft;
	private Button mBtnRight;
	/**半径*/
	private float mRY = 0;
	private float mRX = 0;
	/**需要旋转到的角度*/
	private float mCurrentRotate = 0;
	/**上一次的角度*/
	private float mLastRotate = 0;
	/**变形模式*/
	private int mScale;
	/**宽度不变*/
	private final int SCALE_NONE = 0;
	/**变长*/
	private final int SCALE_LONG = 1;
	/**变短*/
	private final int SCALE_SHORT = 2;
	private Context mContext;
	public AnimationLayout(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}

	public void init(Context c)
	{
		mBtnLeft = (Button)findViewById(R.id.leftanimation);
		mBtnRight = (Button)findViewById(R.id.rightanimation);
		mBtnRight.setOnClickListener(this);
		mBtnLeft.setOnClickListener(this);
		mContext = c;
		if(!Util.isLandScape(mContext))
		{
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 1024);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			AnimationLayout.this.setLayoutParams(params);
		}
		else
		{
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(1024, 100);
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			AnimationLayout.this.setLayoutParams(params);
		}
	}
	
	/**
	 * 设置延时
	 * @param millis
	 */
	public void setDuration(int millis)
	{
		mMillis = millis;
	}
	
	/**
	 * 开始动画
	 */
	public void startAnimation()
	{
		AnimationControl animationControl = new AnimationControl();
		animationControl.start();
	}
	
	public class UpdateHandle extends Handler
	{
		@Override
		public void handleMessage(Message msg) 
		{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int degree;
			int translateX;
			int translateY;
			int width;
			int height;
			MsgData msgData = (MsgData) msg.obj;
			degree = (int) (msgData.getmDegree());
			translateX = msgData.getmTranslateX();
			translateY = msgData.getmTranslateY();
			width = msgData.getmWidth();
			height = msgData.getmHeight();
			Log.d("UpdateHandle", "degree:" + degree + " width:" + width
					+ " height:" + height + " translateX:" + translateX + " translateY" + translateY + " mScale" + mScale);
			if(!Util.isLandScape(mContext))
			{
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				params.addRule(RelativeLayout.CENTER_VERTICAL);
				AnimationLayout.this.setLayoutParams(params);
			}
			else
			{
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
				params.addRule(RelativeLayout.ALIGN_PARENT_TOP );
				params.addRule(RelativeLayout.CENTER_HORIZONTAL);
				AnimationLayout.this.setLayoutParams(params);
			}
//			Log.d(TAG, "" + degree);
			setRotation(degree);
			setTranslationY(translateY);
			setTranslationX(translateX);
		}
		
	}
	public class AnimationControl extends Thread
	{
		@Override
		public void run() 
		{
			super.run();
			int times = mMillis;
			float degreeDx = 0;    //旋转的角度
			float translateX = 0;
			float translateY = 0;
			int width = 0;
			int height = 0;
			MsgData msgData = new MsgData();
			float degreeTemp = mCurrentRotate - mLastRotate;;
			for(int i = 0;i < times;i++)
			{
				//首先做旋转
				degreeDx = degreeTemp / times *(i + 1);
				float tempDegree;
				if(!Util.isLandScape(mContext))
				{
					tempDegree = (float) ((mLastRotate + degreeDx + 90) * Math.PI / 180);
					translateY = (float) (mRY * Math.cos(tempDegree));
					translateX = (float) (mRX * Math.sin(tempDegree) - mRX);
					if(mScale == SCALE_LONG)
					{
						height = Util.DISPLAY_WIDTH + (Util.DISPLAY_HEIGHT - Util.DISPLAY_WIDTH)*(i + 1) / times;
						width = mWidth;
					}
					else if(mScale == SCALE_SHORT)
					{
						height = Util.DISPLAY_HEIGHT - (Util.DISPLAY_HEIGHT - Util.DISPLAY_WIDTH)*(i + 1) / times;
						width = mWidth;
					}
				}
				else
				{
					tempDegree = (float) ((mLastRotate + degreeDx + 180) * Math.PI / 180);
					translateY = (float) (mRY * Math.cos(tempDegree) + mRY);
					translateX = (float) (mRX * Math.sin(tempDegree));
					if(mScale == SCALE_LONG)
					{
						height = mHeight;
						width = Util.DISPLAY_HEIGHT + (Util.DISPLAY_WIDTH - Util.DISPLAY_HEIGHT)*(i + 1) / times;
					}
					else if(mScale == SCALE_SHORT)
					{
						height = mHeight;
						width = Util.DISPLAY_WIDTH - (Util.DISPLAY_WIDTH - Util.DISPLAY_HEIGHT)*(i + 1) / times;
					}
				}
				
				Log.d(TAG, "Math.cos(tempDegree):" + Math.cos(tempDegree) + " Math.sin(tempDegree):" + Math.sin(tempDegree)
				+" translateX:" + translateX + " translateY" + translateY + " mRX:" + mRX + " mRY:" + mRY);
				msgData.setmDegree((int) -(degreeDx + mLastRotate));
				msgData.setmTranslateY((int) translateY);
				msgData.setmTranslateX((int) translateX);
				msgData.setmWidth(width);
				Log.d(TAG, "height :" + height);
				msgData.setmHeight(height);
				Message msg = mUpdateHandle.obtainMessage();
				msg.obj = msgData;
				mUpdateHandle.sendMessage(msg);
				try
				{
					Thread.sleep(10);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			mLastRotate = mCurrentRotate;
		}
	}
	@Override
	public void onClick(View arg0)
	{
		switch (arg0.getId())
		{
		case R.id.leftanimation:
		{
			rotateDegree(-90);
			break;
		}
		case R.id.rightanimation:
		{
			rotateDegree(90);
			break;
		}
		}
	}
	
	public class MsgData
	{
		private int mDegree;
		private int mTranslateY;
		private int mTranslateX;
		private int width;
		private int height;
		
		public int getmHeight()
		{
			Log.d(TAG, "MsgData:height:" + height);
			return height;
		}
		public void setmHeight(int mHeight)
		{
			this.height = mHeight;
		}
		public int getmWidth()
		{
			return width;
		}
		public void setmWidth(int mWidth)
		{
			this.width = mWidth;
		}
		public int getmDegree()
		{
			return mDegree;
		}
		public void setmDegree(int mDegree)
		{
			this.mDegree = mDegree;
		}
		public int getmTranslateY()
		{
			return mTranslateY;
		}
		public void setmTranslateY(int mTranslateY)
		{
			this.mTranslateY = mTranslateY;
		}
		public int getmTranslateX()
		{
			return mTranslateX;
		}
		public void setmTranslateX(int mTranslateX)
		{
			this.mTranslateX = mTranslateX;
		}
	}
	
	public void rotateDegree(int degree)
	{
		if(!Util.isLandScape(mContext))
		{
			mRX = (Util.DISPLAY_WIDTH - this.getWidth()) / 2;
			mRY = (Util.DISPLAY_HEIGHT - this.getWidth()) / 2;
		}
		else
		{
			mRX = (Util.DISPLAY_WIDTH - this.getHeight()) / 2;
			mRY = (Util.DISPLAY_HEIGHT - this.getHeight()) / 2;
		}
		if(mCurrentRotate == 360 || mCurrentRotate == -360)
		{
			mCurrentRotate = 0;
		}
		if(mLastRotate == 360 || mLastRotate == -360)
		{
			mLastRotate = 0;
		}
		mCurrentRotate += degree;
		if((mCurrentRotate - mLastRotate) == 180 || (mCurrentRotate - mLastRotate) == -180)
		{
			mScale = SCALE_NONE;
		}
		if (mCurrentRotate == -90 || mCurrentRotate == 90
				|| mCurrentRotate == 270 || mCurrentRotate == -270)
		{
				mScale = SCALE_SHORT;
		}
		else
		{
				mScale = SCALE_LONG;
		}
		Log.d(TAG, "mCurrent:" + mCurrentRotate + " mLast" + mLastRotate);
		mWidth = AnimationLayout.this.getWidth();
		mHeight = AnimationLayout.this.getHeight();
		startAnimation();
	}
}
