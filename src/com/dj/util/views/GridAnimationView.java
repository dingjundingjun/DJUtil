package com.dj.util.views;

import java.util.ArrayList;

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
	private final String TAG = "GridAnimationView";
	private boolean bShortAnimation = false;
	private ArrayList<View> mArrayList = new ArrayList<View>();
	private int mSelection = 5;
	private int[][]mPositionArray = new int[][]{{0,10},{0,370},{0,730},{0,1090}};
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
		// 在 这要弄一个动画 动画完成重新排序
		// if(!bUseAnimation)
		// int changeBeforFirstPosition = getFirstVisiblePosition();
		// int firstLayout = mSelection - 2;
		// mArrayList.clear();
		// for(int i = 0;i < 16;i++) //把当前显示的view保存起来
		// {
		// mArrayList.add(getChildAt(i));
		// }
		// // JDingDebug.printfD(TAG," getFirstVisiblePosition():" +
		// getFirstVisiblePosition()); //变换以前第一个VIEW位置
		// if(!bShortAnimation)
		// {
		// super.layoutChildren();
		// }
		// if(bShortAnimation)
		// {
		// int firstViewPosition = getFirstVisiblePosition(); //第一个view位置
		// JDingDebug.printfD(TAG, "layoutChildren:");
		// int childCount = getChildCount();
		// RotateAnimation rotateAnimation = new RotateAnimation(0, 90);
		// TranslateAnimation translateAnimation = new TranslateAnimation(0,
		// -50, 0, 0);
		// AnimationSet animationSet = new AnimationSet(false);
		// animationSet.addAnimation(rotateAnimation);
		// animationSet.addAnimation(translateAnimation);
		// animationSet.setDuration(3000);
		// for(int i = 0;i < 16;i++)
		// {
		// View tempView = mArrayList.get(i);
		// if(i < firstLayout)
		// {
		// }
		//
		//
		//
		// if(i % 2 == 0)
		// tempView.setRotation(60);
		// else
		// tempView.setRotation(-60);
		// }
		//
		// for(int i = 0; i < childCount; i++)
		// {
		// View child = getChildAt(i);
		// JDingDebug.printfD(TAG,
		// "index:" + i + " left:" + child.getLeft() + " top:"
		// + child.getTop() + " width:" + child.getWidth()
		// + " height:" + child.getHeight() + " getFirstVisiblePosition():" +
		// getFirstVisiblePosition());
		// /*android.view.ViewGroup.LayoutParams params =
		// child.getLayoutParams();*/
		// // child.setAnimation(animationSet);
		// if(i % 2 == 0)
		// child.setRotation(60);
		// else
		// child.setRotation(-60);
		// }
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
		int changeBeforFirstPosition = getFirstVisiblePosition();
		int childCount = this.getChildCount();
		mArrayList.clear();
		int j = 0;
		JDingDebug.printfD(TAG, "animationMoveToOneColumn:" + selection + " " + childCount);
		for (int i = 0; i < childCount; i++)
		{
			View tempView = getChildAt(i);
			TranslateAnimation translateAnimation;
			int x;
			int y;
			float dx;
			float dy;
			
			if (i < selection/* || i >= selection + 2*/)
			{
				dx = mTopPosition[0] - tempView.getX();
				dy = mTopPosition[1] - tempView.getY();
				AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
				translateAnimation = new TranslateAnimation(0,dx,0,dy);
				AnimationSet animationSet = new AnimationSet(false);
				animationSet.addAnimation(alphaAnimation);
				animationSet.addAnimation(translateAnimation);
				animationSet.setDuration(3000);
				tempView.setAnimation(animationSet);
			}
			else if(i >= selection + 2)
			{
				dx = mBottomPosition[0] - tempView.getX();
				dy = mBottomPosition[1] - tempView.getY();
				AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
				translateAnimation = new TranslateAnimation(0,dx,0,dy);
				AnimationSet animationSet = new AnimationSet(false);
				animationSet.addAnimation(alphaAnimation);
				animationSet.addAnimation(translateAnimation);
				animationSet.setDuration(3000);
				tempView.setAnimation(animationSet);
			}
			else
			{
				x = mPositionArray[j][0];
			    y = mPositionArray[j][1];
				dx = (x - tempView.getX());
				dy = (y - tempView.getY());
				translateAnimation = new TranslateAnimation(0,dx,0,dy);
				translateAnimation.setDuration(3000);
				tempView.setAnimation(translateAnimation);
				j++;
			}
		}
//		for (int i = 0; i < 16; i++) // 把当前显示的view保存起来
//		{
//			mArrayList.add(getChildAt(i));
//		}
		// // JDingDebug.printfD(TAG," getFirstVisiblePosition():" +
		// getFirstVisiblePosition()); //变换以前第一个VIEW位置
		// if(!bShortAnimation)
		// {
		// super.layoutChildren();
		// }
		// if(bShortAnimation)
		// {
		// int firstViewPosition = getFirstVisiblePosition(); //第一个view位置
		// JDingDebug.printfD(TAG, "layoutChildren:");
		// int childCount = getChildCount();
		// RotateAnimation rotateAnimation = new RotateAnimation(0, 90);
		// TranslateAnimation translateAnimation = new TranslateAnimation(0,
		// -50, 0, 0);
		// AnimationSet animationSet = new AnimationSet(false);
		// animationSet.addAnimation(rotateAnimation);
		// animationSet.addAnimation(translateAnimation);
		// animationSet.setDuration(3000);
		// for(int i = 0;i < 16;i++)
		// {
		// View tempView = mArrayList.get(i);
		// if(i < firstLayout)
		// {
		// }
		//
		//
		//
		// if(i % 2 == 0)
		// tempView.setRotation(60);
		// else
		// tempView.setRotation(-60);
		// }
		//
		// for(int i = 0; i < childCount; i++)
		// {
		// View child = getChildAt(i);
		// JDingDebug.printfD(TAG,
		// "index:" + i + " left:" + child.getLeft() + " top:"
		// + child.getTop() + " width:" + child.getWidth()
		// + " height:" + child.getHeight() + " getFirstVisiblePosition():" +
		// getFirstVisiblePosition());
		// /*android.view.ViewGroup.LayoutParams params =
		// child.getLayoutParams();*/
		// // child.setAnimation(animationSet);
		// if(i % 2 == 0)
		// child.setRotation(60);
		// else
		// child.setRotation(-60);
		// }
	}
}
