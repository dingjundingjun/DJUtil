package com.hanvon.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dj.util.Util;
import com.hanvon.core.HWColorPaint;
import com.hanvon.core.StrokeCollection;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class StrokeView extends View
{
	private final String TAG = "StrokeView";
	//画笔迹的Bitmap
    private Bitmap  mBitmap;
    private int[] mPixels;
    //背景像素数组
    private int[] mBackgroundPixels;   
    
    //画笔迹的Bitmap的高度与宽度
    private int mHeight;
    private int mWidth;    

    //包含所有笔迹的集合
    StrokeCollection mStrokes;
    
    //笔迹的三个属性：笔形，颜色，宽度
    int mPenStyle;
	int mColorType;
	int mPenWidth;
	
	//笔迹是否需要取压感值，如果没有就取1
	boolean mHasPressure = true;
	//控制是擦除还是画笔迹
	boolean mIsStroke = true;
	
	boolean mIsMove = false;

	Paint mPaint = new Paint();
	Canvas mCanvas;
	Rect refreshRect;
    
	/**手写识别*/
    private short[] mRecognition = new short[2048];
    private int mRecognition_index = 0;
    private Context mContext;
    private RecognitionHandler mRecognitionHandler = new RecognitionHandler();
    private final int HANDLER_TO_RECONGNITION = 0;
    private final int RECONGNITION_SLEEP = 1000;
    private RecognitionListerner mRecognitionListerner;
    private List<String> mResult = new ArrayList<String>();
	public StrokeView(Context c, int width, int height) {
        super(c);
        mContext = c;
		//int width = 1024, height = 600;
        mWidth = width;
        mHeight = height;
        
        if(mBitmap == null)
        {
        	try {
//        		mBitmap = Bitmap.createBitmap( width, height, Bitmap.Config.RGB_565 );
        		mBitmap = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888 );
            	mPixels = new int[width * height];            	
            }catch ( Exception e ) {
                //e.printStackTrace( );
                Toast toast = Toast.makeText(c,"error", Toast.LENGTH_LONG);
        		toast.setGravity(Gravity.CENTER, 0, 0);
        		toast.show();
            }
        	mBackgroundPixels = new int[width * height];
        }
        
        mCanvas = new Canvas(mBitmap);
        
        //构造背景
        mBitmap.eraseColor(Color.TRANSPARENT);
//        mBitmap.eraseColor(Color.WHITE);
//        drawBackgroud(mBitmap, Color.GRAY);      
//        this.setBackgroundColor(Color.WHITE);
        
        mBitmap.getPixels( mPixels, 0, width, 0, 0, width, height );
        
        //save background        
        mBitmap.getPixels( mBackgroundPixels, 0, width, 0, 0, width, height );
        
        mStrokes = new StrokeCollection(width, height, mPixels, mBackgroundPixels);
  

        /* TEST */
        mPenStyle = 2; //钢笔
    	mColorType = 7;
    	mPenWidth = 5;

    	mPaint.setAntiAlias(true);
    	//mPaint.setDither(true);
    	//mPaint.setFlags (Paint.ANTI_ALIAS_FLAG);			
    	mPaint.setStrokeJoin(Paint.Join.ROUND);
    	mPaint.setStrokeCap(Paint.Cap.ROUND);
    	mPaint.setStyle(Paint.Style.STROKE);			
    	mPaint.setStrokeWidth(10.0f);  	
    	
    }
    
    public void Destroy()
    {
    	if(mBitmap!=null)
    		mBitmap.recycle();
    	
    	mBitmap = null;
    }
  
    @Override
    protected void onDraw(Canvas canvas) { 
    	    	
    	if(mBitmap != null)
    		canvas.drawBitmap(mBitmap, 0, 0, null);
    }

	@SuppressLint("NewApi")
	@TargetApi(14)
	@Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();   
       
        //getMetaState()的值针对A118
        if (event.getToolType(0) != MotionEvent.TOOL_TYPE_ERASER && mIsStroke) {
			float pressure = 0.8f;

			if(event.getMetaState() == StrokeCollection.PEN)
				pressure = event.getPressure();
			//Android4.0调用，判断第一个触控点类型，然后获取相应压感即可
			if(event.getToolType(0) == MotionEvent.TOOL_TYPE_STYLUS)
			{
				pressure = event.getPressure();
			}
			if (event.getToolType(0) == MotionEvent.TOOL_TYPE_ERASER)
			{
				pressure = event.getPressure();
			}
			// 画笔迹
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!mIsMove) {
					mRecognitionHandler.removeMessages(HANDLER_TO_RECONGNITION);
					if(mRecognition_index >= 2047)
					{
						mRecognitionHandler.sendEmptyMessageDelayed(
								HANDLER_TO_RECONGNITION, RECONGNITION_SLEEP);
						break;
					}
					mRecognition[mRecognition_index++] = (short) x;
					mRecognition[mRecognition_index++] = (short) y;
					//调用方式二：mStrokes.addStroke(mPenStyle, Color.argb(255, 0, 0, 0), mPenWidth, true);
					mStrokes.addStroke(mPenStyle, mColorType, mPenWidth, false);
					mIsMove = true;					
				}
				break;
			case MotionEvent.ACTION_MOVE:	
				refreshRect = mStrokes.add(new Point(x, y), pressure);
				if(mRecognition_index >= 2047)
				{
					mRecognitionHandler.sendEmptyMessageDelayed(
							HANDLER_TO_RECONGNITION, RECONGNITION_SLEEP);
					break;
				}
				mRecognition[mRecognition_index++] = (short) x;
				mRecognition[mRecognition_index++] = (short) y;
				if (refreshRect.width() != 0 && refreshRect.height() != 0) {
					
					mBitmap.setPixels(mPixels, refreshRect.top * mWidth
							+ refreshRect.left, mWidth,
							refreshRect.left, refreshRect.top,
							refreshRect.width(), refreshRect.height());
					
					invalidate(refreshRect);
				}
				break;
			case MotionEvent.ACTION_UP:
				// 笔迹结束的时候，传（-100，-100）通知结束
				if(mRecognition_index >= 2047)
				{
					mRecognitionHandler.sendEmptyMessageDelayed(
							HANDLER_TO_RECONGNITION, RECONGNITION_SLEEP);
					break;
				}
				mRecognition[mRecognition_index++] = -1;
				mRecognition[mRecognition_index++] = 0;
/*				mRecognition[mRecognition_index++] = -1;
				mRecognition[mRecognition_index++] = -1;*/
				
				refreshRect = mStrokes.add(new Point(-100, -100), -100f);
				if (refreshRect.width() != 0 && refreshRect.height() != 0) {

					mBitmap.setPixels(mPixels, refreshRect.top * mWidth
							+ refreshRect.left, mWidth,
							refreshRect.left, refreshRect.top,
							refreshRect.width(), refreshRect.height());
					
					invalidate(refreshRect);
				}
				mIsMove = false;
					mRecognitionHandler.sendEmptyMessageDelayed(
							HANDLER_TO_RECONGNITION, RECONGNITION_SLEEP);
				break;
			default:
				break;
			}
		} else {
			// 擦除
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				refreshRect = mStrokes.erase(new Point(x, y));
				if (refreshRect.width() != 0 && refreshRect.height() != 0) {
					mBitmap.setPixels(mPixels, refreshRect.top * mWidth
							+ refreshRect.left, mWidth,
							refreshRect.left, refreshRect.top,
							refreshRect.width(), refreshRect.height());
					invalidate(refreshRect);
				}
				break;
			case MotionEvent.ACTION_UP:
				// 此次擦除完成，也需要传（-1，-1）通知结束
				refreshRect = mStrokes.erase(new Point(-1, -1));
				if (refreshRect.width() != 0 && refreshRect.height() != 0) {
					mBitmap.setPixels(mPixels, refreshRect.top * mWidth
							+ refreshRect.left, mWidth,
							refreshRect.left, refreshRect.top,
							refreshRect.width(), refreshRect.height());
					invalidate(refreshRect);
				}
				break;
			default:
				break;
			}
		}
        
        return true;
    }
    
    public void reDraw()
    {
    	Rect refreshRect = mStrokes.reDraw();
    	
    	if(refreshRect.width() != 0 && refreshRect.height()!=0)
    	{
    		mBitmap.setPixels( mPixels, refreshRect.top * mWidth + refreshRect.left, mWidth, 
        			refreshRect.left, refreshRect.top, refreshRect.width(), refreshRect.height());
        	invalidate(refreshRect);
    	}
    }
    
    public void setTestDistance()
    {
    }
    
	public void erase() {
		//切换擦除和画笔迹
		if(mIsStroke)
			mIsStroke = false;
		else
			mIsStroke = true;
	}

	public void updateColor() {
		++mColorType;
		if (mColorType > 14 && mColorType < 100) {
			mColorType = 100;
		} else if (mColorType > 112) {
			mColorType = 0;
		}
		mIsStroke = true;
	}

	public void updateWidth() {
		mPenWidth = (mPenWidth + 1) % 6;
		if(mPenWidth == 0)
			mPenWidth = 1;
		mIsStroke = true;
	}

	public void updateStyle() {
		mPenStyle = (mPenStyle + 1) % 5;
		if(mPenStyle == 0)
			mPenStyle = 1;
		mIsStroke = true;
	}
	
	public void updatePressure() {
		if(mHasPressure)
			mHasPressure = false;
		else
			mHasPressure=true;
		mIsStroke = true;
	}
	
	public void clear() 
	{
		//清空所有笔迹
		mStrokes.clearBackground();
		
		Rect refreshRect = new Rect(0, 0, mWidth, mHeight);
		if(refreshRect.width() != 0 && refreshRect.height()!=0)
    	{
    		mBitmap.setPixels( mPixels, refreshRect.top * mWidth + refreshRect.left, mWidth, 
        			refreshRect.left, refreshRect.top, refreshRect.width(), refreshRect.height());
        	invalidate(refreshRect);
    	}
	}
	
	boolean isChange = true;
	public void updateBackground() 
	{
		 mBitmap.eraseColor(Color.WHITE);

		 if(isChange)
		 {
			 drawBackgroud(mBitmap, Color.BLUE);
			 isChange = false;
		 }
		 else
		 {
			 drawBackgroud(mBitmap, Color.GRAY);
			 isChange = true;
		 }
         
         mBitmap.getPixels( mPixels, 0, mWidth, 0, 0, mWidth, mHeight );
         mBitmap.getPixels( mBackgroundPixels, 0, mWidth, 0, 0, mWidth, mHeight );
         
         
         Rect refreshRect = new Rect(0, 0, mWidth, mHeight);
 		if(refreshRect.width() != 0 && refreshRect.height()!=0)
     	{
     		mBitmap.setPixels( mPixels, refreshRect.top * mWidth + refreshRect.left, mWidth, 
         			refreshRect.left, refreshRect.top, refreshRect.width(), refreshRect.height());
         	invalidate(refreshRect);
     	}
	}
	
	private void drawBackgroud(Bitmap bitmap, int color)
	{
		Canvas canvas = new Canvas(bitmap);
		
		int width = canvas.getWidth();
    	int height = canvas.getHeight();
    	
    	Path path = new Path(); 
    	
    	float cmUnit = 4 * 10;
    	
    	int distance = (int)(width/cmUnit) + 1;
    	for(int i = 1; i < distance; i++)
    	{
    		path.moveTo((int)(i*cmUnit), 0);
    		path.lineTo((int)(i*cmUnit), height);
    	}
    	
    	distance= (int)(height/cmUnit) + 1;
    	for(int i = 1; i < distance; i++)
    	{
    		path.moveTo(0, (int)(i*cmUnit));
    		path.lineTo(width, (int)(i*cmUnit));
    	}
    	
    	Paint paint = new Paint();
    	paint.setAntiAlias(true);
    	paint.setColor(color);
    	paint.setAlpha(255);
    	paint.setStyle(Paint.Style.STROKE);
    	
    	DashPathEffect mEffects =  new DashPathEffect(new float[] {5, 5, 5, 5}, 1); 
    	paint.setPathEffect(mEffects);
    	paint.setStrokeWidth(1.0f);

		canvas.drawPath(path, paint);
	}
	
	
	private void int2bytes(int[] ints, byte[] bytes) {
		for (int i = 0; i < ints.length; i++) {
			bytes[4 * i] = (byte) (ints[i] >>> (24 - 0 * 8));
			bytes[4 * i + 1] = (byte) (ints[i] >>> (24 - 1 * 8));
			bytes[4 * i + 2] = (byte) (ints[i] >>> (24 - 2 * 8));
			bytes[4 * i + 3] = (byte) (ints[i] >>> (24 - 3 * 8));
		}
	}

	private void bytes2ints(int[] ints, byte[] bytes) {
		for (int i = 0; i < ints.length; i++) {
			ints[i] = 0;
			ints[i] <<= 8;
			ints[i] |= (bytes[4 * i] & 0x000000ff);
			ints[i] <<= 8;
			ints[i] |= (bytes[4 * i + 1] & 0x000000ff);
			ints[i] <<= 8;
			ints[i] |= (bytes[4 * i + 2] & 0x000000ff);
			ints[i] <<= 8;
			ints[i] |= (bytes[4 * i + 3] & 0x000000ff);
		}
	}

	public void saveMyBitmap(String bitName) throws IOException {
		File f = new File("/sdcard/movie/" + bitName + ".png");
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static
	{
		System.loadLibrary("handwrite");
	}
	
	public byte[] nativeRecognition()
	{
//		for(int i = 0; i < mRecognition_index + 1;i ++)
//		{
//			Log.d("nativeRecognition", "" + mRecognition[i]);
//			if(mRecognition[i] == -1 && mRecognition[i + 1] == -1)
//		}
		return recognition(mRecognition);
	}
	public native byte[] recognition(short p[]);
	
	/**1秒无超做则判断为需要识别*/
	public class RecognitionHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch(msg.what)
			{
				case HANDLER_TO_RECONGNITION:
				{
					Log.d(TAG, "the handwrite will to recognition ~~");
					mRecognition[mRecognition_index++] = -1;
					mRecognition[mRecognition_index++] = -1;
					mRecognition_index = 0;
					clear();
					toRecognition();
					mRecognitionListerner.onRecognitionResult(mResult);
					break;
				}
			}
		}
	}
	
	public void setRecognitionListerner(RecognitionListerner listener)
	{
		mRecognitionListerner = listener;
	}
	
	public interface RecognitionListerner
	{
		public void onRecognitionResult(List<String> result);
	}
	
	public void toRecognition()
	{
		Util.getUCSString(recognition(mRecognition),mResult);
		/*String str = "";
		for(int i = 0;i < mResult.size();i++)
		{
			str += mResult.get(i) + " ";
		}*/
	}
}
