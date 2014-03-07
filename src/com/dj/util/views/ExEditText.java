package com.dj.util.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

public class ExEditText extends EditText
{
	private boolean bHandWrite;
	private InputConnection mInputConnection;
	public ExEditText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) 
	{
		if(bHandWrite)
		{
			mInputConnection = new MyConnection(this, false);
		}
		return mInputConnection;
	}
	
public class MyConnection extends BaseInputConnection 
{
		public MyConnection(View targetView, boolean fullEditor) {
			super(targetView, fullEditor);
		}

		@Override
		public boolean commitText(CharSequence text, int newCursorPosition) {
			
			return super.commitText(text, newCursorPosition);
		}
	}
}
