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
 * Created by bibinet on 2016/10/31.
 */
public class HistoryCityAdapter extends BaseAdapter {
    private Context context;
    private List<Citys> citys=new ArrayList<>();

    public HistoryCityAdapter(List<Citys> citys, Context context) {
        this.citys = citys;
        this.context = context;
    }

    @Override
    public int getCount() {
        return citys.size();
    }

    @Override
    public Object getItem(int position) {
        return citys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=LayoutInflater.from(context).inflate(R.layout.item_city,null);
             viewHolder.textview = (TextView) convertView.findViewById(R.id.city_name);
            convertView.setTag(viewHolder);
        }else{
          viewHolder =(ViewHolder) convertView.getTag();
        }
        viewHolder.textview.setText(citys.get(position).getName());
        return convertView;
    }
    class ViewHolder{
        TextView textview;
    }
}
