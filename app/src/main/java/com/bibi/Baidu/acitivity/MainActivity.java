package com.bibi.Baidu.acitivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bibi.Baidu.Bean.AddrItem;
import com.bibi.Baidu.R;
import com.bibi.Baidu.database.HistoryCityDataBase;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class MainActivity extends BaseActivity {
    @ViewInject(R.id.mapview)
    private MapView mapview;
    @ViewInject(R.id.image_left)
    private ImageView image_left;
    @ViewInject(R.id.image_right)
    private ImageView image_right;
    @ViewInject(R.id.drawer)
    private DrawerLayout drawer;
    @ViewInject(R.id.nowlocation)
    private TextView nowlocation;
    @ViewInject(R.id.where_go)
    private TextView wherego;
    @ViewInject(R.id.location)
    private Button location;
    @ViewInject(R.id.taxiprice)
    private TextView taxipric;
    @ViewInject(R.id.callfasttaxi)
    private TextView fasttaxi;
    @ViewInject(R.id.headphoto)
    private ImageView headphoto;
    private BaiduMap baidumap;
    private Marker marker;
    private LocationClient locationc_client;
    private boolean isfirstloc = true;
    private MyLocationData locdata;
    private String adress;
    private String lction;
    public BDLocationListener mylocationlistioner = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null || mapview == null) {
                return;
            }
            if (isfirstloc) {
                isfirstloc = false;
                locdata = new MyLocationData.Builder().accuracy(bdLocation.getRadius())
                        .direction(100).latitude(bdLocation.getLatitude()).longitude(bdLocation.getLongitude())
                        .build();//设置定位数据
                nowlocation.setText(bdLocation.getLocationDescribe());
                lction = bdLocation.getCity();

                HistoryCityDataBase helper = new HistoryCityDataBase(MainActivity.this, "historycitytable.db", null, 1);
                SQLiteDatabase db = helper.getReadableDatabase();
                ContentValues values = new ContentValues();
                values.put("historycity", lction);
                db.insert("historycitytable", null, values);
                db.close();


                LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll, 18);
                baidumap.animateMapStatus(update);
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);
        initiview();
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawer.setScrimColor(Color.TRANSPARENT);//消除drawerlayout的阴影效果
        initmap();
        setlisioner();
    }

    private void initiview() {
        ImageOptions options = new ImageOptions.Builder().setFailureDrawableId(R.mipmap.login_head).setUseMemCache(true).setCircular(true).build();
        x.image().bind(headphoto, "http://b.hiphotos.baidu.com/image/pic/item/d009b3de9c82d15825ffd75c840a19d8bd3e42da.jpg", options);
        //下载文件
        String url = "";

    }

    private void setlisioner() {
        image_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        image_left.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.openDrawer(Gravity.LEFT);

                }
                return true;
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CitySelectActivity.class);
                startActivityForResult(intent, 999);


            }
        });

        nowlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GoActivity.class);
                startActivityForResult(intent, 666);
            }
        });
        wherego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GoActivity.class);
                startActivityForResult(intent, 888);
            }
        });
        wherego.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                taxipric.setVisibility(View.VISIBLE);
                fasttaxi.setVisibility(View.VISIBLE);
                return true;
            }
        });
        fasttaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CallFastTaxiActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            if (requestCode == 666) {
                AddrItem info = (AddrItem) data.getSerializableExtra("addresitem");
                if (info.name != null) {
                    nowlocation.setText(info.name);
                }
                double lat = info.latitude;
                double lng = info.longitude;
                LatLng center = new LatLng(lat, lng);

                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(center)
                        .zoom(18)
                        .build();
                //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                //改变地图状态
                baidumap.animateMapStatus(mMapStatusUpdate);

            }
        }

        if (requestCode == 999) {
            if (resultCode == 555) {
                String cityname = data.getStringExtra("selectedcity");
                Toast.makeText(MainActivity.this, cityname, Toast.LENGTH_SHORT).show();


                GeoCoder geoCoder=GeoCoder.newInstance();
                GeoCodeOption option=new GeoCodeOption();
                option.address(cityname);
                option.city("繁峙");
                  geoCoder.geocode(option);
                geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                        LatLng center= geoCodeResult.getLocation();
                        MapStatus mMapStatus = new MapStatus.Builder()
                                .target(center)
                                .zoom(18)
                                .build();
                        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                        //改变地图状态
                        baidumap.animateMapStatus(mMapStatusUpdate);
                    }

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                        LatLng center = reverseGeoCodeResult.getLocation();
                        MapStatus mMapStatus = new MapStatus.Builder()
                                .target(center)
                                .zoom(18)
                                .build();
                        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                        //改变地图状态
                        baidumap.animateMapStatus(mMapStatusUpdate);

                    }
                });


            }
        }
    }

    private void initmap() {
        baidumap = mapview.getMap();
        mapview.showZoomControls(false);//隐藏地图缩放控件
        mapview.removeViewAt(1);//移除百度地图logo
        // baidumap.setTrafficEnabled(true);
// 开启定位图层
        baidumap.setMyLocationEnabled(true);

        initLocation();

        baidumap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                updatalocation(mapStatus);
            }
        });

        //单击地图时，地图移动到相应的位置
        baidumap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(latLng)
                        .zoom(18)
                        .build();
                //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                //改变地图状态
                baidumap.animateMapStatus(mMapStatusUpdate);

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                double lat = mapPoi.getPosition().latitude;
                double lng = mapPoi.getPosition().longitude;
                LatLng latLng = new LatLng(lat, lng);
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(latLng)
                        .zoom(18)
                        .build();
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                //改变地图状态
                baidumap.animateMapStatus(mMapStatusUpdate);
                wherego.setText(mapPoi.getName());
                return true;
            }
        });


    }

    private void updatalocation(MapStatus mapStatus) {
        LatLng center = mapStatus.target;
        GeoCoder mgecoder = GeoCoder.newInstance();
        mgecoder.reverseGeoCode(new ReverseGeoCodeOption().location(center));
        mgecoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult != null && reverseGeoCodeResult.getPoiList() != null && reverseGeoCodeResult.getPoiList().size() > 0) {
                    PoiInfo info = reverseGeoCodeResult.getPoiList().get(0);
                    String n = info.name;
                    wherego.setText(n);
                }
            }
        });
    }


    private void initLocation() {

        locationc_client = new LocationClient(getApplicationContext());

        locationc_client.registerLocationListener(mylocationlistioner);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
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
        nowlocation.setText(String.valueOf(adress));
    }


    /**
     * 添加marker
     */
    private void setMarker() {
        //定义Maker坐标点
        LatLng point = new LatLng(locdata.latitude, locdata.longitude);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.home_positioning_);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        baidumap.addOverlay(option);
    }

    @Override
    protected void onResume() {
        super.onResume();

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



}
