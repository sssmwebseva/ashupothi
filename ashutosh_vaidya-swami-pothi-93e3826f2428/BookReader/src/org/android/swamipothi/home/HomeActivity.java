package org.android.swamipothi.home;

import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.android.swamipothi.R;
import org.android.swamipothi.app.BaseActivity;
import org.android.swamipothi.bookview.BookViewActivity;
import org.android.swamipothi.font.FontList;
import org.android.swamipothi.util.Alert;
import org.android.swamipothi.util.Constants;

public class HomeActivity extends BaseActivity implements OnItemClickListener
{

	private ListView listViewChaptersPurva;
	private ListView listViewChaptersUttar;
	private String[] chapterDisplayNamesPurva;
	private String[] chapterDisplayNameUttar;
	private String[] chapterFileNamesPurva;
	private String[] chapterFileNamesUttar;
	private TextView textViewTitle;

	private SharedPreferences sharedPrefBookmark;
	private RelativeLayout relativeLayoutMain;
	private FontList fontList;

	private String listType;
	private CustomListAdapter adapterPurva, adapterUttar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_home);
		
		chapterDisplayNamesPurva = getResources().getStringArray(
				R.array.chapterDisplayNameArrayPurva);
		chapterFileNamesPurva = getResources().getStringArray(R.array.chapterFileNameArrayPurva);
		chapterDisplayNameUttar = getResources().getStringArray(
				R.array.chapterDisplayNameArrayUttar);
		chapterFileNamesUttar = getResources().getStringArray(R.array.chapterFileNameArrayUttar);
		sharedPrefBookmark = this.getSharedPreferences(
				getResources().getString(R.string.bookmark_file), Context.MODE_PRIVATE);

		initComponents();
	}

	private void initComponents()
	{
		try
		{
			relativeLayoutMain = (RelativeLayout) findViewById(R.id.home_relativeLayoutMain);

			listViewChaptersPurva = (ListView) findViewById(R.id.home_ListViewChapters);
			adapterPurva = new CustomListAdapter(this, Constants.CHAPTER_LIST_PURVACHARITRA,
					chapterDisplayNamesPurva);
			listViewChaptersPurva.setAdapter(adapterPurva);
			listViewChaptersPurva.setOnItemClickListener(this);

			listViewChaptersUttar = (ListView) findViewById(R.id.home_ListViewChapters1);
			adapterUttar = new CustomListAdapter(this, Constants.CHAPTER_LIST_UTTARCHARITRA,
					chapterDisplayNameUttar);
			listViewChaptersUttar.setAdapter(adapterUttar);
			listViewChaptersUttar.setOnItemClickListener(this);

			final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
					R.layout.action_bar, null);
			textViewTitle = (TextView) actionBarLayout.findViewById(R.id.actionBar_textViewTitle);
			textViewTitle.setText(getResources().getString(R.string.index));
			((Button) actionBarLayout.findViewById(R.id.actionBar_buttonZoomIn))
					.setVisibility(View.GONE);
			((Button) actionBarLayout.findViewById(R.id.actionBar_buttonZoomOut))
					.setVisibility(View.GONE);
			((Button) actionBarLayout.findViewById(R.id.actionBar_buttonNext))
					.setVisibility(View.GONE);
			((Button) actionBarLayout.findViewById(R.id.actionBar_buttonFont))
					.setVisibility(View.GONE);

			final ActionBar actionBar = getActionBar();
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
		String savedListType = sharedPrefBookmark.getString("savedListType", "");
		String savedChapterName = sharedPrefBookmark.getString("savedChapterName", "");
		
		fontList = new FontList(this, null);
		fontList.setFontToAllChilds(relativeLayoutMain);
		adapterPurva.setSavedListTypeAndChapterName(savedListType, savedChapterName);
		adapterUttar.setSavedListTypeAndChapterName(savedListType, savedChapterName);
		adapterPurva.notifyDataSetChanged();
		adapterUttar.notifyDataSetChanged();

		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long arg3)
	{
		try
		{
			Intent intent = new Intent(this, BookViewActivity.class);
			String selectedChapterTypeAndFileName = "";
			if (parent.equals(listViewChaptersPurva))
			{
				listType = Constants.CHAPTER_LIST_PURVACHARITRA;
				selectedChapterTypeAndFileName = listType + "_" + chapterFileNamesPurva[position];
				intent.putExtra("chapterFileName", chapterFileNamesPurva[position]);
				intent.putExtra("chapterName", chapterDisplayNamesPurva[position]);
			}
			if (parent.equals(listViewChaptersUttar))
			{
				listType = Constants.CHAPTER_LIST_UTTARCHARITRA;
				selectedChapterTypeAndFileName = listType + "_" + chapterFileNamesUttar[position];
				intent.putExtra("chapterFileName", chapterFileNamesUttar[position]);
				intent.putExtra("chapterName", chapterDisplayNameUttar[position]);
			}

			JSONObject json = new JSONObject(sharedPrefBookmark.getString(
					selectedChapterTypeAndFileName, "{}"));
			int bookmark_line = json.optInt("bookmark_line");
			int bookmark_column = json.optInt("bookmark_column");
			int fontSize = (json.optInt("fontSize") == 0) ? 26 : json.optInt("fontSize");


			intent.putExtra("bookmark_line", bookmark_line);
			intent.putExtra("bookmark_column", bookmark_column);
			intent.putExtra("fontSize", fontSize);
			intent.putExtra("position", position);
			intent.putExtra("listType", listType);
			startActivity(intent);
		}
		catch (Exception e)
		{
			Alert.showToast(this, e.getMessage());
			e.printStackTrace();
		}
	}

	private static long back_pressed;

	@Override
	public void onBackPressed()
	{
		if (back_pressed + 2000 > System.currentTimeMillis())
		{
			super.onBackPressed();
		}
		else
		{
			Toast.makeText(getBaseContext(), getString(R.string.exit), Toast.LENGTH_SHORT).show();
		}
		back_pressed = System.currentTimeMillis();
	}

}
