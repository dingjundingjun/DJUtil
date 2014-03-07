package com.hanvon.core;

import java.io.IOException;

import com.hanvon.core.StrokeCollection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.widget.Toast;

public class StrokeSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {
	private SurfaceHolder holder;
	private SurfaceViewThread surfaceViewThread;
	private boolean hasSurface;
	
	//画笔迹的Bitmap
    private Bitmap  mBitmap;
    private Bitmap mDrawBitmap;
    private int[] mPixels;
    //背景像素数组
    private int[] mBackgroundPixels;   
    
    private byte[] mPixelsByte;
    private byte[] mBackgroundPixelsByte; 

    //画笔迹的Bitmap的高度与宽度
    private int mHeight;
    private int mWidth;    

    //包含所有笔迹的集合
    StrokeCollection mStrokes;
    
    //笔迹的三个属性：笔形，颜色，宽度
    int mPenStyle;
	int mColorType;
	int mPenWidth;

	StrokeSurfaceView(Context context, int width, int height) {
		super(context);
		init(width, height);
		refreshRect.set(0, 0, width, height);
		
		//resume();
	}

	// Implementing all callback methods below
	public void resume() {
		// Create and start the graphics update thread
		if (surfaceViewThread == null) {
			surfaceViewThread = new SurfaceViewThread();
			if (hasSurface)
				surfaceViewThread.start();
		}
	}

	public void pause() {
		// Stop the graphics update thread
		if (surfaceViewThread != null) {
			surfaceViewThread.requestExitAndWait();
			surfaceViewThread = null;
		}		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		hasSurface = true;

		if (surfaceViewThread != null)
			surfaceViewThread.start();
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
		pause();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if (surfaceViewThread != null)
			surfaceViewThread.onWindowResize(w, h);
	}

	private void init(int width, int height) {
		// Create a new SurfaceHolder and assign this class as its callback
		holder = getHolder();
		holder.addCallback(this);
		hasSurface = false;
		
		//设置 背景透明 
		//setZOrderOnTop(true); 
		//setEGLConfigChooser(8, 8, 8, 8, 16, 0); 
		//getHolder().setFormat(PixelFormat.TRANSLUCENT); 
		
        holder.setFormat(PixelFormat.RGB_565);
		
        mWidth = width;
        mHeight = height;
        
        if(mBitmap == null)
        {
        	try {
        		mBitmap = Bitmap.createBitmap( width, height, Bitmap.Config.RGB_565 );
        		mDrawBitmap = Bitmap.createBitmap( 50, 50, Bitmap.Config.ARGB_8888 );
            	mPixels = new int[width * height];            	
            }catch ( Exception e ) {
                //e.printStackTrace( );
                //Toast toast = Toast.makeText(c,"error", Toast.LENGTH_LONG);
        		//toast.setGravity(Gravity.CENTER, 0, 0);
        		//toast.show();
            }
        	mBackgroundPixels = new int[width * height];
        }
        
        //Canvas mCanvas = new Canvas(mBitmap);
        
        //构造背景
        mBitmap.eraseColor(Color.WHITE);
        //drawBackgroud(mBitmap, Color.GRAY); 
        
        mBitmap.getPixels( mPixels, 0, width, 0, 0, width, height );
        
        //save background        
        mBitmap.getPixels( mBackgroundPixels, 0, width, 0, 0, width, height );
    
        mStrokes = new StrokeCollection(width, height, mPixels, mBackgroundPixels);
        
        /* TEST */
        mPenStyle = 2; //钢笔
    	mColorType = 6;
    	mPenWidth = 5;
	}	
	
	boolean mIsMove = false;
	Point mLast = new Point();
	Rect refreshRect = new Rect();
	Rect refreshRect2 = new Rect();
	boolean isFirst = true;
	long first;
	Canvas canvas;
   
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int x = (int) event.getX();
		int y = (int) event.getY();

		float pressure = event.getPressure();
		
		
		// 画笔迹
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (!mIsMove) {
				mStrokes.addStroke(mPenStyle, mColorType, mPenWidth);
				mIsMove = true;
				mLast.set(x, y);
			}
			first = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_MOVE:
			//long second = System.currentTimeMillis();
			//Log.d("Move time", String.valueOf(second - first));
			first = System.currentTimeMillis();	
			refreshRect2.set(refreshRect);
			refreshRect = mStrokes.add(new Point(x, y), pressure);
			
			if (refreshRect.width() != 0 && refreshRect.height() != 0) {

				mBitmap.setPixels(mPixels, refreshRect.top * mWidth
						+ refreshRect.left, mWidth, refreshRect.left,
						refreshRect.top, refreshRect.width(),
						refreshRect.height());

				canvas = holder.lockCanvas(refreshRect);
				synchronized (holder) { 
				canvas.drawBitmap(mBitmap, 0, 0, null);
				}
				holder.unlockCanvasAndPost(canvas);
			}
			mLast.set(x, y);
			if(isDraw)
				refreshRect.union(refreshRect2);
			isDraw = true;
			
			break;
		case MotionEvent.ACTION_UP:	
			long second = System.currentTimeMillis();
			refreshRect = mStrokes.add(new Point(-100, -100), -100);
			isFirst = false;
			if (refreshRect.width() != 0 && refreshRect.height() != 0) {

				mBitmap.setPixels(mPixels, refreshRect.top * mWidth
						+ refreshRect.left, mWidth, refreshRect.left,
						refreshRect.top, refreshRect.width(),
						refreshRect.height());
				canvas = holder.lockCanvas(refreshRect);
				synchronized (holder) { 
				canvas.drawBitmap(mBitmap, 0, 0, null);
				}
				holder.unlockCanvasAndPost(canvas);			
			}
			mLast.set(-100, -100);
			isDraw = true;
			break;
		default:
			break;
		}
		return true;
	}
	
	public void clear()
	{
		mStrokes.clearBackground();
		
		Rect refreshRect = new Rect(0, 0, mWidth, mHeight);
		if (refreshRect.width() != 0 && refreshRect.height() != 0) {

			mBitmap.setPixels(mPixels, refreshRect.top * mWidth
					+ refreshRect.left, mWidth, refreshRect.left,
					refreshRect.top, refreshRect.width(),
					refreshRect.height());
			canvas = holder.lockCanvas(refreshRect);
			synchronized (holder) { 
			canvas.drawBitmap(mBitmap, 0, 0, null);
			}
			holder.unlockCanvasAndPost(canvas);			
		}
	}
    
	boolean isDraw = true;

	private final class SurfaceViewThread extends Thread {
		private boolean done;
		private Canvas mCanvas;

		SurfaceViewThread() {
			super();
			done = false;
		}

		@Override
		public void run() {
			SurfaceHolder surfaceHolder = holder;

			// Repeat the drawing loop until the thread is stopped
			while (!done) {				
				
				if (!isDraw)
					continue;

				long time = System.currentTimeMillis();
				Canvas canvas = null;

				try {
					synchronized (surfaceHolder) {
						canvas = surfaceHolder.lockCanvas();
						Log.d("Draw time", String.valueOf(System.currentTimeMillis() - time));
						canvas.drawBitmap(mBitmap, 0, 0, null);
					}

				} finally {
					if (canvas != null)
						surfaceHolder.unlockCanvasAndPost(canvas);
				}

				isDraw = false;
			}
		}

		public void requestExitAndWait() {
			// Mark this thread as complete and wait it to finish himself
			done = true;
			try {
				join();
			} catch (InterruptedException ignored) {
			}
		}

		public void onWindowResize(int w, int h) {
			// Deal with a change in the new surface size
		}
	}
}
