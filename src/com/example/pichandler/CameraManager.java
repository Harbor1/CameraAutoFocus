package com.example.pichandler;


import java.io.IOException;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.view.SurfaceHolder;

public class CameraManager {
	private static final String TAG=CameraManager.class.getSimpleName();
	
	private static CameraManager cameraManager;
	private final Context context;
	private Camera camera;
	private boolean initialized;
	private boolean previewing;

	private final AutoFocusCallback autoFocusCallback;
	private final PictureCallback pictureCallback;
	  /**
	   * Initializes this static object with the Context of the calling Activity.
	   *
	   * @param context The Activity which wants to use the camera.
	   */
	  public static void init(Context context) {
	    if (cameraManager== null) {
	      cameraManager = new CameraManager(context);
	    }
	  }

	  /**
	   * Gets the CameraManager singleton instance.
	   *
	   * @return A reference to the CameraManager singleton.
	   */
	  public static CameraManager get() {
	    return cameraManager;
	  }
	
	private CameraManager(Context context){
		this.context=context;
		autoFocusCallback=new AutoFocusCallback();
		pictureCallback=new PictureCallback();
	}
	
	public void openDrive(SurfaceHolder holder)throws IOException{
		if(camera == null) {
            camera = Camera.open();
            if (camera == null) {
                throw new IOException();
              }
            try {
            	camera.setDisplayOrientation(90);  
                camera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
	}

	public void closeDrive(){
		 if (camera != null) {
		      camera.release();
		      camera = null;
		   }
	}
	
	/**
	   * Asks the camera hardware to begin drawing preview frames to the screen.
	   */
	  public void startPreview() {
	    if (camera != null && !previewing) {
	      camera.startPreview();
	      previewing = true;
	    }
	  }

	  /**
	   * Tells the camera to stop drawing preview frames.
	   */
	  public void stopPreview() {
	    if (camera != null && previewing) {
	      camera.stopPreview();
	      autoFocusCallback.setHandler(null, 0);
	      previewing = false;
	    }
	  }

	 public void requestAutoFocus(Handler handler, int message) {
		    if (camera != null && previewing) {
		      autoFocusCallback.setHandler(handler, message);
		      //Log.d(TAG, "Requesting auto-focus callback");
		      camera.autoFocus(autoFocusCallback);
		    }
		  }
	 
	 public void requestTakingPicture(Handler handler, int message){
		 if (camera != null && previewing) {
		      pictureCallback.setHandler(handler, message);
		      Parameters params = camera.getParameters();
              params.setPictureFormat(PixelFormat.JPEG);//图片格式
              //params.setPreviewSize(640, 480);//图片大小
            // 设置照片分辨率 
             // params.setPictureSize(640,480);
              camera.setParameters(params);//将参数设置到我的camera
		      camera.takePicture(null, null, pictureCallback);
		    }
	 }
}
