package com.dj.util;

import jding.debug.JDingDebug;
import android.content.Context;
import android.content.res.Configuration;
import android.view.Display;
import android.view.WindowManager;

public class Util
{
	/**屏幕宽度*/
	public static int DISPLAY_WIDTH;
	/**屏幕高度*/
	public static int DISPLAY_HEIGHT;
	public static int intentActions[][] =
	{
	{ R.string.describe_dragviews, R.string.action_dragviews },
	{ R.string.qrcode, R.string.action_qrcode },
	{ R.string.rotate_layout, R.string.action_rotate_layout },
	{ R.string.animation_grid, R.string.action_animation_grid }};
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
}
