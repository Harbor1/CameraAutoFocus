package com.example.pichandler;

import com.example.modify.PicRecActivity;
import com.example.modify.R;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class PicRecogActivityHandler extends Handler{

	private static final String TAG=PicRecogActivityHandler.class.getSimpleName();
	
	  private State state;
	  private AutoFocusCallback autoFocusCallback=new AutoFocusCallback();

	  private enum State {
	    PREVIEW,
	    SUCCESS,
	    DONE
	  }

	
	private final PicRecActivity activity;
	public PicRecogActivityHandler(PicRecActivity activity) {
		//Log.e(TAG, "PicRecogActivityHandler");
		// TODO Auto-generated constructor stub
		this.activity=activity;
		state = State.PREVIEW;
		CameraManager.get().startPreview();
		CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
	}
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		//String AutoFocusState=(String)msg.obj;
		//Log.e(TAG, AutoFocusState);
		switch (msg.what) {
		case R.id.auto_focus:
			Boolean autoFocusState=(Boolean)msg.obj;
			if(!autoFocusState){
				CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
			}
			else {
				CameraManager.get().requestTakingPicture(this, R.id.take_picture);
			}
			break;
			
		case R.id.take_picture:
			if ((Boolean)msg.obj) {
				activity.finish();
			}
			break;

		default:
			break;
		}
	}
}
