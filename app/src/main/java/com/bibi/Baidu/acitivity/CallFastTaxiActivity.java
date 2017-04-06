package com.bibi.Baidu.acitivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.bibi.Baidu.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Lenovo_user on 2016/11/1.
 */
@ContentView(R.layout.activity_callfasttaxi)
public class CallFastTaxiActivity extends BaseActivity {
    @ViewInject(R.id.mapview)
    private MapView mapview;
    @ViewInject(R.id.circle1)
    private ImageView circle1;
    @ViewInject(R.id.circle2)
    private  ImageView circle2;
    @ViewInject(R.id.circle3)
    private  ImageView circle3;
    @ViewInject(R.id.search_text)
    private TextView search_text;
    @ViewInject(R.id.location)
    private Button location;
    @ViewInject(R.id.image_left)
    private ImageView image_left;
    private BaiduMap baidumap;
    private LocationClient locationc_client;
    private boolean isfirstloc=true;
    private MyLocationData locdata;
    private CallFastTaxiActivity activity;
    public BDLocationListener mylocationlistioner=new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if(bdLocation==null||mapview==null){
                return;
            }
            if (isfirstloc){
                isfirstloc=false;
                locdata=new MyLocationData.Builder().accuracy(bdLocation.getRadius())
                        .direction(100).latitude(bdLocation.getLatitude()).longitude(bdLocation.getLongitude())
                        .build();//设置定位数据

                LatLng ll=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                MapStatusUpdate update= MapStatusUpdateFactory.newLatLngZoom(ll,18);
                baidumap.animateMapStatus(update);
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        activity=this;
        initview();
        initlocation();
        setlistioner();
    }


    private void initview() {
        location.setText("等待应答");
        image_left.setImageResource(R.mipmap.login_returen);
    }
    private void setlistioner() {
        image_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finshActivity();
            }
        });
    }

    private void initlocation() {
        baidumap=mapview.getMap();
        baidumap.setMyLocationEnabled(true);
        // 取消手势操作
        UiSettings uiSettings = baidumap.getUiSettings();
        uiSettings.setAllGesturesEnabled(false);

        // 隐藏缩放控件
        mapview.showZoomControls(false);

        // 删除百度地图logo
        mapview.removeViewAt(1);
        locationc_client=new LocationClient(getApplicationContext());

        locationc_client.registerLocationListener(mylocationlistioner);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        locationc_client.setLocOption(option);

        locationc_client.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAnim();
        mapview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapview.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
    }

    private void startAnim() {
        circle1.setAlpha(0.0f);
        circle2.setAlpha(0.0f);
        circle3.setAlpha(0.0f);

        new Thread(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        circle1.setAlpha(1.0f);
                        circleAnim(circle1);
                    }
                });

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        circle2.setAlpha(1.0f);
                        circleAnim(circle2);
                    }
                });

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        circle3.setAlpha(1.0f);
                        circleAnim(circle3);
                    }
                });
            }
        }).start();
    }

    private void circleAnim(final View view) {
        // 加载动画布局文件
        final Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.circle_alpha);
        // 设置动画监听事件
        loadAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(loadAnimation);
            }
        });
        // 通过控件启动动画
        view.startAnimation(loadAnimation);
    }

    }

