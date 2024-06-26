package org.android.swamipothi.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class BaseTask extends AsyncTask<Object, Integer, Object> {

	ProgressDialog progressDialog = null;
	Activity activity = null;
	String message = null;

	public BaseTask(Activity activity, String title, String message,
			boolean showProgress) {
		this.activity = activity;
		this.message = message;

		progressDialog = new ProgressDialog(activity);
		if (showProgress) {
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		}

		if (title != null && !title.trim().equals("")) {
			progressDialog.setTitle(title);
		}

		if (message != null && !message.trim().equals("")) {
			progressDialog.setMessage(message);
		}

		progressDialog.setCancelable(false);
		showProgressBar();
	}

	public BaseTask(Activity activity, String title, String message,
			boolean showProgress, int progressType) {

		this.activity = activity;
		this.message = message;

		progressDialog = new ProgressDialog(activity);

		if (showProgress) {
			try {
				progressDialog.setProgressStyle(progressType);
				progressDialog.setMax(100);
				progressDialog.setProgress(0);
			} catch (Exception e) {
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			}
		}

		if (title != null && !title.trim().equals("")) {
			progressDialog.setTitle(title);
		}

		if (message != null && !message.trim().equals("")) {
			progressDialog.setMessage(message);
		}

		progressDialog.setCancelable(false);
		showProgressBar();
	}

	protected void showProgressBar() {
		if (progressDialog != null && !progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	@Override
	protected void onPreExecute() {
		progressDialog.show();
		super.onPreExecute();
	}

	@Override
	protected Object doInBackground(Object... params) {
		return null;

	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		progressDialog.setProgress(values[0]);
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Object result) {
		progressDialog.dismiss();
		super.onPostExecute(result);
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		
	}

}
