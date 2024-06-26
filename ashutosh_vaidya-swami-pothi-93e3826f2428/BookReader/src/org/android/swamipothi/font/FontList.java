package org.android.swamipothi.font;

import java.util.ArrayList;
import java.util.List;

import org.android.swamipothi.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;

public class FontList implements OnItemClickListener
{
	private final static String TAG = FontList.class.getSimpleName();
	private Context context;
	private ListPopupWindow listPopupWindow;
	private String fontNames[], fontFiles[];
	private SharedPreferences sharedPrefFont;
	private int currentFontPosition = 0;
	private List<ViewGroup> parentLayouts;

	public FontList(Context context, View ancherView)
	{
		this.context = context;
		parentLayouts = new ArrayList<ViewGroup>();
		fontFiles = context.getResources().getStringArray(R.array.fontFileNameArray);
		fontNames = new String[fontFiles.length];

		sharedPrefFont = context.getSharedPreferences(
				context.getResources().getString(R.string.font_file), Context.MODE_PRIVATE);
		currentFontPosition = sharedPrefFont.getInt("font_pos", 0);
		Log.i("", "Current Font : " + fontFiles[currentFontPosition]);

		for (int i = 0; i < fontNames.length; i++)
		{
			fontNames[i] = context.getResources().getString(R.string.app_name);
		}

		FontListAdapter fontListAdapter = new FontListAdapter(context, fontNames, fontFiles);

		listPopupWindow = new ListPopupWindow(context);
		listPopupWindow.setAdapter(fontListAdapter);
		listPopupWindow.setAnchorView(ancherView);
		listPopupWindow.setModal(true);
		// listPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
		listPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(
				R.drawable.action_bar_bg));
		listPopupWindow.setListSelector(null);
		listPopupWindow.setOnItemClickListener(this);
		listPopupWindow.setContentWidth(measureContentWidth(fontListAdapter));
	}

	public void show()
	{
		listPopupWindow.show();
	}

	private void saveFontPosition(int position)
	{
		Editor editor = sharedPrefFont.edit();
		editor.putInt("font_pos", position);
		editor.commit();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long arg3)
	{
		currentFontPosition = position;
		saveFontPosition(position);
		if (parentLayouts.size() > 0)
		{
			for (ViewGroup viewGroup : parentLayouts)
			{
				setFontToAllChilds(viewGroup);
			}
		}
		listPopupWindow.dismiss();

	}

	public void addViewGroupToChangeFont(ViewGroup viewGroup)
	{
		parentLayouts.add(viewGroup);
	}

	public void setFontToAllChilds(ViewGroup parentLayout)
	{
		try
		{
			Typeface typeFace = null;
			int childCount = parentLayout.getChildCount();
			//Log.i(TAG, "setFontToAllChilds() applying font - " + fontFiles[currentFontPosition]);
			if (currentFontPosition == 0)
			{
				typeFace = Typeface.DEFAULT;
			}
			else
			{
				typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/"
						+ fontFiles[currentFontPosition]);
			}

			for (int i = 0; i < childCount; ++i)
			{
				View child = parentLayout.getChildAt(i);
				if (child instanceof ViewGroup)
					setFontToAllChilds((ViewGroup) child);
				else if (child instanceof TextView)
					((TextView) child).setTypeface(typeFace);
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, "setFontToAllChilds() - " + e.toString());
		}

	}

	public void setFont(TextView textView)
	{
		try
		{
			Typeface typeFace = null;
			//Log.i(TAG, "setFont() applying font - " + fontFiles[currentFontPosition]);
			if (currentFontPosition == 0)
			{
				typeFace = Typeface.DEFAULT;
			}
			else
			{
				typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/"
						+ fontFiles[currentFontPosition]);
			}
			textView.setTypeface(typeFace);
		}
		catch (Exception e)
		{
			Log.e(TAG, "setFont() - " + e.toString());
		}

	}

	private int measureContentWidth(ListAdapter listAdapter)
	{
		ViewGroup mMeasureParent = null;
		int maxWidth = 0;
		View itemView = null;
		int itemType = 0;

		final ListAdapter adapter = listAdapter;
		final int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		final int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		final int count = adapter.getCount();
		for (int i = 0; i < count; i++)
		{
			final int positionType = adapter.getItemViewType(i);
			if (positionType != itemType)
			{
				itemType = positionType;
				itemView = null;
			}

			if (mMeasureParent == null)
			{
				mMeasureParent = new FrameLayout(context);
			}

			itemView = adapter.getView(i, itemView, mMeasureParent);
			itemView.measure(widthMeasureSpec, heightMeasureSpec);

			final int itemWidth = itemView.getMeasuredWidth();

			if (itemWidth > maxWidth)
			{
				maxWidth = itemWidth;
			}
		}

		return maxWidth + 30;
	}

}
