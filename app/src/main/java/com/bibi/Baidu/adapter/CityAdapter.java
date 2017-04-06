package com.bibi.Baidu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.bibi.Baidu.Bean.Citys;
import com.bibi.Baidu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bibinet on 2016/10/31.
 */
public class CityAdapter extends BaseAdapter {
    private Context context;
    private List<Citys> currentcity_list = new ArrayList<>();
    private List<Citys> historycity_lsit = new ArrayList<>();
    private List<Citys> hotcity_list = new ArrayList<>();
    private List<Citys> allcity_list = new ArrayList<>();


    public CityAdapter(List<Citys> currentcity_list, List<Citys> historycity_lsit, List<Citys> hotcity_list, List<Citys> allcity_list, Context context) {
        this.currentcity_list = currentcity_list;
        this.historycity_lsit = historycity_lsit;
        this.hotcity_list = hotcity_list;
        this.allcity_list = allcity_list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3 + allcity_list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {

        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position < 3 ? 0 : 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (getItemViewType(position) == 0) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_citylayout, null);
            TextView textview = (TextView) v.findViewById(R.id.textview);
            GridView gridView = (GridView) v.findViewById(R.id.city);

            if (position == 0) {
                textview.setText("当前定位城市");
                GridAdapter adapter = new GridAdapter(context, currentcity_list);
                gridView.setAdapter(adapter);
            } else if (position == 1) {
                textview.setText("历史访问城市");
                GridAdapter adapter = new GridAdapter(context, historycity_lsit);
                gridView.setAdapter(adapter);
                int totalHeight = 0;
                for(int i=0;i<adapter.getCount()%3;i++) {
                    View viewItem = adapter.getView(i, null, gridView);//这个很重要，那个展开的item的measureHeight比其他的大
                    viewItem.measure(0, 0);
                    totalHeight += viewItem.getMeasuredHeight();
                }

                ViewGroup.LayoutParams params = gridView.getLayoutParams();
                params.height = totalHeight
                        + (gridView.getColumnWidth() * (gridView.getCount() - 1));
                gridView.setLayoutParams(params);
                adapter.notifyDataSetChanged();
            } else if (position == 2) {
                textview.setText("热门城市");
                GridAdapter adapter = new GridAdapter(context, hotcity_list);
                gridView.setAdapter(adapter);
                int totalHeight = 0;
                for(int i=0;i<adapter.getCount()%3;i++) {
                    View viewItem = adapter.getView(i, null, gridView);//这个很重要，那个展开的item的measureHeight比其他的大
                    viewItem.measure(0, 0);
                    totalHeight += viewItem.getMeasuredHeight();
                }
                ViewGroup.LayoutParams params = gridView.getLayoutParams();
                params.height = totalHeight
                        + (gridView.getColumnWidth() * (gridView.getCount() - 1));
                gridView.setLayoutParams(params);
                adapter.notifyDataSetChanged();
            }
            return v;
        } else if (getItemViewType(position) == 1) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_cityname, null);
           // TextView cityname = (TextView) v.findViewById(R.id.city_textview);
            String name = allcity_list.get(position-3).getName();
            //cityname.setText(name);
            return v;
        }
        return null;
    }

}


