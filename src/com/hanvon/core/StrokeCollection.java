package com.hanvon.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class StrokeCollection {
	
	//每一页中都有一个StrokeCollection对象来统一管理笔迹
	
	public static final int FINGER = 0;
	public static final int PEN = 0;
	public static final int ERASER = 18;
	
	private HWCanvas mCanvas;
	private List<Stroke> mStrokes = new ArrayList<Stroke>();
	private Stroke current;
	
	private List<Point> mErasePts = new ArrayList<Point>();	
	private Rect mRect = new Rect();
	
	public StrokeCollection(int width, int height, int[] pixels, int[] backgroundPixels)
	{
		mCanvas = new HWCanvas(width, height, pixels, backgroundPixels);
		current = null;
	}
	
	public StrokeCollection(int width, int height, Bitmap pixels, int[] backgroundPixels)
	{
		mCanvas = new HWCanvas(width, height, pixels, backgroundPixels);
		current = null;
	}
	
	public StrokeCollection(int width, int height, byte[] pixels, byte[] backgroundPixels)
	{
		mCanvas = new HWCanvas(width, height, pixels, backgroundPixels);
		current = null;
	}
	
	public StrokeCollection(int width, int height, int[] pixels, int color)
	{
		mCanvas = new HWCanvas(width, height, pixels, color);
		current = null;
	}
	
	public HWCanvas getCanvas()
	{
		return mCanvas;
	}
	
	public List<Stroke> getStrokes()                                                                                                                                                                                               
	{
		return mStrokes;
	}
	
	public Rect addStroke(Stroke stroke){

		current = stroke;
		mCanvas.setPen(current.getHWPen());
		mStrokes.add(current);
		mCanvas.draw(stroke.getHWPath(), stroke.getHWPen(), false);
		return current.getBounds();
	}
	
	public void UpdateData()
	{
		if(current != null)
			current.UpdateData(mCanvas);
	}
	
    /**
     * 添加新的笔迹
     * 
     */
	public void addStroke(int penStyle, int penColor, int penWidth)
	{
		current = new Stroke(penStyle, penColor, penWidth);
		mCanvas.setPen(current.getHWPen());
		mStrokes.add(current);
	}
	
    /**
     * 添加新的笔迹：可以设置具体的颜色
     * 
     */
	public void addStroke(int penStyle, int penColor, int penWidth, boolean isColor)
	{
		current = new Stroke(penStyle, penColor, penWidth, isColor);
		mCanvas.setPen(current.getHWPen());
		mStrokes.add(current);
	}
	
    /**
     * 为当前笔迹添加新的点和压感值
     * 
     */	
	public Rect add(Point pt, float pressure)
	{
		if(current != null)
			return current.addPoint(mCanvas, pt, pressure);
		else
			return new Rect();
	}

    /**
     * 擦除笔迹
     * 
     */	
	public Rect erase(Point pt)
	{
		Rect rc = new Rect();
		
		int size = mErasePts.size();
		if(size == 0)
			mErasePts.add(pt);
		else
		{
			boolean isErase = false;			
			Point last = mErasePts.get(mErasePts.size() - 1);			
			
			if(pt.x == -1 && pt.y == -1)
			{
				if(size == 1)	
				{
					mRect.set(last.x, last.y, last.x, last.y);
					mRect.inset(-8, -8);
					isErase = true;
				}
				
				mErasePts.clear();
			}
			else
			{
				isErase = true;
				mRect.set(last.x, last.y, pt.x, pt.y);
				mRect.sort();
				mErasePts.add(pt);
			}

			if(isErase)
			{
				if(mRect.width()>0 || mRect.height()>0)
				{
					Stroke cur;
					boolean isFirst = true;
					long first = System.currentTimeMillis(); 
					mCanvas.setBackground();
					
			        for (int i = mStrokes.size() - 1; i>=0; i--)
			        {
			        	cur = mStrokes.get(i);
			        	if(cur.IsIn(mRect)) 
			        	{
			        		if(isFirst)
			        		{
			        			rc.set(cur.getBounds());
			        			isFirst = false;
			        		}
			        		else
			        			rc.union(cur.getBounds());
			        		
			        		//mCanvas.addCilpRect(cur.getBounds()); 
			        		cur.reDraw(mCanvas, true);
			        		mStrokes.remove(i);
			        	}
			        }
			        
			        long second = System.currentTimeMillis();
			        Log.d("find stroke", String.valueOf(second - first));	
			        
			        rc.setIntersect(rc, new Rect(0,0,mCanvas.getWidth(),mCanvas.getHeight()));
			        
			        if(!isFirst)
			        {
			        	//mCanvas.clearBackground();
			        	mCanvas.setCilpRect(rc); 
			            Iterator<Stroke> it = mStrokes.iterator();	
			        	 while (it.hasNext())
			             {
			        		 cur = it.next();
			             	if(Rect.intersects(rc, cur.getBounds()))
			             		cur.reDraw(mCanvas, false);
			             }
			        	 
			        	 mCanvas.reSet();
			        }
			        
			        Log.d("redraw", String.valueOf(System.currentTimeMillis() - second));
				}
			}
		}
		
		return rc;
	}
	
	public Rect reDraw()
	{
		Rect rc = new Rect();
		rc.set(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
		//mCanvas.clearBackground();
    	mCanvas.setCilpRect(rc); 
        Iterator<Stroke> it = mStrokes.iterator();	
    	 while (it.hasNext())
         {
         	Stroke cur = it.next();
         	//if(Rect.intersects(rc, cur.getBounds()))
         		cur.reDraw(mCanvas, false);
         }
    	 
    	 mCanvas.reSet();
    	 
    	 return rc;
	}
	
    /**
     * 清空所有笔迹
     * 
     */	
	public void clearBackground()
	{
		mStrokes.clear();
		mCanvas.clear();
	}
	
    /**
     * 换背景
     *  
     * @param width
     *            宽度
     * @param pen
     *            高度     
     * @param pixels
     *            当前操作的像素值数组
     * @param backgroundPixels
     *            所换背景像素值数组   
     */	
	public void clearBackground(int width, int height, int[] pixels, int[] backgroundPixels)
	{
		mCanvas.attach(width, height, pixels, backgroundPixels);
		Iterator<Stroke> it = mStrokes.iterator();

		Stroke cur = null;
		while (it.hasNext())
        {
        	cur = it.next();
        	cur.reDraw(mCanvas, false);
        }
	}
	
	public void clearBackground(int width, int height, byte[] pixels, byte[] backgroundPixels)
	{
		mCanvas.attach(width, height, pixels, backgroundPixels);
		Iterator<Stroke> it = mStrokes.iterator();

		Stroke cur = null;
		while (it.hasNext())
        {
        	cur = it.next();
        	cur.reDraw(mCanvas, false);
        }
	}
	
	public void clearBackground(int width, int height, Bitmap pixels, int[] backgroundPixels)
	{
		mCanvas.attach(width, height, pixels, backgroundPixels);
		Iterator<Stroke> it = mStrokes.iterator();

		Stroke cur = null;
		while (it.hasNext())
        {
        	cur = it.next();
        	cur.reDraw(mCanvas, false);
        }
	}
}
