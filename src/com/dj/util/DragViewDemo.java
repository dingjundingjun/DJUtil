package com.dj.util;

import com.dj.util.views.DragViews;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class DragViewDemo extends Activity
{
	private LinearLayout mMainLayout;
	private DragViews mDragViews;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dragview_layout);
		init();
	}
	
	private void init()
	{
		mMainLayout = (LinearLayout) findViewById(R.id.main_layout);
		mDragViews = (DragViews)findViewById(R.id.dragviews);
		LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View view1 = new View(this);
		view1.setBackgroundColor(Color.BLUE);
		View view2 = new View(this);
		view1.setBackgroundColor(Color.RED);
		View view3 = new View(this);
		view3.setBackgroundColor(Color.GREEN);
		mDragViews.addView(view1, params);
		mDragViews.addView(view2, params);
		mDragViews.addView(view3, params);
	}
}

