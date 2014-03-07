package com.dj.util.views;

import java.util.List;

import com.hanvon.core.StrokeView;
import com.hanvon.core.StrokeView.RecognitionHandler;
import com.hanvon.core.StrokeView.RecognitionListerner;

import android.content.Context;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

public class HandWriteRecognitionView extends RelativeLayout
{
	private final String TAG = "HandWriteRecognitionView";
	private int mWidth = 0;
	private int mHeight = 0;
	private StrokeView mStrokeView = null;
	private Context mContext;
	private RecognitionListerner mRecognitionListerner;
	public static final int RECOGNITION_CHI = 0;
	public static final int RECOGNITION_ENG = 1;
	public static final int RECOGNITION_ALL = 2;
	private int mMode = 0;
	public HandWriteRecognitionView(Context context, AttributeSet attrs,
			int defStyle)
	{
		super(context, attrs, defStyle);
	}

	
	public HandWriteRecognitionView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		super.onLayout(changed, l, t, r, b);
		if((mWidth != r-l) || (mHeight != b-t))
		{
			Log.d(TAG, "onLayout: 1111111111111111111111111");
			mWidth = r - l;
			mHeight = b - t;
			if(mContext == null)
				return;
			if(mStrokeView == null)
			{
				this.removeAllViews();
				mStrokeView = new StrokeView(mContext, mWidth, mHeight);
				mStrokeView.setRecognitionListerner(mRecognitionListerner);
				if(mMode == RECOGNITION_CHI)
				{
					mStrokeView.setRecogRangeCHI();
				}
				else if(mMode == RECOGNITION_ENG)
				{
					mStrokeView.setRecogModeEng();
				}
				else if(mMode == RECOGNITION_ALL)
				{
					mStrokeView.setRecogModeAll();
				}
				this.addView(mStrokeView);
			}
		}
	}
	
	public void init(Context context)
	{
		mContext = context;
	}
	
	public void setBackground(int color)
	{
		if(mStrokeView != null)
		{
			mStrokeView.setBackgroundColor(color);
		}
	}
	
	public void clear()
	{
		if(mStrokeView != null)
		{
			mStrokeView.clear();
		}
	}
	
	public byte[] test()
	{
		if(mStrokeView != null)
		{
			byte[] b = mStrokeView.nativeRecognition();
			return b;
		}
		return null;
	}


	public void setRecognitionListener(
			RecognitionListerner listener)
	{
		mRecognitionListerner = listener;
	}
	
	public void setMode(int m)
	{
		mMode = m;
		if(mStrokeView != null)
		{
			if(mMode == RECOGNITION_CHI)
			{
				mStrokeView.setRecogRangeCHI();
			}
			else if(mMode == RECOGNITION_ENG)
			{
				mStrokeView.setRecogModeEng();
			}
			else if(mMode == RECOGNITION_ALL)
			{
				mStrokeView.setRecogModeAll();
			}
		}
	}
}
