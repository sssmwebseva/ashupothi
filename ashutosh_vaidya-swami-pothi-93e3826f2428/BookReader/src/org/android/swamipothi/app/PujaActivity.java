package org.android.swamipothi.app;

import org.android.swamipothi.R;
import org.android.swamipothi.home.HomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class PujaActivity extends BaseBookViewActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setTitle(getResources().getString(R.string.puja_title));
		loadChapter("intro/sadguru_manaspooja.txt");

		buttonNext.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(PujaActivity.this, HomeActivity.class);
				startActivity(intent);
			}
		});
	}
}
