package org.android.swamipothi.util;

import android.content.Context;
import android.widget.Toast;

public class Alert
{
	public static void showToast(Context c, String message)
	{
		Toast.makeText(c, message, Toast.LENGTH_LONG).show();
	}
}
