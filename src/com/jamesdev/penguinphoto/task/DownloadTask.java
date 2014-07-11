package com.jamesdev.penguinphoto.task;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.jamesdev.penguinphoto.util.Constants;
import com.jamesdev.penguinphoto.view.PhotoViewHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadTask extends AsyncTask<Void, Integer, Integer> {
	private static final String TAG = "DownLoadTask";
	private PhotoViewHolder mPhotoViewHolder;
	private String mFileName;
	private Integer mFileSize;
	private Context mContext;

    /**
     * Constructor
     * @param context
     * @param skinViewHolder
     * Throws RuntimeException, IOException
     */
	public DownloadTask(Context context, PhotoViewHolder skinViewHolder) {
		super();
		this.mContext = context;
		this.mPhotoViewHolder = skinViewHolder;
		this.mFileName = skinViewHolder.getThumbName();
	}

	@Override
	protected Integer doInBackground(Void... params) {
		try {
			URL url = new URL(mPhotoViewHolder.mPhotoUrl);
			URLConnection urlConn = url.openConnection();
			
			InputStream is = urlConn.getInputStream();
			mFileSize = urlConn.getContentLength();
			
			if (mFileSize <= 0) {
				throw new RuntimeException("cannot get file size");
			}
			
			if (is == null) {
				throw new RuntimeException("cannot get file");
			}
			
			StringBuilder fileName = new StringBuilder(mFileName);
            fileName.append(Constants.IMAGE_SUFFIX);
			String absoluteFilename = Constants.IMAGE_SUFFIX + fileName.toString();
			
			File targetFile = new File(absoluteFilename);
			if (targetFile.exists()) {
				targetFile.delete();
			}
			
			try {
				targetFile.createNewFile();
			} catch (IOException e) {
				Log.e(TAG, "create file error: " + targetFile.getPath());
				e.printStackTrace();
			}
			
			FileOutputStream fOS = new FileOutputStream(absoluteFilename);
			byte buf[] = new byte[1024];
			int downLoadFilePosition = 0;
			int numRead;
			
			while ((numRead = is.read(buf)) != -1) {
				fOS.write(buf, 0, numRead);
				downLoadFilePosition += numRead;
				publishProgress((int)((downLoadFilePosition/(float)mFileSize)*100));
			}
			
			try {
				is.close();
				fOS.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mFileSize;
	}
	
	@Override
	protected void onPreExecute() {
		Toast.makeText(mContext, "start download", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		mPhotoViewHolder.mProgressBar.setVisibility(View.GONE);
		Toast.makeText(mContext, "download compeleted", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress) {
		mPhotoViewHolder.mProgressBar.setProgress(progress[0]);
	}
	
}
