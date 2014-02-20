package com.dj.util.views;

import jding.debug.JDingDebug;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewDebug.CapturedViewProperty;
import android.view.ViewDebug.ExportedProperty;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

public class GridAnimationView extends GridView
{
	private final String TAG = "GridAnimationView";
	private boolean bShortAnimation = false;
	public GridAnimationView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void attachLayoutAnimationParameters(View child,
			android.view.ViewGroup.LayoutParams params, int index, int count)
	{
		JDingDebug.printfD(TAG, "attachLayoutAnimationParameters:" + index + " " + count);
		super.attachLayoutAnimationParameters(child, params, index, count);
	}

	@Override
	protected int computeVerticalScrollExtent()
	{
		// TODO Auto-generated method stub
		return super.computeVerticalScrollExtent();
	}

	@Override
	protected int computeVerticalScrollOffset()
	{
		// TODO Auto-generated method stub
		return super.computeVerticalScrollOffset();
	}

	@Override
	protected int computeVerticalScrollRange()
	{
		// TODO Auto-generated method stub
		return super.computeVerticalScrollRange();
	}

	@Override
	public ListAdapter getAdapter()
	{
		// TODO Auto-generated method stub
		return super.getAdapter();
	}

	@Override
	public int getColumnWidth()
	{
		// TODO Auto-generated method stub
		return super.getColumnWidth();
	}

	@Override
	public int getGravity()
	{
		// TODO Auto-generated method stub
		return super.getGravity();
	}

	@Override
	public int getHorizontalSpacing()
	{
		// TODO Auto-generated method stub
		return super.getHorizontalSpacing();
	}

	@Override
	@ExportedProperty
	public int getNumColumns()
	{
		// TODO Auto-generated method stub
		return super.getNumColumns();
	}

	@Override
	public int getRequestedColumnWidth()
	{
		// TODO Auto-generated method stub
		return super.getRequestedColumnWidth();
	}

	@Override
	public int getRequestedHorizontalSpacing()
	{
		// TODO Auto-generated method stub
		return super.getRequestedHorizontalSpacing();
	}

	@Override
	public int getStretchMode()
	{
		// TODO Auto-generated method stub
		return super.getStretchMode();
	}

	@Override
	public int getVerticalSpacing()
	{
		// TODO Auto-generated method stub
		return super.getVerticalSpacing();
	}

	@Override
	protected void layoutChildren()
	{
		
		//在 这要弄一个动画  动画完成重新排序
//		if(!bUseAnimation)
		{
			super.layoutChildren();
		}
		if(bShortAnimation)
		{
			int firstViewPosition = getFirstVisiblePosition();    //第一个位置
			JDingDebug.printfD(TAG, "layoutChildren:");
			int childCount = getChildCount();
			RotateAnimation rotateAnimation = new RotateAnimation(0, 90);
			TranslateAnimation translateAnimation = new TranslateAnimation(0, -50, 0, 0);
			AnimationSet animationSet = new AnimationSet(false);
			animationSet.addAnimation(rotateAnimation);
			animationSet.addAnimation(translateAnimation);
			animationSet.setDuration(3000);
			
			for(int i = 0; i < childCount; i++)
			{
				View child = getChildAt(i);
				JDingDebug.printfD(TAG,
						"index:" + i + " left:" + child.getLeft() + " top:"
								+ child.getTop() + " width:" + child.getWidth()
								+ " height:" + child.getHeight() + " getFirstVisiblePosition():" + );
				/*android.view.ViewGroup.LayoutParams params = child.getLayoutParams();*/
	//			child.setAnimation(animationSet);
				if(i % 2 == 0)
					child.setRotation(60);
				else
					child.setRotation(-60);
			}
		}
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect)
	{
		// TODO Auto-generated method stub
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
	}

	@Override
	public void onInitializeAccessibilityEvent(AccessibilityEvent event)
	{
		// TODO Auto-generated method stub
		super.onInitializeAccessibilityEvent(event);
	}

	@Override
	public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info)
	{
		// TODO Auto-generated method stub
		super.onInitializeAccessibilityNodeInfo(info);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event)
	{
		// TODO Auto-generated method stub
		return super.onKeyMultiple(keyCode, repeatCount, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		// TODO Auto-generated method stub
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public void setAdapter(ListAdapter adapter)
	{
		// TODO Auto-generated method stub
		super.setAdapter(adapter);
	}

	@Override
	public void setColumnWidth(int columnWidth)
	{
		// TODO Auto-generated method stub
		super.setColumnWidth(columnWidth);
	}

	@Override
	public void setGravity(int gravity)
	{
		// TODO Auto-generated method stub
		super.setGravity(gravity);
	}

	@Override
	public void setHorizontalSpacing(int horizontalSpacing)
	{
		// TODO Auto-generated method stub
		super.setHorizontalSpacing(horizontalSpacing);
	}

	@Override
	public void setNumColumns(int numColumns)
	{
		// TODO Auto-generated method stub
		super.setNumColumns(numColumns);
	}

	@Override
	public void setRemoteViewsAdapter(Intent intent)
	{
		// TODO Auto-generated method stub
		super.setRemoteViewsAdapter(intent);
	}

	@Override
	public void setSelection(int position)
	{
		// TODO Auto-generated method stub
		super.setSelection(position);
	}

	@Override
	public void setStretchMode(int stretchMode)
	{
		// TODO Auto-generated method stub
		super.setStretchMode(stretchMode);
	}

	@Override
	public void setVerticalSpacing(int verticalSpacing)
	{
		// TODO Auto-generated method stub
		super.setVerticalSpacing(verticalSpacing);
	}

	@Override
	public void smoothScrollByOffset(int offset)
	{
		// TODO Auto-generated method stub
		super.smoothScrollByOffset(offset);
	}

	@Override
	public void smoothScrollToPosition(int position)
	{
		// TODO Auto-generated method stub
		super.smoothScrollToPosition(position);
	}
	
	public void setChange(boolean b)
	{
		bShortAnimation = b;
	}
}
