package com.dj.util;

import jding.debug.JDingDebug;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dj.util.views.GridAnimationView;

public class GridAnimationActivity extends Activity implements OnClickListener
{
	public static int ANIMATION_SLEEP = 10000;
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
	private final int HANDLE_UPDATE_MUT_COLOUM = 1;
	private final int HANDLE_UPDATE_MUT_COLOUM_BEFOR = 2;
	private boolean bShort = false;
	private AnimationHandler mAnimationHandler = new AnimationHandler();
	private EditText mSpeedEdit;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.grid_animation);
		mBtnStart = (Button) findViewById(R.id.btn_start_animation);
		mBtnRelay = (Button) findViewById(R.id.btn_start_relay);
		mGridView = (GridAnimationView) findViewById(R.id.gridview);
		mSpeedEdit = (EditText)findViewById(R.id.speed);
		mSpeedEdit.setText("" + 500);
		mGridView.setAdapter(mGridAdapter);
		mGridAdapter.notifyDataSetChanged();
		mBtnStart.setOnClickListener(this);
		mBtnRelay.setOnClickListener(this);
		mGridView.setNumColumns(4);
		mGridView.setChange(false);
		mGridView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3)
			{
				ANIMATION_SLEEP = Integer.parseInt(mSpeedEdit.getText().toString());
				if(!bShort)
				{
					mGridView.animationMoveToOneColumn(position + mGridView.getFirstVisiblePosition());
					Message msg = new Message();
					msg.what= HANDLE_UPDATE_ONE_COLOUM;
					msg.arg1 = position + mGridView.getFirstVisiblePosition() - 1;
					mAnimationHandler.sendMessageDelayed(msg, ANIMATION_SLEEP);
					mGridView.getSelectionPosition(position - mGridView.getFirstVisiblePosition());
//					mGridView.animationMoveToMutColumn(position - mGridView.getFirstVisiblePosition());
					bShort = true;
				}
				else
				{
					mGridView.getSelectionPosition(position - mGridView.getFirstVisiblePosition());
					LayoutParams params = mGridView.getLayoutParams();
					params.width = -2;
					mGridView.setVisibility(View.INVISIBLE);
					mGridView.setNumColumns(4);
					mGridView.setAdapter(mGridAdapter);
					mGridAdapter.notifyDataSetChanged();
					JDingDebug.printfD(TAG, "bShort = true mGridView.getFirstVisiblePosition():" + mGridView.getFirstVisiblePosition() + " " + position);
					Message msgTemp = mAnimationHandler.obtainMessage();
					msgTemp.what = HANDLE_UPDATE_MUT_COLOUM_BEFOR;
					msgTemp.arg1 = position - mGridView.getFirstVisiblePosition() + 1;
					bShort = false;
					mAnimationHandler.sendMessage(msgTemp);
				}
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
			case R.id.btn_start_animation:    //从1返回到5
			{
				LayoutParams params = mGridView.getLayoutParams();
				params.width = -2;
				mGridView.setChange(true);
				mGridView.setNumColumns(4);
				mGridAdapter.notifyDataSetChanged();
				
				mGridView.getSelectionPosition(1);
				mGridView.animationMoveToMutColumn(1);
				
				Message msg = mAnimationHandler.obtainMessage();
				msg.what = HANDLE_UPDATE_MUT_COLOUM;
				msg.arg1 = 1;
				mAnimationHandler.sendMessageDelayed(msg, ANIMATION_SLEEP);
				bShort = false;
				break;
			}
			case R.id.btn_start_relay:
			{
				mGridView.setChange(false);
				LayoutParams params = mGridView.getLayoutParams();
				params.width = -2;
				mGridView.setChange(false);
				mGridView.setNumColumns(4);
				mGridAdapter.notifyDataSetChanged();
				bShort = false;
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
					LayoutParams params = mGridView.getLayoutParams();
					params.width = 250;
					mGridView.setNumColumns(1);
					mGridView.setSelection(msg.arg1);
					mGridAdapter.notifyDataSetChanged();
					bShort = true;
					break;
				}
				case HANDLE_UPDATE_MUT_COLOUM:
				{
					LayoutParams params = mGridView.getLayoutParams();
					params.width = -2;
//					mGridView.setChange(true);
					mGridView.setNumColumns(4);
					mGridView.setAdapter(mGridAdapter);
					mGridAdapter.notifyDataSetChanged();
					break;
				}
				case HANDLE_UPDATE_MUT_COLOUM_BEFOR:
				{
					mGridView.animationMoveToMutColumn(msg.arg1);
					mGridView.invalidate();
					mGridView.setVisibility(View.VISIBLE);
					Message msgTemp = mAnimationHandler.obtainMessage();
					msgTemp.what = HANDLE_UPDATE_MUT_COLOUM;
					msgTemp.arg1 = 1;
					mAnimationHandler.sendMessageDelayed(msgTemp, ANIMATION_SLEEP);
					bShort = false;
					break;
				}
			}
			
		}
		
	}
}
