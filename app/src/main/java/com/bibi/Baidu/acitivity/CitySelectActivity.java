package com.bibi.Baidu.acitivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bibi.Baidu.Bean.Citys;
import com.bibi.Baidu.R;
import com.bibi.Baidu.adapter.CityAdapter;
import com.bibi.Baidu.adapter.SortAdapter;
import com.bibi.Baidu.database.HistoryCityDataBase;
import com.bibi.Baidu.utils.CharacterParser;
import com.bibi.Baidu.utils.PinyinComparator;
import com.bibi.Baidu.utils.SideBar;
import com.bibi.Baidu.utils.SortModel;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bibinet on 2016/10/31.
 */
@ContentView(R.layout.activity_cityselectx)
public class CitySelectActivity extends BaseActivity {
    @ViewInject(R.id.history_city)
    private GridView history_city;
    @ViewInject(R.id.location)
    private Button location;
    @ViewInject(R.id.city_listview)
    private ListView city_listivew;
    @ViewInject(R.id.sidebar)
    private SideBar sideBar;
    private List<Citys> datas = new ArrayList<>();
    private List<Citys> currentcity_list = new ArrayList<>();
    private List<Citys> historycity_list = new ArrayList<>();
    private List<Citys> hotcity_list = new ArrayList<>();
    private List<Citys> allcity_list = new ArrayList<>();

    private List<SortModel> sortlist=new ArrayList<>();

    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    private SortAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initdata();
        initview();
        setlitioner();
        //  HistoryCityAdapter adapter=new HistoryCityAdapter(datas,this);
        //history_city.setAdapter(adapter);
    }

    private void setlitioner() {
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    city_listivew.setSelection(position);
                }

            }
        });
    }

    private void initview() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        String[] citys=new String[allcity_list.size()];
        for (int i=0;i<allcity_list.size();i++){
            citys[i]=allcity_list.get(i).getName();
        }
        sortlist=filledData(citys);
        Collections.sort(sortlist, pinyinComparator);
         adapter = new SortAdapter(currentcity_list, historycity_list, hotcity_list, sortlist, this);
        city_listivew.setAdapter(adapter);
    //   adapter = new SortAdapter(this, sortlist);
    }

    private void initdata() {
        location.setText("选择城市");
        HistoryCityDataBase dataBasehelper = new HistoryCityDataBase(this, "historycitytable.db", null, 1);
        SQLiteDatabase database = dataBasehelper.getReadableDatabase();
        String sql = "select * from historycitytable";
        Cursor cusor = database.rawQuery(sql, null);
        if (cusor.moveToNext()) {
            String histroycity = cusor.getString(cusor.getColumnIndex("historycity"));
            historycity_list.add(new Citys(histroycity));
        }
        currentcity_list.add(new Citys("太原"));

        hotcity_list.add(new Citys("太原"));
        hotcity_list.add(new Citys("北京"));
        hotcity_list.add(new Citys("大连"));
        hotcity_list.add(new Citys("天津"));
        hotcity_list.add(new Citys("上海"));

        allcity_list.add(new Citys("武汉"));
        allcity_list.add(new Citys("贵州"));
        allcity_list.add(new Citys("南昌"));
        allcity_list.add(new Citys("忻州"));
        allcity_list.add(new Citys("兰州"));
        allcity_list.add(new Citys("贵阳"));
        allcity_list.add(new Citys("太原"));
        allcity_list.add(new Citys("河北"));
        allcity_list.add(new Citys("河南"));
        allcity_list.add(new Citys("高平"));
        allcity_list.add(new Citys("武昌"));
        allcity_list.add(new Citys("深圳"));
        allcity_list.add(new Citys("北京"));


        //  datas.add(new Citys("太原"));
        // datas.add(new Citys("北京"));
        //datas.add(new Citys("天津"));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
   /*     if(requestCode==999){
            if(resultCode==555){
                String cityname = (String) data.getSerializableExtra("selectedcity");
                GeoCoder geoCoder=GeoCoder.newInstance();
                GeoCodeOption option=new GeoCodeOption();
                option.city(cityname);
                geoCoder.geocode(option);
                geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {


                    }

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                        LatLng center = reverseGeoCodeResult.getLocation();
                    }
                });


            }
        }*/


    }

    /**
     * 为ListView填充数据
     * @param date
     * @return
     */
    private List<SortModel> filledData(String [] date){
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0; i<date.length; i++){
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }
}
