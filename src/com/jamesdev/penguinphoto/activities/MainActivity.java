package com.jamesdev.penguinphoto.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.*;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.jamesdev.penguinphoto.R;
import com.jamesdev.penguinphoto.activities.user.PickImageHelper;
import com.jamesdev.penguinphoto.app.AppConfig;
import com.jamesdev.penguinphoto.data.PhotoDataSource;
import com.jamesdev.penguinphoto.model.Photo;
import com.jamesdev.penguinphoto.util.*;

import java.io.File;
import java.util.List;

public class MainActivity extends Activity implements AMapLocationListener, LocationSource, Runnable {
    private static final String TAG = MainActivity.class.getSimpleName();
    private AMap aMap;
    private MapView mMapView;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager = null;
    private TextView mTextViewLocation;
    private AMapLocation mAMapLocation;
    private Handler handler = new Handler();

    private static final int REQUEST_TAKE_PHOTO = 1;
    public static final String PHOTO_PATH_FORMAT = AppConfig.getPhotoFolderPath() + File.separator + "%s.jpg";
    private PickImageHelper mPickImageHelper;
    private String mPhotoPath;
    private String mPhotoName;
    private long mPhotoTakeTime;
    private String mLocationDes;
    private Double mGeoLat;
    private Double mGeoLng;

    private PhotoDataSource mPhotoDataSource;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        init();

        Button takePhoto = (Button) findViewById(R.id.button_take_photo);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoName = generatePhotoName();
                String outputPath = String.format(PHOTO_PATH_FORMAT, mPhotoName);

                getPickImageHelper().pickImageFromCamera(
                        getString(R.string.take_photo), REQUEST_TAKE_PHOTO,
                        DisplayUtils.getWidthPixels(), DisplayUtils.getHeightPixels(),
                        outputPath);
            }
        });

        findViewById(R.id.button_log_db).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Photo> photos = mPhotoDataSource.getAllPhotos();
                for (Photo photo : photos) {
                    Log.d(TAG, photo.toString());
                }
            }
        });

        findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        mPickImageHelper = new PickImageHelper(this);

        mTextViewLocation = (TextView) findViewById(R.id.text_view_location);


        mPhotoDataSource = new PhotoDataSource(this);
        mPhotoDataSource.open();
    }

    private void init() {
        if (null == aMap) {
            aMap = mMapView.getMap();
            setUpMap();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    private PickImageHelper getPickImageHelper() {
        if (mPickImageHelper == null) {
            mPickImageHelper = new PickImageHelper(this);
        }
        return mPickImageHelper;
    }

    private void zoomInMap(int zoomLevel) {
        for (int i = 0; i < zoomLevel; i++) {
            changeCamera(CameraUpdateFactory.zoomIn(), null);
        }
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {
        aMap.animateCamera(update, 1000, callback);
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        mPhotoDataSource.open();
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        mPhotoDataSource.close();
        super.onPause();
        mMapView.onPause();

        deactivate();
    }

    private void stopLocation() {
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destory();
        }
        mAMapLocationManager = null;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    /**
     * 此方法已经废弃
     */
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void run() {
        if (mAMapLocation == null) {
            ToastUtil.show(this, "12秒内还没有定位成功，停止定位");
            mTextViewLocation.setText("12秒内还没有定位成功，停止定位");
            stopLocation();// 销毁掉定位
        }
    }



    /**
     * 混合定位回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation location) {
        if (mListener != null && location != null) {
            mListener.onLocationChanged(location);// 显示系统小蓝点
        }

        if (location != null) {
            this.mAMapLocation = location;// 判断超时机制
            mGeoLat = location.getLatitude();
            mGeoLng = location.getLongitude();
            String cityCode = "";
            Bundle locBundle = location.getExtras();
            if (locBundle != null) {
                cityCode = locBundle.getString("citycode");
                mLocationDes = locBundle.getString("desc");
            }
            String str = ("定位成功:(" + mGeoLng + "," + mGeoLat + ")"
                    + "\n精    度    :" + location.getAccuracy() + "米"
                    + "\n定位方式:" + location.getProvider() + "\n定位时间:"
                    + AMapUtil.convertToTime(location.getTime()) + "\n城市编码:"
                    + cityCode + "\n位置描述:" + mLocationDes + "\n省:"
                    + location.getProvince() + "\n市:" + location.getCity()
                    + "\n区(县):" + location.getDistrict() + "\n区域编码:" + location
                    .getAdCode());
            mTextViewLocation.setText(str);

            zoomInMap(4);
            deactivate();
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
            mAMapLocationManager.requestLocationUpdates(
                    LocationProviderProxy.AMapNetwork, 2000, 10, this);
            handler.postDelayed(this, 12000);// 设置超过12秒还没有定位到就停止定位

        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destory();
        }
        mAMapLocationManager = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:
                    mPhotoDataSource.open();

                    mPhotoDataSource.createPhoto("钓鱼", mPhotoName, mPhotoTakeTime, mLocationDes, mGeoLat, mGeoLng);
                    break;
                default:
                    break;
            }
        }
    }

    private void insertToDb(String type, String name, long createTime, String locationDescription,
                            double latitude, double longitude) {
        Photo photo = mPhotoDataSource.createPhoto(type, name, createTime, locationDescription, latitude, longitude);

    }


    private String generatePhotoName() {
        String photoName;
        mPhotoTakeTime = TimeUtil.getCurrentTimeMillis();
        String nameString = mPhotoTakeTime + mGeoLat.toString() + mGeoLng.toString();
        photoName = EncodeUtils.Encrypt(nameString, null);
        return photoName;
    }
}
