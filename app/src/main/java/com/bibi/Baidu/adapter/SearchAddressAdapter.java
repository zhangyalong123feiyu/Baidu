package com.bibi.Baidu.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bibi.Baidu.Bean.AddrItem;
import com.bibi.Baidu.R;
import com.bibi.Baidu.acitivity.GoActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bibinet on 2016/11/1.
 */
public class SearchAddressAdapter extends BaseAdapter {
    private Context context;
    private List<AddrItem> addres=new ArrayList<>();

    public SearchAddressAdapter(Context context, List<AddrItem> addres) {
        this.context = context;
        this.addres = addres;
    }

    @Override
    public int getCount() {
        return addres.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_addres,null);
            viewHolder.textname= (TextView) convertView.findViewById(R.id.add_name);
            viewHolder.textaddres= (TextView) convertView.findViewById(R.id.addres);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        String name = addres.get(position).name;
        String city=addres.get(position).address;
        viewHolder.textname.setText(name);
        viewHolder.textaddres.setText(city);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson=new Gson();
                String json_addr= gson.toJson(addres.get(position));
                savehiscity(json_addr);
                Intent data=new Intent();
                data.putExtra("addresitem",addres.get(position));
                ((GoActivity)(context)).setResult(200, data);
                ((GoActivity)(context)).finshActivity();
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView textname,textaddres;
    }

    /**
     * 保存历史访问城市
     *
     * @param
     *
     */
    private void savehiscity(String json_addr) {
        SharedPreferences preferences = context.getSharedPreferences("hiscity", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Set<String> hiscityaddr = preferences.getStringSet("hiscityaddr", null);
        if (hiscityaddr == null) {
            hiscityaddr = new HashSet<String>();
        }
        hiscityaddr.add(json_addr);

        editor.clear();
        editor.putStringSet("hiscityaddr", hiscityaddr);
        editor.commit();
    }

}
