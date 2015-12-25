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
	
	public static final int MSG_SUCCESS = 0;//��ȡͼƬ�ɹ��ı�ʶ
	public static final int MSG_FAILURE = 1;//��ȡͼƬʧ�ܵı�ʶ
    private SurfaceView surface;
    private Button shutter;//����
    private SurfaceHolder holder;
    private Camera camera;//�������
    private String filepath = "";//��Ƭ����·��
    private int cameraPosition = 1;//0����ǰ������ͷ��1�����������ͷ
	// ͼƬ�����·��
	public  String PIC_PATH = "/mypic/";
	// ģ�ͱ����·��
	public  String MODULE_PATH = "/mypic/module/";
	public  String moduleFile = "moduleFileRank.xml";
	private  int bookNo;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//û�б���
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//����ȫ��
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//���չ�����Ļһֱ���ڸ���
      
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.pic_reg);

        
        PIC_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ PIC_PATH;
		MODULE_PATH = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + MODULE_PATH;   
        surface = (SurfaceView) findViewById(R.id.camera_surface);
        holder = surface.getHolder();//��þ��
        holder.addCallback(this);//��ӻص�
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//surfaceview��ά���Լ��Ļ��������ȴ���Ļ��Ⱦ���潫�������͵��û���ǰ
        CameraManager.init(getApplication());
        File file = new File(MODULE_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
    }

   
    /*surfaceHolder����ϵͳ�ṩ��һ����������surfaceView��һ�����󣬶���ͨ��surfaceView.getHolder()�����������á�
     Camera�ṩһ��setPreviewDisplay(SurfaceHolder)�ķ���������*/

    //SurfaceHolder.Callback,���Ǹ�holder������ʾsurfaceView ���ݵĽӿ�,������ʵ������3������
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
        //��surfaceview�ر�ʱ���ر�Ԥ�����ͷ���Դ
    	CameraManager.get().stopPreview();
    	CameraManager.get().closeDrive();
        holder = null;
        surface = null;
    }  
}