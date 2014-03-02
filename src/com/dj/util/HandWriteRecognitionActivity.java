package com.dj.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.dj.util.views.HandWriteRecognitionView;
import com.hanvon.core.StrokeView.RecognitionListerner;

public class HandWriteRecognitionActivity extends Activity implements OnClickListener 
{
	private final String TAG = "HandWriteRecognitionActivity";
	private HandWriteRecognitionView mHandWriteRecognitionView;
	private Button mBtnClear;
	private Button mBtnTest;
	private EditText mEditText;
	private List<String> mResult = new ArrayList<String>();
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
		mHandWriteRecognitionView.setRecognitionListener(new RecognitionListerner()
		{
			@Override
			public void onRecognitionResult(List<String> result)
			{
				mEditText.setText("");
				String str = "";
				for(int i = 0;i < result.size();i++)
				{
					str += result.get(i) + " ";
				}
				mEditText.setText(str);
			}
		});
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
			Util.getUCSString(mHandWriteRecognitionView.test(),mResult);
			String str = "";
			for(int i = 0;i < mResult.size();i++)
			{
				str += mResult.get(i) + " ";
			}
			mEditText.setText(str);
		}
		}
	}
}
