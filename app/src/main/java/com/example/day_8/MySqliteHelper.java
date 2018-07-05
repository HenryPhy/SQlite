package com.example.day_8;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class MySqliteHelper extends SQLiteOpenHelper {
    //库名
    private static String name="users.db";
    //版本号
    private static int version=1;
    public MySqliteHelper(Context context) {
        super(context, name, null, version);
    }
    //创建，创建表写这里，只要APP不卸载只会执行一次
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user(_id integer primary key autoincrement,name varchar(20),sex varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
