package com.dj.util;

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
	private int mCurrentWidth;
	private int mCurrentHeight;
	private int mStartOrientation;
	private int mEndOrientation;
	private int mMillis;
	private final int mAnimationSleep = 10;
	private UpdateHandle mUpdateHandle = new UpdateHandle();
	private Button mBtnLeft;
	private Button mBtnRight;
	private float mRotateDegree = 0;
	private float mCenterX = 600;
	private float mCenterY = 810;
	private float mRY = 735;
	private float mRX = 500;
	private float mCurrentRotate = 0;
	private float mLastRotate = 0;
	private int mScale;
	private final int SCALE_NONE = 0;
	private final int SCALE_LONG = 1;
	private final int SCALE_SHORT = 2;
	private int mHeight;
	private RelativeLayout mRelaLayout;
	public AnimationLayout(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}

	public void init()
	{
//		mRelaLayout = (RelativeLayout) (findViewById(R.id.relalayout));
		mBtnLeft = (Button)findViewById(R.id.leftanimation);
		mBtnRight = (Button)findViewById(R.id.rightanimation);
		mBtnRight.setOnClickListener(this);
		mBtnLeft.setOnClickListener(this);
	}
	
	public void setDuration(int millis)
	{
		mMillis = millis;
	}
	
	public void setOrientation(int startOrientation,int endOrientation)
	{
		mStartOrientation = startOrientation;
		mEndOrientation = endOrientation;
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
			MsgData msgData = (MsgData) msg.obj;
			degree = (int) (msgData.getmDegree());
			translateX = msgData.getmTranslateX();
			translateY = msgData.getmTranslateY();
			width = msgData.getmWidth();
			Log.d("UpdateHandle", "degree:" + degree + " width:" + width
					+ " height:" + mHeight);
			if(mScale == SCALE_LONG)
			{
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, mHeight);
				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				AnimationLayout.this.setLayoutParams(params);
//				params.width = width;
//				mRelaLayout.setLayoutParams(params);
				Log.d("UpdateHandle", "width:" + AnimationLayout.this.getWidth());
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
			int times = mMillis / mAnimationSleep;
			float degreeDx = 0;    //旋转的角度
			float translateX = 0;
			float translateY = 0;
			int width = 0;
			MsgData msgData = new MsgData();
			float degreeTemp = mCurrentRotate - mLastRotate;;
			for(int i = 0;i < times;i++)
			{
				//首先做旋转
				degreeDx = degreeTemp / times *(i + 1);
				float tempDegree = (float) ((mLastRotate + degreeDx) * Math.PI / 180);
				translateY = (float) (mRY * Math.cos(tempDegree) - mRY);
				translateX = (float) (mRX * Math.sin(tempDegree));
				if(mScale == SCALE_LONG)
				{
					width = 1200 + 720 / times * (i + 1);
				}
				msgData.setmDegree((int) -(degreeDx + mLastRotate));
				msgData.setmTranslateY((int) translateY);
				msgData.setmTranslateX((int) translateX);
				msgData.setmWidth(width);
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
		private int mWidth;
		
		public int getmWidth()
		{
			return mWidth;
		}
		public void setmWidth(int mWidth)
		{
			this.mWidth = mWidth;
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
		mCurrentRotate += degree;
		if(mLastRotate >= 360)
		{
			mLastRotate = mLastRotate - 360;
		}
		else if(mLastRotate <= -360)
		{
			mLastRotate = mLastRotate + 360;
		}
		if(mCurrentRotate > 360)
		{
			mCurrentRotate = mCurrentRotate - 360;
		}
		else if(mCurrentRotate < -360)
		{
			mCurrentRotate = mCurrentRotate + 360;
		}
		if((mCurrentRotate - mLastRotate) == 180 || (mCurrentRotate - mLastRotate) == -180)
		{
			mScale = SCALE_NONE;
		}
		if (mCurrentRotate == -90 || mCurrentRotate == 90
				|| mCurrentRotate == 270 || mCurrentRotate == -270)
		{
			mScale = SCALE_LONG;
		}
		else
		{
			mScale = SCALE_SHORT;
		}
		mCurrentWidth = AnimationLayout.this.getWidth();
		mHeight = AnimationLayout.this.getHeight();
		startAnimation();
	}
}
