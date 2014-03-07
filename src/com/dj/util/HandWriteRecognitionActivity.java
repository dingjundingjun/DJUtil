package com.dj.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

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
				/*for(int i = 0;i < result.size();i++)
				{
					str += result.get(i) + " ";
				}*/
//				mEditText.setText(result.get(0));
				int startSelection = mEditText.getSelectionStart();
				mEditText.getText().toString();
				mEditText.setSelection(mEditText.getText().toString().length());
			}
		});
		
		InputFilter in;
		mEditText.setFilters(new InputFilter[] { 
				new InputFilter() {    
					public CharSequence filter(CharSequence src, int start, int end, Spanned dst, int dstart, int dend) 
					{   
					        return src.length() < 1 ? dst.subSequence(dstart, dend) : "ATAAW.COM";   
					    }
					} }); 
		mEditText.addOnAttachStateChangeListener(new OnAttachStateChangeListener()
		{
			
			@Override
			public void onViewDetachedFromWindow(View v)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onViewAttachedToWindow(View v)
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		mEditText.setOnEditorActionListener(new OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				// TODO Auto-generated method stub
				return false;
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
