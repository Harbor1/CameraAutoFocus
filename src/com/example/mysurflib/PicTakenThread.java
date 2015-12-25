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
			camera.autoFocus(new AutoFocusCallback() {//�Զ��Խ�
	            @Override
	            public void onAutoFocus(boolean success, Camera camera) {
	            	Log.e(Tag, "onAutofocus");
	                // TODO Auto-generated method stub
	                if(success) {
	                	Log.e(Tag, "success");
	                    //���ò�����������
	                    Parameters params = camera.getParameters();
	                    params.setPictureFormat(PixelFormat.JPEG);//ͼƬ��ʽ
	                    //params.setPreviewSize(640, 480);//ͼƬ��С
	                  // ������Ƭ�ֱ��� 
	                    //params.setPictureSize(640,480);
	                    camera.setParameters(params);//���������õ��ҵ�camera
	                    camera.takePicture(null, null, jpeg);//�����㵽����Ƭ���Զ���Ķ���
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
	
	 //����jpegͼƬ�ص����ݶ���
    PictureCallback jpeg = new PictureCallback() {
    	
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
        	Log.d(Tag,"onPictureTaken");
            // TODO Auto-generated method stub
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //�Զ����ļ�����·��  ������ʱ����������
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
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//��ͼƬѹ����������
                bos.flush();// ˢ�´˻������������
                bos.close();// �رմ���������ͷ�������йص�����ϵͳ��Դ
                camera.stopPreview();//�ر�Ԥ�� ��������
             //   camera.startPreview();//���ݴ�����������ʼԤ��
                bitmap.recycle();//����bitmap�ռ�
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
