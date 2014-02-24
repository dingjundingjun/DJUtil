package com.dj.util;

import com.dj.util.views.GridAnimationView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

public class GridAnimationActivity extends Activity implements OnClickListener
{
	public static final int ANIMATION_SLEEP = 1000;
	private Button mBtnStart;
	private Button mBtnRelay;
	private GridAnimationView mGridView;
	private int[] mColorArray = new int[]
	{ Color.BLACK, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY,
			Color.GREEN, Color.LTGRAY, Color.MAGENTA, Color.RED,
			Color.TRANSPARENT, Color.WHITE, Color.YELLOW,Color.BLUE, Color.CYAN,Color.GREEN, Color.LTGRAY};
	private GridAdapter mGridAdapter = new GridAdapter();
	private final String TAG = "GridAnimationActivity";
	private AdapterView<Adapter> mViewGroup;
	private final int HANDLE_UPDATE_ONE_COLOUM = 0;
	private AnimationHandler mAnimationHandler = new AnimationHandler();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.grid_animation);
		mBtnStart = (Button) findViewById(R.id.btn_start_animation);
		mBtnRelay = (Button) findViewById(R.id.btn_start_relay);
		mGridView = (GridAnimationView) findViewById(R.id.gridview);
		mGridView.setAdapter(mGridAdapter);
		mGridAdapter.notifyDataSetChanged();
		mBtnStart.setOnClickListener(this);
		mBtnRelay.setOnClickListener(this);
		mGridView.setNumColumns(4);
		mGridView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				mGridView.animationMoveToOneColumn(arg2 - mGridView.getFirstVisiblePosition());
				Message msg = new Message();
				msg.what= HANDLE_UPDATE_ONE_COLOUM;
				msg.arg1 = arg2 - mGridView.getFirstVisiblePosition() - 1;
				mAnimationHandler.sendMessageDelayed(msg, ANIMATION_SLEEP);
			}
			
		});
	}

	public class GridAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			return mColorArray.length;
		}

		@Override
		public Object getItem(int position)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = LinearLayout.inflate(GridAnimationActivity.this, R.layout.grid_animation_item, null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.text);
			tv.setBackgroundColor(mColorArray[position]);
			tv.setText("" + position);
			return convertView;
		}

	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.btn_start_animation:
			{
/*				LayoutParams params = mGridView.getLayoutParams();
				params.width = 250;*/
//				mGridView.setChange(true);
//				mGridView.setNumColumns(1);
//				mGridView.setSelection(8);
//				mGridAdapter.notifyDataSetChanged();
				mGridView.animationMoveToOneColumn(5);
				Message msg = new Message();
				msg.what= HANDLE_UPDATE_ONE_COLOUM;
				msg.arg1 = 3;
				mAnimationHandler.sendMessageDelayed(msg, 3000);
				break;
			}
			case R.id.btn_start_relay:
			{
				LayoutParams params = mGridView.getLayoutParams();
				params.width = -2;
				mGridView.setChange(false);
				mGridView.setNumColumns(4);
				mGridAdapter.notifyDataSetChanged();
				break;
			}
		}
	}
	
	public class AnimationHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch(msg.what)
			{
			case HANDLE_UPDATE_ONE_COLOUM:
			{
				mGridView.setNumColumns(1);
				mGridView.setSelection(msg.arg1);
				mGridAdapter.notifyDataSetChanged();
				break;
			}
			}
		}
		
	}
}
