package com.matcha.myapplication;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.matcha.myapplication.dao.Action;
import com.matcha.myapplication.dao.ActionDAO;
import com.matcha.myapplication.dao.Game;
import com.matcha.myapplication.dao.GameDAO;
import com.matcha.myapplication.dao.Player;
import com.matcha.myapplication.dao.PlayerDAO;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SectionFragment extends Fragment {


    Context context;
    WebView wv;
    Spinner sp3,sp2;
    ArrayList<Game> games=new ArrayList<>();
    String sour="";
    static String pid="";
    int sec=0;      //spinner1的節次,0(全部),1,2,3,4
    String num="";  //spinner2的背號,""為全部,其餘為背號
    ArrayList<Player> players;  //取得球員
    ActionDAO actionDAO;
    ArrayList<Action> actions;//取得的動作
    ParseHTML parseHTML;
    public SectionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SectionFragment newInstance(String pid) {
        SectionFragment fragment = new SectionFragment();
        SectionFragment.pid=pid;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
        parseHTML=new ParseHTML();
        actionDAO=new ActionDAO(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_section, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("summary pid",pid);
        wv=getActivity().findViewById(R.id.webView3);
        sp3=getActivity().findViewById(R.id.spinner3);
        sp2=getActivity().findViewById(R.id.spinner2);

        actionDAO=new ActionDAO(context);
        //選擇節次
        String[] sections={"全部","1","2","3","4"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,sections);
        sp3.setAdapter(adapter);
        sp3.setOnItemSelectedListener(new MyListener());

        //選擇球員
        PlayerDAO dao=new PlayerDAO(context);
        players=dao.getPlayers(pid);
        String[] names=new String[players.size()+2];
        int c=0;
        names[c++]="全部";
        for(Player p:players)
        {
            names[c++]=p.getNumber()+" "+p.getName();
        }
        names[c++]="G 客隊";
        final ArrayAdapter<String> adapterName=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,names);
        sp2.setAdapter(adapterName);
        sp2.setOnItemSelectedListener(new MyListener());

        insertData();

    }
    class MyListener implements AdapterView.OnItemSelectedListener
    {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch (adapterView.getId())
            {
                case R.id.spinner3:
                    sec=i;
                    break;
                case R.id.spinner2:
                    Log.d("guest","i = "+i+" Player.size"+players.size());
                    if(i==0)
                    {
                        num="";
                    }
                    else
                    {
                        if(i==players.size()+1)
                        {
                            num="G";
                        }
                        else
                        {
                            num=players.get(i-1).getNumber();
                        }
                    }
                    break;
            }
            Log.d("spinner","sec : "+ sec+" num : "+num);

            insertData();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            return;
        }
    }

    public void insertData()
    {
        games=new GameDAO(context).getGames(pid,sec,num);
        Log.d("count insert","p "+pid+"s "+sec+" m"+num);
        Log.d("count insert size",games.size()+"");

        wv.loadUrl("about:blank");
        wv.loadData(parseHTML.getString(games),"text/html;charset=UTF-8",null);
    }
}
