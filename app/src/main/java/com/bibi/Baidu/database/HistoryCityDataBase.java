package com.bibi.Baidu.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bibinet on 2016/11/2.
 */
public class HistoryCityDataBase extends SQLiteOpenHelper {
    private  String sql="create table historycitytable(" +
            "_id integer primary key autoincrement,"+
            "historycity text not null)";
    public HistoryCityDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="drop table hitorycity";
        db.execSQL(sql);
        onCreate(db);
    }
}
