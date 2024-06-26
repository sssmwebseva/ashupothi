package org.android.swamipothi.util;

import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class MyGestureDetector extends SimpleOnGestureListener
{

	public interface IDirection
	{
		void flipUp();

		void flipDown();
	}

	private static final int SWIPE_MIN_DISTANCE = 60;//120;
	private static final int SWIPE_MAX_OFF_PATH = 500;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	private IDirection direction;

	private MyGestureDetector()
	{
		
	}
	
	public MyGestureDetector(IDirection direction)
	{
		this.direction = direction;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		try
		{
			//Log.i("", "onFling()");
			if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
			{
				Log.i("","out of path.");
				return false;
			}
			// right to left swipe
			/*if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
			{
				Log.i("", "Left Swing.");
			}
			// left to right swipe
			else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
			{
				Log.i("", "Right Swing.");
			}*/

			else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
			{
				Log.i("", "Up Swing.");
				direction.flipUp();
			}

			else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
			{
				Log.i("", "Down Swing.");
				direction.flipDown();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
}