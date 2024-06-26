package org.android.swamipothi.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.android.swamipothi.R;

public class FontListAdapter extends BaseAdapter
{

	private final static String TAG = FontListAdapter.class.getSimpleName();
	private Context context;
	private String[] fontNames, fontFiles;
	private LayoutInflater inflater;
	private TextView textViewFontName;

	public FontListAdapter(Context context, String[] fontNames, String[] fontFiles)
	{
		this.context = context;
		this.fontNames = fontNames;
		this.fontFiles = fontFiles;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount()
	{
		return fontNames.length;
	}

	@Override
	public Object getItem(int position)
	{
		return fontNames[position];
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
		try
		{
			if (convertView == null)
			{
				convertView = inflater.inflate(R.layout.listrow_font, null);
			}
			textViewFontName = (TextView) convertView.findViewById(R.id.listrow_textViewFontName);
			textViewFontName.setText(fontNames[position]);

			if (position == 0)
			{
				textViewFontName.setTypeface(Typeface.DEFAULT);
			}
			else
			{
				//Log.i("", "position : " + position + " | file : " + fontFiles[position]);
				Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/"
						+ fontFiles[position]);
				textViewFontName.setTypeface(typeFace);
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, "getView() - " + e.toString());
		}
		return convertView;
	}

}
