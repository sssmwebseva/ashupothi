package org.android.swamipothi.app;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.android.swamipothi.R;
import org.android.swamipothi.font.FontList;
import org.android.swamipothi.util.Alert;
import org.android.swamipothi.util.BaseTask;
import org.android.swamipothi.util.FileUtil;

public class BaseBookViewActivity extends BaseActivity implements OnClickListener
{
	public static final boolean doubleBackToExitPressedOnce = false;
	private static TextView textViewPage;
	private ScrollView scrollViewPage;
	private TextView textViewTitle;
	private ActionBar actionBar;
	private Button buttonZoomIn, buttonZoomOut, buttonFont;
	protected Button buttonNext;
	private final int zoom_in_limit = 40;
	private final int zoom_out_limit = 22;
	private int currentZoom = 26;
	private LinearLayout linearLayoutMain;
	private FontList fontList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.bookview);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		initComponents();
	}

	private void initComponents()
	{
		try
		{
			linearLayoutMain = (LinearLayout) findViewById(R.id.bookview_linearLayoutMain);

			textViewPage = (TextView) findViewById(R.id.bookview_textViewPage);
			textViewPage.setOnClickListener(this);
			scrollViewPage = (ScrollView) findViewById(R.id.bookview_scrollViewPage);

			final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
					R.layout.action_bar, null);
			textViewTitle = (TextView) actionBarLayout.findViewById(R.id.actionBar_textViewTitle);

			buttonNext = (Button) actionBarLayout.findViewById(R.id.actionBar_buttonNext);
			buttonNext.setOnClickListener(this);
			buttonZoomIn = (Button) actionBarLayout.findViewById(R.id.actionBar_buttonZoomIn);
			buttonZoomIn.setOnClickListener(this);
			buttonZoomOut = (Button) actionBarLayout.findViewById(R.id.actionBar_buttonZoomOut);
			buttonZoomOut.setOnClickListener(this);
			buttonFont = (Button) actionBarLayout.findViewById(R.id.actionBar_buttonFont);
			buttonFont.setOnClickListener(this);
			

			actionBar = getActionBar();
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setCustomView(actionBarLayout);
		
		}
		catch (Exception e)
		{
			Alert.showToast(this, e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume()
	{
		fontList = new FontList(this, buttonFont);
		fontList.addViewGroupToChangeFont(linearLayoutMain);
		fontList.setFontToAllChilds(linearLayoutMain);
		super.onResume();
	}

	public void setTitle(String title)
	{
		textViewTitle.setText(title);
	}

	public void showButtonZoomIn(boolean show)
	{
		buttonZoomIn.setVisibility((show) ? View.VISIBLE : View.GONE);
	}

	public void showButtonZoomOut(boolean show)
	{
		buttonZoomOut.setVisibility((show) ? View.VISIBLE : View.GONE);
	}

	public void showButtonNext(boolean show)
	{
		buttonNext.setVisibility((show) ? View.VISIBLE : View.GONE);
	}
	
	public void showButtonFont(boolean show)
	{
		buttonFont.setVisibility((show) ? View.VISIBLE : View.GONE);
	}
	
	
	public void loadChapter(String chapterName)
	{
		try
		{
			new BackgroundTask(chapterName, this, "", "Loading...", true).execute();
		}
		catch (Exception e)
		{
			Alert.showToast(this, e.getMessage());
			e.printStackTrace();
		}
	}

	private static class BackgroundTask extends BaseTask
	{

		Activity activity;
		String fileName, fileName1;
		private boolean doubleBackToExitPressedOnce;

		public BackgroundTask(String chapterName, Activity activity, String title, String message,
				boolean showProgress)
		{
			super(activity, title, message, showProgress);
			this.activity = activity;
			fileName = chapterName;
		}

		@Override
		protected Object doInBackground(Object... params)
		{
			return FileUtil.assetFileToString(activity, fileName);
		}

		@Override
		protected void onPostExecute(Object result)
		{
			try
			{
				if (result != null)
				{
					textViewPage.setText((String) result);
				}
				else
				{
					Alert.showToast(activity, "Failed to load file.");
				}
			}
			catch (Exception e)
			{
				Alert.showToast(activity, e.getMessage());
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

	}

	@Override
	public void onClick(View v)
	{

		if (v.equals(buttonNext))
		{
			/*			Intent intent = new Intent(this, HomeActivity.class);
						startActivity(intent);*/
		}
		else if (buttonZoomIn == v)
		{
			if (currentZoom < zoom_in_limit)
			{
				currentZoom = currentZoom + 2;
				textViewPage.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentZoom);
				textViewPage.invalidate();
			}
		}
		else if (buttonZoomOut == v)
		{
			if (currentZoom > zoom_out_limit)
			{
				currentZoom = currentZoom - 2;
				textViewPage.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentZoom);
				textViewPage.invalidate();
			}
		}
		else if (buttonFont == v)
		{
			fontList.show();
		}
		
		
		else if (textViewPage == v)
		{
			if (actionBar.isShowing())
			{
				actionBar.hide();
			}
			else
			{
				actionBar.show();
			}
		}

	}

}
