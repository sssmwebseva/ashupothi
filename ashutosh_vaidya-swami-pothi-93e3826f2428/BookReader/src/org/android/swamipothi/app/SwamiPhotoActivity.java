package org.android.swamipothi.app;

import org.android.swamipothi.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class SwamiPhotoActivity extends BaseActivity implements OnClickListener
{

	public static Object fa;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_swami_photo);
		
		((LinearLayout)findViewById(R.id.activitySwami_linearLayoutMain)).setOnClickListener(this);
		
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	public void onClick(View v)
	{
		Intent intent = new Intent(SwamiPhotoActivity.this, IntroductionActivity.class);
		startActivity(intent);
		finish();
	}
	
	
}
