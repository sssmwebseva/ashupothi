package org.android.swamipothi.bookview;

import org.android.swamipothi.R;
import org.android.swamipothi.app.BaseActivity;
import org.android.swamipothi.font.FontList;
import org.android.swamipothi.util.Alert;
import org.android.swamipothi.util.BaseTask;
import org.android.swamipothi.util.Constants;
import org.android.swamipothi.util.FileUtil;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class BookViewActivity extends BaseActivity implements OnClickListener
{
	private final static String TAG = BookViewActivity.class.getSimpleName();
	private TextView textViewPage;
	private ScrollView scrollViewPage;
	private ActionBar actionBar;
	private String chapterFileName, chapterName;
	private String[] chapterDisplayNames;
	private String[] chapterFileNames;
	private TextView textViewTitle;
	private Button buttonZoomIn, buttonZoomOut, buttonFont, buttonNext;
	private LinearLayout linearLayoutMain;
	private int currPosition;
	private final int zoom_in_limit = 40;
	private final int zoom_out_limit = 22;
	private int currentZoom = 26;
	private int bookmark_line;
	private SharedPreferences sharedPrefBookmark;
	private FontList fontList;
	private String listType;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		try
		{
			super.onCreate(savedInstanceState);
			getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
			this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);

			setContentView(R.layout.bookview);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

			sharedPrefBookmark = this.getSharedPreferences(
					getResources().getString(R.string.bookmark_file), Context.MODE_PRIVATE);

			initComponent();

			chapterFileName = getIntent().getExtras().getString("chapterFileName");
			chapterName = getIntent().getExtras().getString("chapterName");
			listType = getIntent().getExtras().getString("listType");
			bookmark_line = getIntent().getExtras().getInt("bookmark_line");
			getIntent().getExtras().getInt("bookmark_ad");
			currentZoom = getIntent().getExtras().getInt("fontSize");
			currPosition = getIntent().getExtras().getInt("position");
			textViewPage.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentZoom);
			textViewTitle.setText(chapterName);

			if (listType.equals(Constants.CHAPTER_LIST_PURVACHARITRA))
			{
				chapterDisplayNames = getResources().getStringArray(
						R.array.chapterDisplayNameArrayPurva);
				chapterFileNames = getResources().getStringArray(R.array.chapterFileNameArrayPurva);
			}
			else if (listType.equals(Constants.CHAPTER_LIST_UTTARCHARITRA))
			{
				chapterDisplayNames = getResources().getStringArray(
						R.array.chapterDisplayNameArrayUttar);
				chapterFileNames = getResources().getStringArray(R.array.chapterFileNameArrayUttar);
			}

			if (chapterFileName != null && !chapterFileName.trim().equals(""))
			{
				loadChapter(chapterFileName, chapterName);
			}
			else
			{
				Alert.showToast(this, "File not found.");
			}

		}
		catch (Exception e)
		{
			Alert.showToast(this, e.getMessage());
			e.printStackTrace();
		}
	}

	private void loadNextChapter(int position)
	{
		try
		{
			String chapterFileName = chapterFileNames[position];
			String chapterName = chapterDisplayNames[position];

			textViewTitle.setText(chapterName);
			if (chapterFileName != null && !chapterFileName.trim().equals(""))
			{
				loadChapter(chapterFileName, chapterName);
			}

			else
			{
				Alert.showToast(this, "file not found.");
			}

		}
		catch (Exception e)
		{
			Log.e(TAG, "loadNextChapter() - " + e.toString());
			e.printStackTrace();
		}
	}

	private void initComponent()
	{
		try
		{
			linearLayoutMain = (LinearLayout) findViewById(R.id.bookview_linearLayoutMain);

			final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
					R.layout.action_bar, null);

			textViewPage = (TextView) findViewById(R.id.bookview_textViewPage);
			textViewPage.setOnClickListener(this);
			scrollViewPage = (ScrollView) findViewById(R.id.bookview_scrollViewPage);

			textViewTitle = (TextView) actionBarLayout.findViewById(R.id.actionBar_textViewTitle);
			buttonZoomIn = (Button) actionBarLayout.findViewById(R.id.actionBar_buttonZoomIn);
			buttonZoomOut = (Button) actionBarLayout.findViewById(R.id.actionBar_buttonZoomOut);
			buttonFont = (Button) actionBarLayout.findViewById(R.id.actionBar_buttonFont);
			buttonNext = (Button) actionBarLayout.findViewById(R.id.actionBar_buttonNext);

			buttonZoomIn.setOnClickListener(this);
			buttonZoomOut.setOnClickListener(this);
			buttonFont.setOnClickListener(this);
			buttonNext.setOnClickListener(this);

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

	@Override
	protected void onPause()
	{
		saveCurrentPosition();
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		saveCurrentPosition();
		super.onDestroy();
	}

	private void saveCurrentPosition()
	{
		scrollViewPage.post(new Runnable()
		{
			@Override
			public void run()
			{
				Editor editor = sharedPrefBookmark.edit();

				try
				{
					JSONObject json = new JSONObject();
					json.put("bookmark_line", scrollViewPage.getScrollY());
					json.put("bookmark_column", scrollViewPage.getScrollX());
					json.put("fontSize", currentZoom);
					editor.putString(listType + "_" + chapterFileName, json.toString());
					editor.commit();

				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}

			}
		});
	}

	private void goToBookmarkPosition()
	{
		textViewPage.post(new Runnable()
		{

			@Override
			public void run()
			{
				Layout layout = textViewPage.getLayout();
				// ScrollView.scrollTo(0,layout.getLineTop(layout.getLineForOffset(startPos)));
				scrollViewPage.smoothScrollTo(0, layout.getLineTop(layout.getLineCount() - 10));
				scrollViewPage.smoothScrollTo(0, bookmark_line);

			}
		});
	}

	private void loadChapter(String chapterFileName, String chapterName)
	{
		try
		{
			Editor editor = sharedPrefBookmark.edit();
			editor.putString("savedChapterName", chapterName);
			editor.putString("savedListType", listType);
			editor.commit();
			new BackgroundTask(chapterFileName, this, "", "Loading...", true).execute();
		}
		catch (Exception e)
		{
			Alert.showToast(this, e.getMessage());
			e.printStackTrace();
		}
	}

	private class BackgroundTask extends BaseTask
	{
		Activity activity;
		String fileName;

		public BackgroundTask(String chapterName, Activity activity, String title, String message,
				boolean showProgress)
		{
			super(activity, title, message, showProgress);
			this.activity = activity;

			if (listType.equals(Constants.CHAPTER_LIST_PURVACHARITRA))
			{
				fileName = "chapters/purvacharitra/" + chapterName;
			}

			if (listType.equals(Constants.CHAPTER_LIST_UTTARCHARITRA))
			{
				fileName = "chapters/uttarcharitra/" + chapterName;
			}

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
					goToBookmarkPosition();
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
		if (buttonZoomIn == v)
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
		else if (buttonNext == v)
		{
			if ((currPosition + 1) >= chapterFileNames.length)
			{
				String msgString = (listType.equals(Constants.CHAPTER_LIST_PURVACHARITRA)) ? getResources()
						.getString(R.string.purvacharitra)
						+ " "
						+ getResources().getString(R.string.end) : getResources().getString(R.string.coming_soon);

				Toast.makeText(this, msgString, Toast.LENGTH_SHORT).show();
			}
			else
			{
				currPosition = currPosition + 1;
				loadNextChapter(currPosition);
			}
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
