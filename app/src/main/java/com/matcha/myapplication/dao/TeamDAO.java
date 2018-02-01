package com.matcha.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Student on 2018/1/30.
 */

public class TeamDAO {
    Context context;
    public TeamDAO(Context context)
    {
        this.context=context;
    }

    //新增隊伍
    public int insertTeam(Team team)
    {
        SQLiteDatabase database=new MyDBHelper(context).getWritableDatabase();
        ContentValues values;
        values=new ContentValues();
        values.put("team1",team.getTeam1());
        values.put("team2",team.getTeam2());
        values.put("score1",team.getScore1());
        values.put("score2",team.getScore2());
        int id=(int)database.insert("teams",null,values);
        Log.d("insertTeam","id : "+id+", team1 : "+team.getTeam1()+", team2 : "+team.getTeam2()+", score1 : "+team.getScore1()+", score2 : "+team.getScore2());
        database.close();
        return id;
    }

    //依場次查詢
    public Team getTeam(String pid)
    {
        SQLiteDatabase database=new MyDBHelper(context).getWritableDatabase();
        String strSql="select * from teams where _id=?";
        Cursor c=database.rawQuery(strSql,new String[]{pid});
        Team team=null;
        if(c.moveToFirst())
        {
            int id=c.getInt(c.getColumnIndex("_id"));
            String team1=c.getString(c.getColumnIndex("team1"));
            String team2=c.getString(c.getColumnIndex("team2"));
            int score1=c.getInt(c.getColumnIndex("score1"));
            int score2=c.getInt(c.getColumnIndex("score2"));
            team=new Team(id,team1,team2,score1,score2);
        }
        database.close();
        return team;
    }

    //查詢全部隊伍
    public ArrayList<Team> getTeams()
    {
        ArrayList<Team> teams=new ArrayList<>();
        SQLiteDatabase database=new MyDBHelper(context).getWritableDatabase();
        Cursor c=database.rawQuery("select * from teams order by _id desc",null);
        if(c.moveToFirst())
        {
            do {
                int id=c.getInt(c.getColumnIndex("_id"));
                String team1=c.getString(c.getColumnIndex("team1"));
                String team2=c.getString(c.getColumnIndex("team2"));
                int score1=c.getInt(c.getColumnIndex("score1"));
                int score2=c.getInt(c.getColumnIndex("score2"));
                teams.add(new Team(id,team1,team2,score1,score2));
                Log.d("dao Team","_id : "+id+", team1 : "+team1+", team2 : "+team2+", score1 : "+score1+", score2 : "+score2);
            }while(c.moveToNext());
        }
        database.close();
        return teams;
    }

    //修改
    public boolean updateTeams(int score1,int score2,String pid)
    {
        SQLiteDatabase database=new MyDBHelper(context).getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("score1",score1);
        values.put("score2",score2);
        int i=database.update("teams",values,"_id=?",new String[]{pid});
        Log.d("teamUpdate","pid="+pid+"c= "+i);
        database.close();
        return i>0 ?true : false;
    }

    //依埸次刪除隊伍
    public int delTeams(String pid)
    {
        SQLiteDatabase database=new MyDBHelper(context).getWritableDatabase();
        int count=database.delete("teams","_id=?",new String[]{pid});
        database.close();
        Log.d("delTeams",count+"");
        return count;
    }
}
