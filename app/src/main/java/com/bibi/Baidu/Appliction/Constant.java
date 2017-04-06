package com.bibi.Baidu.Appliction;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.bibi.Baidu.Bean.AddrItem;
import com.bibi.Baidu.Bean.LoginResult.UserInfo;
import com.bibi.Baidu.Bean.MyResult.MyData;
import com.google.gson.Gson;

/**
 * Created by bibinet on 2016/11/3.
 */
public class Constant {
    public static UserInfo userInfo;
    public static MyData datamy;
    private static AddrItem homaddr,comanyaddr;
    public  static AddrItem getHomaddr(Context context){
        if (homaddr==null){
            SharedPreferences sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
            String json=sp.getString("homaddr","");
            if (!TextUtils.isEmpty(json)){
                Gson gson=new Gson();
                homaddr=gson.fromJson(json,AddrItem.class);
            }
        }
        return homaddr;
    }
    public  static AddrItem getComanyaddr(Context context){
        if (comanyaddr==null){
            SharedPreferences sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
            String json=sp.getString("comanyaddr","");
            if (!TextUtils.isEmpty(json)){
                Gson gson=new Gson();
                comanyaddr=gson.fromJson(json,AddrItem.class);
            }
        }
        return comanyaddr;
    }

}
