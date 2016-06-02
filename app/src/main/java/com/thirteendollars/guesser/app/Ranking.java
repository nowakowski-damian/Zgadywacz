package com.thirteendollars.guesser.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thirteendollars.guesser.R;
import com.thirteendollars.guesser.other.RankingItem;

import java.util.ArrayList;
import java.util.List;

public class Ranking extends AppCompatActivity {


    List<RankingItem> rankingList= new ArrayList<RankingItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        try{
            getSupportActionBar().hide();
        }
        catch(NullPointerException actionBarException){
            Toast.makeText(getApplicationContext(), R.string.actionBarException,Toast.LENGTH_SHORT).show();
        }
        getWindow().getDecorView().setSystemUiVisibility(AppStaticData.FLAGS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainMenu.updateRankingPlace();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ranking");
        query.orderByDescending("levelPercentage");
        query.setLimit(100);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> ranking, ParseException e) {
                if (e == null) fillList(ranking);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) getWindow().getDecorView().setSystemUiVisibility(AppStaticData.FLAGS);
    }


    private void fillList(List<ParseObject> ranking) {
        int color=0;
        String userId=AppStaticData.pUser.getObjectId();
        rankingList.clear();
        for(int i=0;i<ranking.size();i++) {
            ParseObject record=ranking.get(i);
            if( record.getString("playerId").equals(userId) ) color=getResources().getColor(R.color.green);
            else color=0;
            RankingItem item= new RankingItem(color,i+1+"", record.getString("playerName"), record.getInt("levelPercentage")+"",record.getInt("ownWordsNum")+"" );
            rankingList.add(item);
        }

        ArrayAdapter<RankingItem> adapter = new myAdapter();
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

    }


    private class myAdapter extends ArrayAdapter<RankingItem>{
        public myAdapter() {
            super(Ranking.this,R.layout.listview_item,rankingList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView=getLayoutInflater().inflate(R.layout.listview_item,parent,false);

            RankingItem currentItem=rankingList.get(position);

            TextView resultText=(TextView)convertView.findViewById(R.id.result_text_view);
            resultText.setText( currentItem.getResult() );

            TextView nickText=(TextView)convertView.findViewById(R.id.nick_text_view);
            nickText.setText(currentItem.getNick());

            TextView percentText=(TextView)convertView.findViewById(R.id.percent_text_view);
            percentText.setText(currentItem.getPercentProgress());

            TextView levelText=(TextView)convertView.findViewById(R.id.level_text_view);
            levelText.setText(currentItem.getownWordsNum());

            TextView progressInfoText=(TextView)convertView.findViewById(R.id.listview_progress_info_text);
            TextView ownwordsInfoText=(TextView)convertView.findViewById(R.id.listview_ownwords_info_text);


            RelativeLayout background=(RelativeLayout)convertView.findViewById(R.id.listview_item);

            int color=currentItem.getColor();
            if(  color!= 0 ){
                int textColor=Color.parseColor("#000000");
                background.setBackgroundColor(color);
                resultText.setTextColor(textColor);
                nickText.setTextColor(textColor);
                percentText.setTextColor(textColor);
                levelText.setTextColor(textColor);
                progressInfoText.setTextColor(textColor);
                ownwordsInfoText.setTextColor(textColor);
            }

            return convertView;
        }
    }





}
