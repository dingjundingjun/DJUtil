package com.dj.util;

import android.app.Activity;
import android.os.Bundle;

public class RotateActivity extends Activity
{
	private AnimationLayout mView;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rotate_layout);
		mView = (AnimationLayout) findViewById(R.id.testlayout);
		mView.init();
		mView.setDuration(2000);
	}
}
