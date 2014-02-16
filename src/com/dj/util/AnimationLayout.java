package com.dj.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class AnimationLayout extends RelativeLayout implements OnClickListener
{
	private final String TAG = "AnimationLayout";
	private int mWidth;
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
	public AnimationLayout(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}

	public void init()
	{
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
			MsgData msgData = (MsgData) msg.obj;
			degree = (int) (msgData.getmDegree());
			translateX = msgData.getmTranslateX();
			translateY = msgData.getmTranslateY();
//			Log.d("UpdateHandle","degree:" + degree + " " + translateX + " " + translateY);
/*			mWidth += 2;
			RelativeLayout.LayoutParams params = new LayoutParams(mWidth, -2);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			setLayoutParams(params);*/
			
//			Log.d(TAG, "" + degree);
			setRotation(degree);
			setTranslationY(translateY);
			setTranslationX(translateX);
//			mView.invalidate();
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
			MsgData msgData = new MsgData();
			float degreeTemp = mCurrentRotate - mLastRotate;;
			for(int i = 0;i < times;i++)
			{
				//首先做旋转
				degreeDx = degreeTemp / times *(i + 1);
				float tempDegree = (float) ((mLastRotate + degreeDx) * Math.PI / 180);
				translateY = (float) (mRY * Math.cos(tempDegree) - mRY);
				translateX = (float) (mRX * Math.sin(tempDegree));
				msgData.setmDegree((int) -(degreeDx + mLastRotate));
				msgData.setmTranslateY((int) translateY);
				msgData.setmTranslateX((int) translateX);
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
//			mCurrentRotate -= 90;
			/*if(mLastRotate >= 360)
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
			}*/
//			Log.d("onClick", "R.id.rightanimation" + " " + mCurrentRotate + " " + mLastRotate);
//			startAnimation();
			break;
		}
		case R.id.rightanimation:
		{
			rotateDegree(180);
			/*mCurrentRotate += 90;
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
			}*/
//			Log.d("onClick", "R.id.rightanimation" + " " + mCurrentRotate + " " + mLastRotate);
//			startAnimation();
			break;
		}
		}
	}
	
	public class MsgData
	{
		private int mDegree;
		private int mTranslateY;
		private int mTranslateX;
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
		startAnimation();
	}
}
