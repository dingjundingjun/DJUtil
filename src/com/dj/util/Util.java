package com.dj.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import jding.debug.JDingDebug;
import android.content.Context;
import android.content.res.Configuration;
import android.view.Display;
import android.view.WindowManager;

public class Util
{
	private final static String TAG = "Util";
	/**屏幕宽度*/
	public static int DISPLAY_WIDTH;
	/**屏幕高度*/
	public static int DISPLAY_HEIGHT;
	public static int intentActions[][] =
	{
	{ R.string.describe_dragviews, R.string.action_dragviews },
	{ R.string.qrcode, R.string.action_qrcode },
	{ R.string.rotate_layout, R.string.action_rotate_layout },
	{ R.string.animation_grid, R.string.action_animation_grid },
	{ R.string.handwrite_recognition, R.string.action_handwrite_recognition }};
	/**
	 * 判断是横屏还是竖屏
	 * @param context
	 * @return true:横屏 false:竖屏
	 */
	public static boolean isLandScape(Context context) 
	{
		Configuration config = context.getResources().getConfiguration();
		if (config.orientation != Configuration.ORIENTATION_LANDSCAPE) 
		{
			return false;
		}
		return true;
	}
	
	/**
	 * 获取屏幕分辨率
	 * @param context
	 */
	public static void getDisplaySize(Context context)
	{
		Display display = ((WindowManager)context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		DISPLAY_WIDTH = display.getWidth();
		DISPLAY_HEIGHT = display.getHeight();
		JDingDebug.printfD("Util", "w=" + DISPLAY_WIDTH + " h=" + DISPLAY_HEIGHT);
	}
	
	/**
	 * 解析编码，解析出中文，存成列表
	 * @param b
	 * @param result
	 */
	public static void getUCSString(byte b[],List<String> result)
	{
		result.clear();
		byte[] tempB = new byte[2];
		StringBuffer resultStr = new StringBuffer();
		for(int i= 0;i<1024;i++)
		{
//			JDingDebug.printfD(TAG, "i:" + b[i] + " " + b[i+1]);
			String tempStr = "";
			if(i%2 == 0)
			{
				
				if(!(b[i] == 0 && b[i+1] == 0))
				{
					try
					{
						tempB[0] = b[i + 1];
						tempB[1] = b[i];
						tempStr = new String(tempB,"ucs-2");
						JDingDebug.printfD(TAG, "tempStr:" + tempStr);
						resultStr.append(tempStr);
					} catch (UnsupportedEncodingException e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					
					String str = new String(resultStr);
					JDingDebug.printfD(TAG, "str:" + str + " resultStr:" + resultStr);
					result.add(str);
					resultStr = new StringBuffer("");
				}
			}
			if(b[i] == 0 && b[i+1] == 0 && b[i+2] == 0 && b[i + 3] == 0)
			{
				break;
			}
		}
	}
}
