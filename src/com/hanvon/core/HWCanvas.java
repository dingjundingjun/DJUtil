package com.hanvon.core;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;

public class HWCanvas {
	
	private int[] mRect = new int[4]; 
	private int[] mBackgroundPixels;
	private byte[] mBackgroundPixelsByte;
	private int mBackgroundColor;
	
	private int mWidth;
	private int mHeight;
	
	private float[] mPoints = new float[2048];
	private float[] mPoints2 = new float[2048];
	private boolean mUsed = true;
	
	private HWPen mPen;
	private boolean mIsSucsess;
	
	private float[] mCurrentPoints = null;
	
    /**
     * 构造函数
     * 
     * @param width
     *            宽度
     * @param pen
     *            高度     
     * @param pixels
     *            当前操作的像素值数组
     * @param backgroundPixels
     *            背景像素值     
     */
	public HWCanvas(int width, int height, int[] pixels, int[] backgroundPixels)
	{
		mBackgroundPixels = backgroundPixels;
		mBackgroundColor = Color.WHITE;
		mPen = new HWPen(0, 0, 0);
		
		mWidth = width;
		mHeight = height;
		
		mBackgroundPixelsByte = null;
		
		mIsSucsess = HWColorPaint.initializeEx(width, height, pixels);
		//mIsSucsess = false;
	}
	
	public HWCanvas(int width, int height, byte[] pixels, byte[] backgroundPixels)
	{
		mBackgroundPixelsByte = backgroundPixels;
		mBackgroundColor = Color.WHITE;
		mPen = new HWPen(0, 0, 0);
		
		mWidth = width;
		mHeight = height;
		
		mBackgroundPixels = null;
		
		mIsSucsess = HWColorPaint.initializeExByte(width, height, pixels);
	}
	
	public HWCanvas(int width, int height, Bitmap pixels, int[] backgroundPixels)
	{
		mBackgroundPixels = backgroundPixels;
		mBackgroundColor = Color.WHITE;
		mPen = new HWPen(0, 0, 0);
		
		mWidth = width;
		mHeight = height;
		
		mBackgroundPixelsByte = null;
		
		mIsSucsess = HWColorPaint.initializeBitmap(pixels);
	}
	
	public void attach(int width, int height, Bitmap pixels, int[] backgroundPixels)
	{		
		mWidth = width;
		mHeight = height;
		
		mBackgroundPixelsByte = null;
		mBackgroundPixels = backgroundPixels;
		mIsSucsess = HWColorPaint.initializeBitmap(pixels);
	}

	public void attach(int width, int height, int[] pixels, int[] backgroundPixels)
	{		
		mWidth = width;
		mHeight = height;
		
		mBackgroundPixelsByte = null;
		mBackgroundPixels = backgroundPixels;
		mIsSucsess = HWColorPaint.initializeEx(width, height, pixels);
	}
	
	public void attach(int width, int height, byte[] pixels, byte[] backgroundPixels)
	{		
		mWidth = width;
		mHeight = height;
		
		mBackgroundPixelsByte = backgroundPixels;
		mBackgroundPixels = null;
		mIsSucsess = HWColorPaint.initializeExByte(width, height, pixels);
	}
	
	public HWCanvas(int width, int height, int[] pixels, int color)
	{
		mBackgroundPixels = null;
		mBackgroundPixelsByte = null;
		mBackgroundColor = color;
		mPen = new HWPen(0, 0, 0);
		
		mWidth = width;
		mHeight = height;
		
		mIsSucsess = HWColorPaint.initializeEx(width, height, pixels);
	}
	
	public int getWidth()
	{
		return mWidth;
	}
	
	public int getHeight()
	{
		return mHeight;
	}
	
	public void setPen(HWPen pen)
	{
		if(!mIsSucsess)
			return;
		
		//if(!(mPen.getStyle() == pen.getStyle() 
		//		&& mPen.getColor() == pen.getColor()
		//		&& mPen.getWidth() == pen.getWidth()))
		{
			HWColorPaint.setPen(3, pen.getStyle(), pen.getColor(), pen.getWidth(), 12 ); 
			if(pen.getIsColor())
				HWColorPaint.setPenColor(pen.getColors());
			mPen.set(pen);
		}
	}

    /**
     * 清空画布所画的东西
     * 
     */
	public void clear()
	{
		if(!mIsSucsess)
			return;
		
		addCilpRect(new Rect(0, 0, mWidth, mHeight));  
		clearBackground();
		reSet();
	}

	public void clearBackground()
	{
		if(!mIsSucsess)
			return;
		
		if(mBackgroundPixels != null)
			HWColorPaint.clearBackground(mBackgroundPixels);
		else if(mBackgroundPixelsByte != null)
			HWColorPaint.clearBackgroundByte(mBackgroundPixelsByte);
		else
			HWColorPaint.clear(mBackgroundColor);
	}
	
	public void setBackground()
	{
		if(!mIsSucsess)
			return;
		
		if(mBackgroundPixels != null)
			HWColorPaint.setBackground(mBackgroundPixels);
		else
			HWColorPaint.clear(mBackgroundColor);
	}
	
	public void addCilpRect(Rect rc)
	{
		if(!mIsSucsess)
			return;
		
		mRect[0] = rc.left;
		mRect[1] = rc.top;
		mRect[2] = rc.right;
		mRect[3] = rc.bottom;	
		
		HWColorPaint.addClipRegion(mRect);
	}
	
	public void setCilpRect(Rect rc)
	{
		if(!mIsSucsess)
			return;
		
		mRect[0] = rc.left;
		mRect[1] = rc.top;
		mRect[2] = rc.right;
		mRect[3] = rc.bottom;	
		
		HWColorPaint.setClipRegion(mRect);
	}
	
	public void reSet()
	{
		if(!mIsSucsess)
			return;
		
		HWColorPaint.reSet();
	}
	
    /**
     * 为指定HWPath绘制一小段笔迹
     * 说明：一般是在MouseMove过程中调用
     * 
     * @param path
     *            指定的HWPath
     * 
     * @param pt
     *            采样点坐标
     * 
     * @param pressure
     *            当前采样点 压感值
     *           
     * @return    此次对像素点的操作矩阵区域
     */
	public Rect draw(HWPath path, Point pt, float pressure)
	{
		Rect rc = new Rect();	
		
		if(!mIsSucsess)
			return rc;
		
		//HWColorPen.drawLine(pt.x, pt.y, pressure, mRect, mPixels);
		float[] points = mPoints;
		if(mUsed)
			mUsed = false;
		else
		{
			points = mPoints2;
			mUsed = true;
		}
		if(points != null && points.length > 0)
			points[0] = 0;
		
		HWColorPaint.drawLineEx(pt.x, pt.y, pressure, mRect, points);
		//path.addData(points);
		path.add(pt, pressure);
		mCurrentPoints = points;
		
		if(mRect[0]<0)
			mRect[0] = 0;
		if(mRect[1]<0)
			mRect[1] = 0;
		if(mRect[2] >= mWidth)
			mRect[2] = mWidth - 1;
		if(mRect[3] >= mHeight)
			mRect[3] = mHeight - 1;
		if(mRect[2]<0)
			mRect[2] = 0;
		if(mRect[3]<0)
			mRect[3] = 0;
		if(mRect[0] >= mWidth)
			mRect[0] = mWidth - 1;
		if(mRect[1] >= mHeight)
			mRect[1] = mHeight - 1;
		path.updateBounds(mRect);
		rc.set(mRect[0], mRect[1], mRect[2], mRect[3]);
		
		return rc;
	}
	
	public void UpdateData(HWPath path)
	{
		if(mCurrentPoints != null)
			path.addData(mCurrentPoints);
	}
	
    /**
     * 重绘HWPath函数
     * 说明：一般是在笔迹擦擦除过程中笔迹重绘使用
     * 
     * @param path
     *            包含所绘笔迹信息
     * 
     * @param pen
     *            指定的HWPath         
     */
	public void draw(HWPath path, HWPen pen, boolean isErase)
	{
		if(!mIsSucsess)
			return;
		
		setPen(pen);
		//HWColorPen.reDrawLine(path.PointData(), path.PressureData(), path.PointData().length, mPixels);
		if(path.IsInterpolate())
			interpolate(path, isErase);
		else
			HWColorPaint.reDrawLineEx(path.PathData(), path.PathData().length, isErase);
	}
	
	private void interpolate(HWPath path, boolean isErase)
	{
		int[] pRect = new int[4];
		float[] pathData = new float[path.PressureData().length * 100];
		if(pathData != null && pathData.length > 0)
			pathData[0] = 0;
		HWColorPaint.interpolate(path.PointData(), path.PressureData(), pRect, pathData, isErase);
		path.updatePathData(pathData, pRect);
	}
}
