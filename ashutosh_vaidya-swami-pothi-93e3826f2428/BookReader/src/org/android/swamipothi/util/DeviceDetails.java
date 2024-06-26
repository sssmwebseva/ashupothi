package org.android.swamipothi.util;

import android.content.Context;
import android.content.res.Configuration;

public class DeviceDetails
{
	private static String getSizeName(Context context)
	{
		int screenLayout = context.getResources().getConfiguration().screenLayout;
		screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

		switch (screenLayout)
		{
			case Configuration.SCREENLAYOUT_SIZE_SMALL:
				return "small";
			case Configuration.SCREENLAYOUT_SIZE_NORMAL:
				return "normal";
			case Configuration.SCREENLAYOUT_SIZE_LARGE:
				return "large";
			case Configuration.SCREENLAYOUT_SIZE_XLARGE:
				return "xlarge";
			default:
				return "undefined";
		}
	}
}
