package com.example.glass.arshopping;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME="GlassDB.db";

    public DBHelper(@Nullable Context context) {
        super(context,"GlassDB.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(user_id INTEGER PRIMARY KEY AUTOINCREMENT,user_name TEXT,user_email TEXT,user_password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {

        MyDB.execSQL("drop Table if exists users");
    }
    public Boolean insertData(String user_name,String user_email,String user_password){
        SQLiteDatabase MyDB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("user_name",user_name);
        contentValues.put("user_email",user_email);
        contentValues.put("user_password",user_password);
        long result=MyDB.insert("users",null,contentValues);
        if (result==1) return false;
        else
            return true;

    }
    public Boolean checkusername(String user_name){

        SQLiteDatabase MyDB=this.getWritableDatabase();
        Cursor cursor=MyDB.rawQuery("Select*from users where user_name =?",new String[] {user_name});

        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public Boolean checkusernamepassword(String user_name, String user_password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where user_name = ? and user_password = ?", new String[]{user_name, user_password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
}