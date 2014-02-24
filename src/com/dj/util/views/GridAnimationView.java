package com.dj.util.views;

import java.util.ArrayList;

import com.dj.util.GridAnimationActivity;

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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
	private final int GRID_DEVIATION_Y = 5;
	private final int GRID_DEVIATION_X = 5;
	private final int CHILD_VIEW_WIDTH = 250;
	private final int CHILD_VIEW_LAND_HEIGHT = 180;
	private final int CHILD_MIDDLE_POSITION = 2;
	private final int CHILD_LAND_ALL_POSITION_NUMBER = 4;
	private final String TAG = "GridAnimationView";
	private boolean bShortAnimation = false;
	private ArrayList<View> mArrayList = new ArrayList<View>();
	private int mSelection = 5;
	private int[][]mPositionArray = new int[][]{{0,0},{0,180},{0,360},{0,540}};
	private int[] mTopPosition = new int[]{0,-500};
	private int[] mBottomPosition = new int[]{0,1700};
	public GridAnimationView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void attachLayoutAnimationParameters(View child,
			android.view.ViewGroup.LayoutParams params, int index, int count)
	{
		JDingDebug.printfD(TAG, "attachLayoutAnimationParameters:" + index
				+ " " + count);
		super.attachLayoutAnimationParameters(child, params, index, count);
	}

	@Override
	protected void layoutChildren()
	{
		super.layoutChildren();
	}

	public void setChange(boolean b)
	{
		bShortAnimation = b;
	}

	/**
	 * 移动到一边的动画
	 */
	public void animationMoveToOneColumn(int selection)
	{
		// 在 这要弄一个动画 动画完成重新排序
		int childCount = this.getChildCount();
		mArrayList.clear();
		JDingDebug.printfD(TAG, "animationMoveToOneColumn:" + selection + " " + childCount  + " getLastVisiblePosition:" + getLastVisiblePosition());
		int firstPositonY = (CHILD_MIDDLE_POSITION - selection - 1)* CHILD_VIEW_LAND_HEIGHT;
		int firstPositionX = GRID_DEVIATION_X;
		if(selection >= getLastVisiblePosition() - 1)
		{
			firstPositonY = (CHILD_LAND_ALL_POSITION_NUMBER - getLastVisiblePosition() - 1)* CHILD_VIEW_LAND_HEIGHT;
		}
		if(selection == getFirstVisiblePosition())
		{
			firstPositonY = 0;
		}
		firstPositonY += GRID_DEVIATION_Y;
		
		for (int i = 0; i < childCount; i++)
		{
			View tempView = getChildAt(i);
			TranslateAnimation translateAnimation;
			float dx;
			float dy;
			dx = firstPositionX - tempView.getX();
			dy = firstPositonY + i*CHILD_VIEW_LAND_HEIGHT - tempView.getY();
			JDingDebug.printfD(TAG, "dy:" + dy);
			AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
			translateAnimation = new TranslateAnimation(0,dx,0,dy);
			AnimationSet animationSet = new AnimationSet(false);
//			animationSet.addAnimation(alphaAnimation);
			animationSet.addAnimation(translateAnimation);
			animationSet.setDuration(GridAnimationActivity.ANIMATION_SLEEP);
			tempView.setAnimation(animationSet);
		}
	}
}
