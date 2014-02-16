package com.dj.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private ListView mChooseList;
	private ChooseAdapter mChooseAdapter;
	private static String TAG = "MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		Log.d(TAG, "wifi mac ==>" + getLocalMacAddress());
	}
	
	
	private void init()
	{
		mChooseList = (ListView)findViewById(R.id.chooselist);
		mChooseAdapter = new ChooseAdapter();
		mChooseList.setAdapter(mChooseAdapter);
		mChooseAdapter.notifyDataSetChanged();
		mChooseList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				Intent intent = new Intent(MainActivity.this.getString(Util.intentActions[arg2][1]));
				MainActivity.this.startActivity(intent);
			}
			
		});
	}
	
	public class ChooseAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			return Util.intentActions.length;
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
			TextView nameTextView = null;
			if(convertView == null)
			{
				convertView = LinearLayout.inflate(MainActivity.this, R.layout.listitem, null);
			}
			nameTextView = (TextView) convertView.findViewById(R.id.name);
			nameTextView.setText(Util.intentActions[position][0]);
			return convertView;
		}
	}
	
	 public String getLocalMacAddress() 
    {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}
}
