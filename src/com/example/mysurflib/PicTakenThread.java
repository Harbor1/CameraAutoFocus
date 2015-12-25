package com.example.mysurflib;



import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.modify.PicRecActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class PicTakenThread extends Thread{
	private static final String Tag="PicTakenThread";
	private SurfaceHolder mHolder;
	private Camera camera;
	private Handler mHandler;
	
	public  static String curPicPath;
	
	public PicTakenThread(SurfaceHolder  holder,Camera camera,Handler handler){
		//Log.e(Tag, "PicTakenThread Constructor");
		this.mHolder=holder;
		this.camera=camera;
		this.mHandler=handler;
	}
	
	public void run(){
		//Log.e(Tag, "Thread Run");
		
		synchronized (mHolder) {

			try{
				Thread.sleep(1000);
			camera.autoFocus(new AutoFocusCallback() {//自动对焦
	            @Override
	            public void onAutoFocus(boolean success, Camera camera) {
	            	Log.e(Tag, "onAutofocus");
	                // TODO Auto-generated method stub
	                if(success) {
	                	Log.e(Tag, "success");
	                    //设置参数，并拍照
	                    Parameters params = camera.getParameters();
	                    params.setPictureFormat(PixelFormat.JPEG);//图片格式
	                    //params.setPreviewSize(640, 480);//图片大小
	                  // 设置照片分辨率 
	                    //params.setPictureSize(640,480);
	                    camera.setParameters(params);//将参数设置到我的camera
	                    camera.takePicture(null, null, jpeg);//将拍摄到的照片给自定义的对象
	                }
	                else {
	                	 Message msg=new Message();
	                     msg.what=PicRecActivity.MSG_SUCCESS;
	                     mHandler.sendMessage(msg);
					}
	            }
	        });
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	 //创建jpeg图片回调数据对象
    PictureCallback jpeg = new PictureCallback() {
    	
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
        	Log.d(Tag,"onPictureTaken");
            // TODO Auto-generated method stub
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //自定义文件保存路径  以拍摄时间区分命名
                //filepath = "/sdcard/Messages/MyPictures/";
                String path;
               // path="/mnt/internal_sd/mypic/";
                path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/mypic/";
                String fileName=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".jpg";
               // String fileName="tttt"+".jpg";
                File out = new File(path);
				if (!out.exists()) {
					out.mkdirs();
				}
				out = new File(path, fileName);
				//curPicPath="/mnt/internal_sd/mypic/"+fileName;
				//curPicPath="/storage/emulated/0/mypic/"+fileName;
				curPicPath=path+fileName;
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(out));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩的流里面
                bos.flush();// 刷新此缓冲区的输出流
                bos.close();// 关闭此输出流并释放与此流有关的所有系统资源
                camera.stopPreview();//关闭预览 处理数据
             //   camera.startPreview();//数据处理完后继续开始预览
                bitmap.recycle();//回收bitmap空间
                Message msg=new Message();
                msg.what=PicRecActivity.MSG_SUCCESS;
                mHandler.sendMessage(msg);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
}
