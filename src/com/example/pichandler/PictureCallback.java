package com.example.pichandler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class PictureCallback implements Camera.PictureCallback{
	
	private static final String TAG=PictureCallback.class.getSimpleName();
	
	private Handler pictureCallbackHandler;
	private int pictureCallbackMessage;
	public  static String curPicPath;
	
	public void setHandler(Handler pictureCallbackHandler,int pictureCallbackMessage){
		this.pictureCallbackHandler=pictureCallbackHandler;
		this.pictureCallbackMessage=pictureCallbackMessage;
	}
	
	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
		if (pictureCallbackHandler != null) {
			
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
                //camera.stopPreview();//关闭预览 处理数据
             //   camera.startPreview();//数据处理完后继续开始预览
                bitmap.recycle();//回收bitmap空间
          
			boolean success=true;
	    	
		      Message message = pictureCallbackHandler.obtainMessage(pictureCallbackMessage,
		    		  success);
		      // Simulate continuous autofocus by sending a focus request every
		      // AUTOFOCUS_INTERVAL_MS milliseconds.
		      //Log.d(TAG, "Got auto-focus callback; requesting another");
		      pictureCallbackHandler.sendMessage(message);
		      pictureCallbackHandler = null;
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		}else {
		      Log.d(TAG, "Got auto-focus callback, but no handler for it");
		}
	}
}
