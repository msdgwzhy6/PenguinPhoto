package com.jamesdev.penguinphoto.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.jamesdev.penguinphoto.R;
import com.jamesdev.penguinphoto.view.PhotoViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by Bengpeng.Jiang.
 */
public class PhotoItemAdapter extends BaseAdapter {
    private static final String TAG = "PhotoItemAdapter";

    private Context mContext;
    private  List<Map<String, String>> mPhotoData;


    /**
     * constructor
     * @param context
     * @param photos
     */
    public PhotoItemAdapter(Context context, List<Map<String, String>> photos) {
        mContext = context;
        mPhotoData = photos;
    }
    @Override
    public int getCount() {
        return mPhotoData.size();
    }

    @Override
    public Object getItem(int view) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoViewHolder holder;
        View view = convertView;

        if (convertView == null) {
            view = newView(R.layout.item_grid_image, parent, false);
            holder = new PhotoViewHolder(mContext, this, position);
            view.setTag(holder);
        } else {
            holder = (PhotoViewHolder)view.getTag();
        }

        bindView(position, view, holder);
        return view;
    }

    private View newView(int resource, ViewGroup parent, boolean attachToRoot) {
        View  view = ((Activity) mContext).getLayoutInflater().inflate(resource, parent, attachToRoot);
        return view;
    }

    private void bindView(int position, View view, PhotoViewHolder holder) {
        if (view == null) {
            throw new RuntimeException("item view error in position:" + position);
        }

        holder.mImageView = (ImageView) view.findViewById(R.id.image);
        holder.mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        holder.mDeleteImageView = (ImageView)view.findViewById(R.id.deleteImageView);

        String thumbName;


    }

    /**
     * refresh data in adapter
     */
    public void refresh() {
        this.notifyDataSetChanged();
    }
}
