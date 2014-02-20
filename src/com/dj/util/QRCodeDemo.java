package com.dj.util;

import java.lang.reflect.Array;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.SFHCamera;
import com.google.zxing.common.HybridBinarizer;

public class QRCodeDemo extends Activity
{
	private SurfaceView sfvCamera;  
    private SFHCamera sfhCamera;  
    private ImageView imgView;  
    private View centerView;  
    private TextView txtScanResult;  
    private Timer mTimer;  
    private MyTimerTask mTimerTask;  
    // 按照标准HVGA  
    //2592 1944
    final static int width = 800;  
    final static int height = 600;  
    int dstLeft, dstTop, dstWidth, dstHeight;  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.qr_layout);  
        this.setTitle("Android条码/二维码识别Demo-----hellogv");  
        imgView = (ImageView) this.findViewById(R.id.ImageView01);  
        centerView = (View) this.findViewById(R.id.centerView);  
        sfvCamera = (SurfaceView) this.findViewById(R.id.sfvCamera);  
        sfhCamera = new SFHCamera(sfvCamera.getHolder(), width, height,  
                previewCallback);  
        txtScanResult=(TextView)this.findViewById(R.id.txtScanResult);  
        // 初始化定时器  
        mTimer = new Timer();  
        mTimerTask = new MyTimerTask();  
        mTimer.schedule(mTimerTask, 0, 80);  
        
       
    }  
      
    class MyTimerTask extends TimerTask {  
        @Override  
        public void run() {  
            if (dstLeft == 0) {//只赋值一次  
                dstLeft = centerView.getLeft() * width  
                        / getWindowManager().getDefaultDisplay().getWidth();  
                dstTop = centerView.getTop() * height  
                        / getWindowManager().getDefaultDisplay().getHeight();  
                dstWidth = (centerView.getRight() - centerView.getLeft())* width  
                        / getWindowManager().getDefaultDisplay().getWidth();  
                dstHeight = (centerView.getBottom() - centerView.getTop())* height  
                        / getWindowManager().getDefaultDisplay().getHeight();  
            }  
            sfhCamera.AutoFocusAndPreviewCallback();  
        }  
    }  
    /** 
     *  自动对焦后输出图片 
     */  
    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {  
        @Override  
        public void onPreviewFrame(byte[] data, Camera camera) 
        {  
        	Log.d("QRCodeDemo", camera.getParameters().getPreviewSize().width + " " + camera.getParameters().getPreviewSize().height);
        	Log.d("QRCodeDemo", "" +data.length);
        	//取得指定范围的帧的数据  
/*            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(  
                    data, width, height, dstLeft, dstTop, dstWidth, dstHeight);  */
        	
        	int tWidth = camera.getParameters().getPreviewSize().width;
        	int tHeight = camera.getParameters().getPreviewSize().height;
        	byte yuvData[] = new byte[tWidth*tHeight*3/2];
//        	yuvData = data;
            YUVRotate90(data, yuvData, width, height);    //旋转90度
//        	PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(  
//        			yuvData, width, height, dstLeft, dstTop, 300, 300);  
        	PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(  
        			yuvData, height, width, 600*234/768, 800*362/1024, 300*600/768, 300*800/1024);  
            //取得灰度图  
            Bitmap mBitmap = source.renderCroppedGreyscaleBitmap(); 
            //显示灰度图  
            imgView.setImageBitmap(mBitmap);  
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));  
            MultiFormatReader reader = new MultiFormatReader();  
            try {  
                Result result = reader.decode(bitmap);  
                String strResult = "BarcodeFormat:"  
                        + result.getBarcodeFormat().toString() + "  text:"  
                        + result.getText();  
                txtScanResult.setText(strResult);  
            } catch (Exception e) {  
                txtScanResult.setText("Scanning");  
            }  
        }  
    };  
    
    
    @Override
	protected void onDestroy()
	{
		super.onDestroy();
		
	}


	private void YUVRotate90(byte[] src,byte[] des,int width,int height) 
    { 
    	/*int i=0,j=0,n=0; 
    	int hw=width/4,hh=height/4; 
    	for(j=width;j>0;j--) 
    	for(i=0;i<height;i++) 
    	{ 
    	   des[n++] = src[width*i+j]; 
    	}
    	
    	byte[] ptmp = new byte[width*height/4]; 
    	System.arraycopy(src, width*height, ptmp, 0, width * height/4);
    	for(j=hw;j>0;j--)
    	for(i=0;i<hh;i++) 
    	{
    		des[n++] = ptmp[hw*i+j]; 
    	}
    	
//    	byte[] ptmp2;
//    	ptmp = src+width*height*5/4;
    	System.arraycopy(src, width*height*5/4, ptmp, 0, width * height/4);
    	for(j=hw;j>0;j--)
    	for(i=0;i<hh;i++) 
    	{ 
    		des[n++] = ptmp[hw*i+j]; 
    	} */  
    	
    	
    	int i=0,j=0,n=0; 
    	int hw=width/4,hh=height/4; 
    	for(j=0;j<width;j++) 
    	for(i=height;i>0;i--) 
    	{ 
    	   des[n++] = src[width*i+j]; 
    	}
    	
    	byte[] ptmp = new byte[width*height/4]; 
    	System.arraycopy(src, width*height, ptmp, 0, width * height/4);
    	for(j=0;j<hw;j++)
    	for(i=hh;i>0;i--) 
    	{
    		des[n++] = ptmp[hw*i+j]; 
    	}
    	
//    	byte[] ptmp2;
//    	ptmp = src+width*height*5/4;
    	System.arraycopy(src, width*height*5/4, ptmp, 0, width * height/4);
    	for(j=0;j<hw;j++)
    	for(i=hh;i>0;i--) 
    	{ 
    		des[n++] = ptmp[hw*i+j]; 
    	}  
    }
}
