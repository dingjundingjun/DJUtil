package com.dj.util.views;

import com.hanvon.core.StrokeView;

import jding.debug.JDingDebug;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class HandWriteRecognitionView extends RelativeLayout
{
	private final String TAG = "HandWriteRecognitionView";
	private int mWidth = 0;
	private int mHeight = 0;
	private StrokeView mStrokeView = null;
	private Context mContext;
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
			mWidth = r - l;
			mHeight = b - t;
			mStrokeView = null;
			this.removeAllViews();
			mStrokeView = new StrokeView(mContext, mWidth, mHeight);
			this.addView(mStrokeView);
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
			byte[] b = mStrokeView.recognition();
			return b;
		}
		return null;
	}
}
