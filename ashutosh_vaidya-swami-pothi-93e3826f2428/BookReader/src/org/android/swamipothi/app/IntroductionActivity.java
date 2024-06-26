package org.android.swamipothi.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import org.android.swamipothi.R;
import org.android.swamipothi.home.HomeActivity;

public class IntroductionActivity extends BaseBookViewActivity implements OnClickListener
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setTitle(getResources().getString(R.string.introduction));
		loadChapter("intro/prastavna.txt");

		buttonNext.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(IntroductionActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

}
