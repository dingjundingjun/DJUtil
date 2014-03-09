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
	private boolean bHandWrite = true;

	public ExEditText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
	}

	// 往文本框中添加内容 
    public void addString(String sequence) {
        int index = getEditSelection();// 光标的位置
        if (index < 0 || index >= getEditTextViewString().length()) {
            append(sequence);
        } else {
            getEditableText().insert(index, sequence);// 光标所在位置插入文字
        }
    }
 
    // 获取光标当前位置
    public int getEditSelection() {
        return getSelectionStart();
    }
 
    // 获取文本框的内容
    public String getEditTextViewString() {
        return getText().toString();
    }
 
    // 清除文本框中的内容
    public void clearText() {
        getText().clear();
    }
 
    // 删除指定位置的字符
    public void deleteEditValue(int index) {
        getText().delete(index - 1, index);
    }
 
    // 设置光标位置
    public void setEditSelectionLoc(int index) {
        setSelection(index);
    }
 
    // 判断是否是数字
    public static boolean isNum(String str) {
        return str.matches("([0-9]+)?)$");
    }
}
