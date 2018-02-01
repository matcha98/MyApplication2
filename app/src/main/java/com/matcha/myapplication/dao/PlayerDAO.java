package com.matcha.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Student on 2018/1/16.
 */

public class PlayerDAO {

    Context context;
    public PlayerDAO(Context context)
    {
        this.context=context;
    }

    //新增
    public boolean insertPlayers(ArrayList<Player> myData)
    {
        int count=0;
        SQLiteDatabase database=new MyDBHelper(context).getWritableDatabase();
        ContentValues values;
        for(int i=0;i<myData.size();i++)
        {
            values=new ContentValues();
            values.put("pid",myData.get(i).getPid());
            values.put("number",myData.get(i).getNumber());
            values.put("name",myData.get(i).getName());
            long id=database.insert("players",null,values);
            Log.d("insertPlayer","id "+id+" pid "+myData.get(i).getPid()+
                    " number "+myData.get(i).getNumber()+" name "+myData.get(i).getName());
            count++;
        }
        database.close();

        return (count>0) ? true:false;
    }

    //依背號取得全部球員
    public ArrayList<Player> getPlayers(String pid)
    {
        ArrayList<Player> list=new ArrayList<>();
        int id=0;
        String number,name;
        SQLiteDatabase database=new MyDBHelper(context).getWritableDatabase();
        String strSql="select * from players where pid=? order by CAST(number as integer)";
        Cursor c=database.rawQuery(strSql,new String[]{pid});
        c.moveToFirst();
        do {
            id=c.getInt(c.getColumnIndex("_id"));
            number=c.getString(c.getColumnIndex("number"));
            name=c.getString(c.getColumnIndex("name"));
            list.add(new Player(id,pid,number,name));
            Log.d("Player get","_id : "+id+", pid : "+pid+", number : "+number+", name : "+name);
        }while(c.moveToNext());
        Log.d("Player_Count",list.size()+"");
        database.close();
        return list;
    }

    //依場次刪除球員
    public int delPlayer(String pid)
    {
        SQLiteDatabase database=new MyDBHelper(context).getWritableDatabase();
        int count=database.delete("players","pid=?",new String[]{pid});
        database.close();
        Log.d("delPlayer",count+"");
        return count;
    }
}
