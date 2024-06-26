package org.android.swamipothi.app;

import org.android.swamipothi.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

public class BaseActivity extends Activity
{
	private static Object fa;
	int onStartCount = 0;
	protected boolean doubleBackToExitPressedOnce;

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		onStartCount = 1;
		if (savedInstanceState == null) // 1st time
		{
			this.overridePendingTransition(R.anim.anim_slide_in_up, R.anim.anim_slide_out_up);
		}
		else
		// already created so reverse animation
		{
			onStartCount = 2;
		}
	}


	
	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		if (onStartCount > 1)
		{
			this.overridePendingTransition(R.anim.anim_slide_in_down, R.anim.anim_slide_out_down);
		}
		else if (onStartCount == 1)
		{
			onStartCount++;
		}

			
	}



	public void onItemClick(AdapterView<?> parent, View view, int position,
			long arg3, BaseAdapter adapter) {
		// TODO Auto-generated method stub
		
	}
}
