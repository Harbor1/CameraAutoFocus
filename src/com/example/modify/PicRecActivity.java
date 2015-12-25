package com.example.modify;



import java.io.File;
import java.io.IOException;

import com.example.mysurflib.PicTakenThread;
import com.example.pichandler.CameraManager;
import com.example.pichandler.PicRecogActivityHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class PicRecActivity extends Activity implements Callback {
	
	private static final String TAG=PicRecActivity.class.getSimpleName();
	
	public static final int MSG_SUCCESS = 0;//获取图片成功的标识
	public static final int MSG_FAILURE = 1;//获取图片失败的标识
    private SurfaceView surface;
    private Button shutter;//快门
    private SurfaceHolder holder;
    private Camera camera;//声明相机
    private String filepath = "";//照片保存路径
    private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头
	// 图片保存的路径
	public  String PIC_PATH = "/mypic/";
	// 模型保存的路径
	public  String MODULE_PATH = "/mypic/module/";
	public  String moduleFile = "moduleFileRank.xml";
	private  int bookNo;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//拍照过程屏幕一直处于高亮
      
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.pic_reg);

        
        PIC_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ PIC_PATH;
		MODULE_PATH = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + MODULE_PATH;   
        surface = (SurfaceView) findViewById(R.id.camera_surface);
        holder = surface.getHolder();//获得句柄
        holder.addCallback(this);//添加回调
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//surfaceview不维护自己的缓冲区，等待屏幕渲染引擎将内容推送到用户面前
        CameraManager.init(getApplication());
        File file = new File(MODULE_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
    }

   
    /*surfaceHolder他是系统提供的一个用来设置surfaceView的一个对象，而它通过surfaceView.getHolder()这个方法来获得。
     Camera提供一个setPreviewDisplay(SurfaceHolder)的方法来连接*/

    //SurfaceHolder.Callback,这是个holder用来显示surfaceView 数据的接口,他必须实现以下3个方法
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    	try{
    		CameraManager.get().openDrive(holder);
    		}catch (IOException ioe){
    			ioe.printStackTrace();
    		}
        new PicRecogActivityHandler(this);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        //当surfaceview关闭时，关闭预览并释放资源
    	CameraManager.get().stopPreview();
    	CameraManager.get().closeDrive();
        holder = null;
        surface = null;
    }  
}