package com.hanvon.core;

import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;

public class Stroke {
	
	private HWPen mPen;
	private HWPath mPath;
	
    /**
     * 构造函数
     * 说明：一般是新生成一条笔迹中使用
     * 
     * @param nColorType
     *        只代表颜色类型ID
     */
	public Stroke(int penStyle, int penColor, int penWidth)
	{
		mPen = new HWPen(penStyle, penColor, penWidth);
		mPath = new HWPath();		
	}
	
    /**
     * 构造函数
     * 说明：一般是新生成一条笔迹中使用
     * 
     * @param nColorType
     *        当 isColor = false，代表颜色类型ID，当 isColor = true 时，代表真正的颜色值
     */
	public Stroke(int penStyle, int penColor, int penWidth, boolean isColor)
	{
		mPen = new HWPen(penStyle, penColor, penWidth, isColor);
		mPath = new HWPath();		
	}
	
    /**
     * 构造函数
     * 说明：一般是保存后打开或者上一页下一页切换中使用
     * 
     * @param nPenStyle
     *            画笔风格　在不反走样时生效,1=铅笔, 2=钢笔,　3=毛笔, 4=蜡笔
     * @param nColorType
     *            颜色类型　( 0-蓝色，1-绿色，2-青色，3-红色，4-粉红色，5-黄色，6-黑色，7-深蓝色
     *            8-橄榄色，9-浅蓝色，10-栗色，11-紫色，12-暗黄色，13-银灰色，14-黑灰色
     *            100～112-渐变1～渐变13 )
     * @param nPenWidth
     *            画笔宽度[1, 5]，值越大笔形越粗
     * @param pts
     *            点坐标
     * @param prs
     *            压感
     */
	public Stroke(int penStyle, int penColor, int penWidth, List<Point> pts, List<Float> prs)
	{
		mPen = new HWPen(penStyle, penColor, penWidth);
		mPath = new HWPath(pts, prs);		
	}
	
	public Stroke(int penStyle, int penColor, int penWidth, List<Point> pts, List<Float> prs, boolean isColor)
	{
		mPen = new HWPen(penStyle, penColor, penWidth, isColor);
		mPath = new HWPath(pts, prs);		
	}
	
	public HWPen getHWPen()
	{
		return mPen;
	}
	
	public HWPath getHWPath()
	{
		return mPath;
	}
	
	public Rect getBounds()
	{
		return mPath.getBounds();
	}	
	
	public void UpdateData(HWCanvas canvas)
	{
		mPath.UpdateData(canvas);
	}
	
	public boolean IsIn(Rect rc)
	{
		return mPath.IsOutlineVisible(rc, mPen);
	}

	public Rect addPoint(HWCanvas canvas, Point pt, float pressure)
	{
		if(canvas != null)
			return canvas.draw(mPath, pt, pressure);
		else
			return new Rect(0,0,0,0);
	}
	
	public void reDraw(HWCanvas canvas, boolean isErase)
	{
		if(canvas != null)
			canvas.draw(mPath, mPen, isErase);
	}
}
