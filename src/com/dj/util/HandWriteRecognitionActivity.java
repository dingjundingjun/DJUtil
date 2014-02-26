package com.dj.util;

import java.io.UnsupportedEncodingException;

import jding.debug.JDingDebug;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.dj.util.views.HandWriteRecognitionView;

public class HandWriteRecognitionActivity extends Activity implements OnClickListener
{
	private final String TAG = "HandWriteRecognitionActivity";
	private HandWriteRecognitionView mHandWriteRecognitionView;
	private Button mBtnClear;
	private Button mBtnTest;
	private EditText mEditText;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.handwrite_recognition_layout);
		init();
	}
	
	private void init()
	{
		mBtnClear = (Button)findViewById(R.id.btn_clear);
		mBtnTest = (Button)findViewById(R.id.btn_test);
		mEditText = (EditText)findViewById(R.id.edittext);
		mBtnClear.setOnClickListener(this);
		mBtnTest.setOnClickListener(this);
		
		mHandWriteRecognitionView = (HandWriteRecognitionView)findViewById(R.id.handwriterecognition);
		mHandWriteRecognitionView.init(this);
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
		case R.id.btn_clear:
		{
			mHandWriteRecognitionView.clear();
			break;
		}
		case R.id.btn_test:
		{
			byte[] b = mHandWriteRecognitionView.test();
			for(int i= 0;i<1024;i++)
			{
				JDingDebug.printfD(TAG, "i:" + b[i]);
				if(b[i] == 0 && b[i+1] == 0)
				{
					break;
				}
			}
			
			String result = "";
			try
			{
				result = new String(b, "ucs-2");
			} catch (UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mEditText.setText(result);
		}
		}
	}
}
