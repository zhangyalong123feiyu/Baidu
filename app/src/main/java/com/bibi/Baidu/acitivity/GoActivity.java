package com.bibi.Baidu.acitivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.bibi.Baidu.Appliction.MyTextWatcher;
import com.bibi.Baidu.Bean.AddrItem;
import com.bibi.Baidu.R;
import com.bibi.Baidu.adapter.SearchAddressAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by bibinet on 2016/10/29.
 */
@ContentView(R.layout.activity_go)
public class GoActivity extends BaseActivity{
    @ViewInject(R.id.search)
    private EditText search;
    @ViewInject(R.id.cacel)
    private TextView cacel;
    @ViewInject(R.id.searchcitylistview)
    private ListView searcitylsitiview;
    private PoiSearch poisearch;
    private SuggestionSearch mSuggestionSearch;
    private SearchAddressAdapter adapter;
    private View v;
    private PoiSearch mpoisearch;
    private List<AddrItem> hislists=new ArrayList<>();
    private LinearLayout home;
    private LinearLayout company;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        initpoi();
        poisearh();
        initlistioner();
    }

    private void initview() {
        v=LayoutInflater.from(GoActivity.this).inflate(R.layout.item_addselect,null);
         home = (LinearLayout) v.findViewById(R.id.home);
        company=(LinearLayout)v.findViewById(R.id.company);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(GoActivity.this,SetAddressActivity.class));
            }
        });
        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GoActivity.this,SetAddressActivity.class));
            }
        });

    }

    private void initlistioner() {
        cacel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finshActivity();
            }
        });

    }

    private void initpoi() {

        SharedPreferences preferences = GoActivity.this.getSharedPreferences("hiscity", Context.MODE_PRIVATE);
        Set<String> hiscityaddr = preferences.getStringSet("hiscityaddr", null);
        if (hiscityaddr != null) {
            Gson gson = new Gson();
            for (String json : hiscityaddr) {
                try {
                    AddrItem item = gson.fromJson(json, AddrItem.class);
                    hislists.add(item);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        }

        searcitylsitiview.addHeaderView(v);
        adapter = new SearchAddressAdapter(GoActivity.this, hislists);
        searcitylsitiview.setAdapter(adapter);
        search.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    searcitylsitiview.removeHeaderView(v);
                    mSuggestionSearch.requestSuggestion(new SuggestionSearchOption().city("太原").keyword(s.toString()));
                }
                if (s.length()<=0){
                    searcitylsitiview.addHeaderView(v);
                    adapter = new SearchAddressAdapter(GoActivity.this, hislists);
                    searcitylsitiview.setAdapter(adapter);
                }

            }
        });

        mpoisearch=PoiSearch.newInstance();
        mpoisearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                List<PoiInfo> pois = poiResult.getAllPoi();
                if (pois == null || pois.size() <= 0) { // err
                    Log.d("TAGx-poi", "err");
                    return;
                }
                for (PoiInfo info : pois) {
                    Log.d("TAGx-poi", info.name + " " + info.address);
                    hislists.add(new AddrItem(info.location.latitude, info.location.longitude, info.name, info.address));
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });




        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            public void onGetSuggestionResult(SuggestionResult res) {
                if (res == null || res.getAllSuggestions() == null) {
                    // 未找到相关结果
                    //listview.setVisibility(View.GONE);
                    return;
                }
                // 获取在线建议检索结果
                List<AddrItem> lists = new ArrayList<AddrItem>();
                for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
                    Log.d("TAGx-Suggestion", info.uid + " " + info.key);
                    if (info.pt == null) {
                        continue;
                    }
                    lists.add(new AddrItem(info.pt.latitude, info.pt.longitude, info.key, info.district));
                }
                adapter = new SearchAddressAdapter(GoActivity.this, lists);
              searcitylsitiview.setAdapter(adapter);
            }
        });

    }

    private void poisearh() {


    }
}
