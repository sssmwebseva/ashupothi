package org.android.swamipothi.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

public class FileUtil
{
	public static String assetFileToString(Context context,String assetFileName)
	{
		InputStream inputStream = null;
		try
		{
			inputStream = context.getAssets().open(assetFileName);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			int size = inputStream.available();
			char[] buffer = new char[size];
			inputStreamReader.read(buffer);
			return new String(buffer);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
