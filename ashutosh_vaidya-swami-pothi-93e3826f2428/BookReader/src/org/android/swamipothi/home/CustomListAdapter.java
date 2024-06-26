package org.android.swamipothi.home;

import org.android.swamipothi.R;
import org.android.swamipothi.util.Constants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter
{
	private String[] chapterNames;
	private LayoutInflater inflater;
	private TextView textViewChapterName;
	private String savedListType, savedChapterName, currListType;

	public CustomListAdapter(Context c, String currListType, String[] arrayChapterName)
	{
		chapterNames = arrayChapterName;
		inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.currListType = currListType;
	}

	public void setSavedListTypeAndChapterName(String savedListType, String savedChapterName)
	{
		this.savedListType = savedListType;
		this.savedChapterName = savedChapterName;
	}

	@Override
	public int getCount()
	{
		return chapterNames.length;
	}

	@Override
	public Object getItem(int position)
	{
		return chapterNames[position];
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.listrow_chapter, null);
		}

		textViewChapterName = (TextView) convertView.findViewById(R.id.listrow_textViewChapterName);
		textViewChapterName.setText(chapterNames[position]);
		textViewChapterName.setTextColor(Color.BLACK);
		textViewChapterName.setTypeface(null, Typeface.NORMAL);


		if (currListType.equals(Constants.CHAPTER_LIST_PURVACHARITRA))
		{
			if (savedListType.equals(Constants.CHAPTER_LIST_PURVACHARITRA)
					&& savedChapterName.equals(chapterNames[position]))
			{
				textViewChapterName.setTextColor(Color.parseColor("#0000CC"));
				textViewChapterName.setTypeface(null, Typeface.BOLD);

			}
		}

		if (currListType.equals(Constants.CHAPTER_LIST_UTTARCHARITRA))
		{
			if (savedListType.equals(Constants.CHAPTER_LIST_UTTARCHARITRA)
					&& savedChapterName.equals(chapterNames[position]))
			{
				textViewChapterName.setTextColor(Color.parseColor("#0000CC"));
				textViewChapterName.setTypeface(null, Typeface.BOLD);

			}
		}

		textViewChapterName.requestLayout();
		return convertView;

	}
}
