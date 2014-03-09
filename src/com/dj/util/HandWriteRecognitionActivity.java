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
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.dj.util.views.ExEditText;
import com.dj.util.views.HandWriteRecognitionView;
import com.hanvon.core.StrokeView.RecognitionListerner;

public class HandWriteRecognitionActivity extends Activity implements OnClickListener 
{
	private final String TAG = "HandWriteRecognitionActivity";
	private HandWriteRecognitionView mHandWriteRecognitionView;
	private Button mBtnClear;
	private Button mBtnTest;
	private Button mBtnEdit;
	private Button mBtnDelete;
	private Button mBtnHandWrite;
	private ExEditText mEditText;
	private List<String> mResult = new ArrayList<String>();
	private InputConnection mInputConnection;
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
		mEditText = (ExEditText)findViewById(R.id.edittext);
		mBtnEdit = (Button)findViewById(R.id.btn_edit_normal);
		mBtnHandWrite = (Button)findViewById(R.id.btn_handwrite);
		mBtnDelete = (Button)findViewById(R.id.btn_delete);
		mBtnClear.setOnClickListener(this);
		mBtnTest.setOnClickListener(this);
		mBtnEdit.setOnClickListener(this);
		mBtnHandWrite.setOnClickListener(this);
		mBtnDelete.setOnClickListener(this);
		mHandWriteRecognitionView = (HandWriteRecognitionView)findViewById(R.id.handwriterecognition);
		mHandWriteRecognitionView.init(this);
		mHandWriteRecognitionView.setEditText(mEditText);
		mHandWriteRecognitionView.setRecognitionListener(new RecognitionListerner()
		{
			@Override
			public void onRecognitionResult(List<String> result)
			{
//				mEditText.setText(result.get(0));
				mEditText.addString(result.get(0));
				mEditText.setSelection(mEditText.getSelectionStart());
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
			break;
		}
		case R.id.btn_edit_normal:
		{
			mHandWriteRecognitionView.setVisibility(View.GONE);
			break;
		}
		case R.id.btn_handwrite:
		{
			mHandWriteRecognitionView.setVisibility(View.VISIBLE);
			break;
		}
		case R.id.btn_delete:
		{
			mEditText.deleteEditValue(mEditText.getEditSelection());
			break;
		}
		}
	}
}
