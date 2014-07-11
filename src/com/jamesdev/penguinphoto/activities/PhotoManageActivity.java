package com.jamesdev.penguinphoto.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import com.jamesdev.penguinphoto.R;
import com.jamesdev.penguinphoto.adapter.PhotoItemAdapter;
import com.jamesdev.penguinphoto.adapter.PhotoWallAdapter;
import com.jamesdev.penguinphoto.util.Images;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/7/11.
 */
public class PhotoManageActivity extends Activity {
    private static final String TAG = "MainActivity";

    private GridView mGridView;
    private Button mSkinEditButton;
    private List<Map<String, String>> mPhotoData;
    private PhotoItemAdapter mPhotoItemAdapter;
    private boolean mEditMode = false;
    private PhotoWallAdapter mPhotoWallAdapter;
    private GridView mPhotoWall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_photo);

  /*      mPhotoData = new ArrayList<Map<String, String>>();
        mPhotoItemAdapter = new PhotoItemAdapter(this, mPhotoData);

        mSkinEditButton = (Button)findViewById(R.id.button_skin_edit);
        mSkinEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button self = (Button)view;
                if (mEditMode) {
                    self.setText(getString(R.string.edit));
                } else {
                    self.setText(getString(R.string.save));
                }
                mPhotoItemAdapter.refresh();
            }
        });*/

/*        mGridView = (GridView)findViewById(R.id.gridViewSkin);
        mGridView.setAdapter(mPhotoItemAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                PhotoItemAdapter skinViewHolder = (PhotoItemAdapter)view.getTag();
                if (mEditMode) {
                    //do edit
                } else  {
                    //do update load
                }
            }
        });*/

        mPhotoWall = (GridView)findViewById(R.id.gridView);
        mPhotoWallAdapter = new PhotoWallAdapter(this, 0, Images.imageThumbUrls, mPhotoWall);
        mPhotoWall.setAdapter(mPhotoWallAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出程序时结束所有的下载任务
        mPhotoWallAdapter.cancelAllTasks();
    }
}