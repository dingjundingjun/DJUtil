package com.dj.util.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * @author dingj
 * this is a dragViews.it is drag by velocity
 *
 */
@TargetApi(11)
public class DragViews extends ViewGroup
{
	private VelocityTracker mVelocityTracker;
	private static final int SNAP_VELOCITY = 100;
	private Scroller mScroller;
	private int mCurScreen;
	private int mDefaultScreen = 0;
	private float mLastMotionX;
	private OnViewChangeListener mOnViewChangeListener;
	private float mLastdispathX;
	public DragViews(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		if(changed)
		{
			int childLeft = 0;
			final int childCount = getChildCount();
			for(int i=0; i<childCount;i++)
			{
				final View childView = getChildAt(i);
				if(childView.getVisibility() != View.GONE)
				{
					final int childWidth = childView.getMeasuredWidth();
					childView.layout(childLeft, 0, childLeft + childWidth, childView.getMeasuredHeight());
					childLeft += childWidth;
				}
			}
		}
	}

	private void init(Context context)
	{
		mCurScreen = mDefaultScreen;
		mScroller = new Scroller(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int count = getChildCount();
		for(int i=0;i<count;i++)
		{
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		scrollTo(mCurScreen * width,0);
	}
	
	public void snapToDestination()
	{
		final int screenWidth = getWidth();
		final int destScreen = (getScrollX() + screenWidth/2)/screenWidth;
		snapToScreen(destScreen);
	}

	private void snapToScreen(int whichScreen)
	{
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		if(getScrollX() != (whichScreen * getWidth()))
		{
			final int delta = whichScreen * getWidth() - getScrollX();
			mScroller.startScroll(getScrollX(), 0, delta, 0,Math.abs(delta) * 2);
			mCurScreen = whichScreen;
			invalidate();
			if(mOnViewChangeListener != null)
			{
				mOnViewChangeListener.OnViewChange(mCurScreen);
			}
		}
	}

	public void snapToLastScreen()
	{
		snapToScreen(mCurScreen -1);
	}
	
	public void snapToNextScreen()
	{
		snapToScreen(mCurScreen + 1);
	}
	
	@Override
	public void computeScroll()
	{
		super.computeScroll();
		if(mScroller.computeScrollOffset())
		{
			scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
			postInvalidate();
		}
	}

	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
//		return false;
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		final int action = event.getAction();
		final float x = event.getX();
		final float y = event.getY();
		switch(action)
		{
			case MotionEvent.ACTION_DOWN:
			{
				System.out.println("2222222222222222222222222222");
				if(mVelocityTracker == null)
				{
					mVelocityTracker = VelocityTracker.obtain();
					mVelocityTracker.addMovement(event);
				}
				if(!mScroller.isFinished())
				{
					mScroller.abortAnimation();
				}
				mLastMotionX = x;
				return true;
//				break;
			}
			case MotionEvent.ACTION_MOVE:
			{
				int deltaX = (int)(mLastMotionX - x);
				System.out.println("jding debug:" + deltaX );
				if(IsCanMove(deltaX))
				{
					if(mVelocityTracker != null)
					{
						mVelocityTracker.addMovement(event);
					}
//					mLastMotionX = x;
//					scrollBy(deltaX, 0);
				}
				break;
			}
			case MotionEvent.ACTION_UP:
			{
				int velocityX = 0;
				if(mVelocityTracker != null)
				{
					mVelocityTracker.addMovement(event);
					mVelocityTracker.computeCurrentVelocity(100);
					velocityX = (int)mVelocityTracker.getXVelocity();
				}
//				System.out.println("jding debug:1111111111111111:" + velocityX );
				if(velocityX > SNAP_VELOCITY && mCurScreen > 0)
				{
					snapToScreen(mCurScreen -1);
				}
				else if(velocityX < -SNAP_VELOCITY && mCurScreen < getChildCount() - 1)
				{
					snapToScreen(mCurScreen + 1);
				}
				else
				{
					snapToDestination();
				}
				if(mVelocityTracker != null)
				{
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
				break;
			}
		}
		return super.dispatchTouchEvent(event);
	}

	@SuppressLint("NewApi")
	private boolean IsCanMove(int deltaX)
	{
		if(getScrollX() <= 0 && deltaX < 0)
		{
			return false;
		}
		if(getScrollX() >= (getChildCount() - 1) * getWidth() && deltaX > 0)
		{
			return false;
		}
		return true;
	}
	
	public void SetOnViewChangeListener(OnViewChangeListener listener)
	{
		mOnViewChangeListener = listener;
	}

	public interface OnViewChangeListener 
	{
		void OnViewChange(int view); 
	}
	
	public int getViewIndex()
	{
		return mCurScreen;
	}
}
