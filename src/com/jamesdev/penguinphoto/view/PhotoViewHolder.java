package com.jamesdev.penguinphoto.view;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.jamesdev.penguinphoto.task.DownloadTask;


/**
 * Created by Benpeng.Jiang
 */
public class PhotoViewHolder {
    private String mThumbName;
    private Context mContext;
    private BaseAdapter mBaseAdapter;
    private static final String TAG = "PhotoViewHolder";

    public ImageView mImageView;
    public ProgressBar mProgressBar;
    public ImageView mDeleteImageView;

    public String mPhotoUrl;

    public boolean mIsEmbeded;
    public boolean mIsLast;
    public int mPosition;


    /**
     * Constructor
     * @param context Activity which creates adapter
     * @param baseAdapter Adapter which holds this object
     * @param position position in adapter
     */
    public PhotoViewHolder(Context context, BaseAdapter baseAdapter, int position) {
        mContext = context;
        mBaseAdapter = baseAdapter;
        mPosition = position;
    }

    // called when clicked in view mode
    public void doResponse() {

    }

    // called when clicked in edit mode
    public void doEdit() {

    }

    private void startSkinDownload() {
        DownloadTask downloadTask = new DownloadTask(mContext, this);
        downloadTask.execute();
    }

    public String getThumbName() {
        return mThumbName;
    }
}