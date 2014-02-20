package com.dj.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;

public class RotateActivity extends Activity
{
	private MyOrientationEventListener mMyOrientationEventListener;
	private AnimationLayout mView;
	private String TAG = "RotateActivity";
	int mOrientation = 0;
	int mOrientationCompensation = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rotate);
		mView = (AnimationLayout) findViewById(R.id.testlayout);
		mView.init(this);
		mView.setDuration(30);
		mMyOrientationEventListener = new MyOrientationEventListener(this);
		mMyOrientationEventListener.enable();
	}
	
	 private class MyOrientationEventListener
     extends OrientationEventListener {
     public MyOrientationEventListener(Context context) {
         super(context);
     }

     @Override
         public void onOrientationChanged(int orientation)
     {
         // We keep the last known orientation. So if the user first orient
         // the camera then point the camera to floor or sky, we still have
         // the correct orientation.
         if (orientation == ORIENTATION_UNKNOWN) {
//             mOrientation = 0;
         	return;
         }
         Log.d(TAG, "" + orientation);
         mOrientation = roundOrientation(orientation, mOrientation);
        
         // When the screen is unlocked, display rotation may change. Always
         // calculate the up-to-date orientationCompensation.
         int orientationCompensation = (mOrientation + 90) % 360;
         if (mOrientationCompensation != orientationCompensation) 
         {
             mOrientationCompensation = orientationCompensation;
             mView.rotateDegree(mOrientationCompensation);
         }
     }
	 }
	 
	 public static int roundOrientation(int orientation, int orientationHistory) {
	        boolean changeOrientation = false;
	        if (orientationHistory == OrientationEventListener.ORIENTATION_UNKNOWN) {
	            changeOrientation = true;
	        } else {
	            int dist = Math.abs(orientation - orientationHistory);
	            dist = Math.min( dist, 360 - dist );
	            changeOrientation = ( dist >= 45 + 5 );
	        }
	        if (changeOrientation) {
	            return ((orientation + 45) / 90 * 90) % 360;
	        }
	        return orientationHistory;
	    }
}
