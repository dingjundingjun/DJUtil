package com.dj.util;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class CameraUseActivity extends Activity implements OnClickListener
{
	private final String TAG = "CameraUseActivity";
	private Button mBtnOprateSystemUI;
	private ActionBar mActionBar;
	private LinearLayout mMainView;
	private final int DEFAULT_SYSTEM_UI_VISIBILITY = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.camera_use);
		init();
	}
	
	private void init()
	{
		mMainView = (LinearLayout)findViewById(R.id.main_view);
		mBtnOprateSystemUI = (Button)findViewById(R.id.oprate_systemui);
		mBtnOprateSystemUI.setOnClickListener(this);
		mActionBar = this.getActionBar();
		mActionBar.show();
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.oprate_systemui:
			{
				if(mActionBar.isShowing())
				{
					mActionBar.hide();
					setSystemBarsVisibility(false);
				}
				else
				{
					mActionBar.show();
					setSystemBarsVisibility(true);
				}
				break;
			}
		}
	}
	/**
	 *   
	1. View.SYSTEM_UI_FLAG_VISIBLE：显示状态栏，Activity不全屏显示(恢复到有状态的正常情况)。

    2. View.INVISIBLE：隐藏状态栏，同时Activity会伸展全屏显示。

    3. View.SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示，且状态栏被隐藏覆盖掉。

    4. View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住。

    5. View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

    6. View.SYSTEM_UI_LAYOUT_FLAGS：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

    7. View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。

    8. View.SYSTEM_UI_FLAG_LOW_PROFILE：状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏。*/
	private void setSystemBarsVisibility(boolean visible)
	{
		int currentSystemUIVisibility = mMainView.getSystemUiVisibility();
		 int newSystemUIVisibility =
	                (visible ? View.SYSTEM_UI_FLAG_VISIBLE :
	                        View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_FULLSCREEN);
	        if (newSystemUIVisibility != currentSystemUIVisibility) {
	        	mMainView.setSystemUiVisibility(newSystemUIVisibility);
	        }
    }
}
