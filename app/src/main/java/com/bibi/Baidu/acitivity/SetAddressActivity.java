package com.bibi.Baidu.acitivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
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

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by bibinet on 2016/11/2.
 */
@ContentView(R.layout.activity_setaddres)
public class SetAddressActivity extends BaseActivity {
    @ViewInject(R.id.search)
    private EditText search;
    @ViewInject(R.id.setaddres_list)
    private ListView setaddres_list;
    private PoiSearch mpoisearch;
    private List<AddrItem> hislists=new ArrayList<>();
    private SearchAddressAdapter adapter;
    private SuggestionSearch mSuggestionSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initpoi();
        initsearch();
    }

    private void initpoi() {
        mpoisearch= PoiSearch.newInstance();
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
                adapter = new SearchAddressAdapter(SetAddressActivity.this, lists);
               setaddres_list.setAdapter(adapter);
            }
        });

    }
    private void initsearch() {
        search.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (s.length()>0){
                    mSuggestionSearch.requestSuggestion(new SuggestionSearchOption().city("太原").keyword(s.toString()));
                }
                if (s.length()<=0){
                    adapter = new SearchAddressAdapter(SetAddressActivity.this, hislists);
                    setaddres_list.setAdapter(adapter);
                }

            }
        });


    }
}
