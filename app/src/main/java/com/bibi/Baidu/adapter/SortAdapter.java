package com.bibi.Baidu.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.bibi.Baidu.Bean.Citys;
import com.bibi.Baidu.R;
import com.bibi.Baidu.acitivity.CitySelectActivity;
import com.bibi.Baidu.utils.SortModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bibinet on 2016/10/31.
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private Context context;
    private List<Citys> currentcity_list = new ArrayList<>();
    private List<Citys> historycity_lsit = new ArrayList<>();
    private List<Citys> hotcity_list = new ArrayList<>();
    private List<SortModel> allcity_list = new ArrayList<>();


    public SortAdapter(List<Citys> currentcity_list, List<Citys> historycity_lsit, List<Citys> hotcity_list, List<SortModel> allcity_list, Context context) {
        this.currentcity_list = currentcity_list;
        this.historycity_lsit = historycity_lsit;
        this.hotcity_list = hotcity_list;
        this.allcity_list = allcity_list;
        this.context = context;
    }
    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param
     */
    public void updateListView(List<SortModel> allcity_list){
        this.allcity_list = allcity_list;
        notifyDataSetChanged();
   }


    @Override
    public int getCount() {
        Log.d("TAG",3 + allcity_list.size()+"");
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.i("TAG__getview",position+"====");

        if (getItemViewType(position) == 0) {

             convertView = LayoutInflater.from(context).inflate(R.layout.item_citylayout, null);
            TextView textview = (TextView) convertView.findViewById(R.id.textview);
            GridView gridView = (GridView) convertView.findViewById(R.id.city);
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
            return convertView;
        } else if (getItemViewType(position) == 1) {
          /*  View v = LayoutInflater.from(context).inflate(R.layout.item_cityname, null);
            TextView cityname = (TextView) v.findViewById(R.id.city_textview);
            String name = allcity_list.get(position-3).getName();
            cityname.setText(name)*/;

            ViewHolder viewHolder = null;
            final SortModel mContent = allcity_list.get(position-3);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_cityname, null);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
                viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position-3);

            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if(position-3 == getPositionForSection(section)){
                viewHolder.tvLetter.setVisibility(View.VISIBLE);
                viewHolder.tvLetter.setText(mContent.getSortLetters());
            }else{
                viewHolder.tvLetter.setVisibility(View.GONE);
            }
            viewHolder.tvTitle.setText(this.allcity_list.get(position-3).getName());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent data=new Intent();
                    data.putExtra("selectedcity",allcity_list.get(position-3).getName());
                    ((CitySelectActivity)(context)).setResult(555,data);
                    ((CitySelectActivity)(context)).finshActivity();
                }
            });


            return convertView;
        }
        return null;
    }

    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < allcity_list.size(); i++) {
            String sortStr = allcity_list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return allcity_list.get(position).getSortLetters().charAt(0);
    }
    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String  sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}


