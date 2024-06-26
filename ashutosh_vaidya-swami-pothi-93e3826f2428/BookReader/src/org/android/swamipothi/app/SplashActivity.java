package org.android.swamipothi.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import org.android.swamipothi.R;

public class SplashActivity extends BaseActivity
{



	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
	}
	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();

	}
	
	@Override
	protected void onPostResume()
	{
		// TODO Auto-generated method stub
		super.onPostResume();
		showSplash(2000);
	}
	
	private void showSplash(final long milisec)
	{
		final Handler handler;
		Thread thread;
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				finish();
				Intent intent = new Intent(SplashActivity.this,SwamiPhotoActivity.class);
				startActivity(intent);
			}
		};

		thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(milisec);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(1);
			}
		});
		
		thread.start();
	}
}
