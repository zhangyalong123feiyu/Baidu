package com.bibi.Baidu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bibi.Baidu.Bean.Citys;
import com.bibi.Baidu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bibinet on 2016/11/2.
 */
public class GridAdapter extends BaseAdapter {
    private Context context;
    private List<Citys> data=new ArrayList<>();

    public GridAdapter(Context context, List<Citys> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_city,null);
        TextView textview = (TextView) view.findViewById(R.id.city_name);
        String name=data.get(position).getName();
        textview.setText(name);
        return view;
    }
}
