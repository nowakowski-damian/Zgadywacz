package com.thirteendollars.guesser.other;

/**
 * Created by Damian on 2015-12-18.
 */
public class RankingItem {

    private int color;
    private String result;
    private String nick;
    private String percentProgress;
    private String ownWordsNum;



    public RankingItem(int color, String result, String nick, String percentProgress, String ownWordsNum) {
        this.color = color;
        this.result = result;
        this.nick = nick;
        this.percentProgress = percentProgress+"%";
        this.ownWordsNum=ownWordsNum;
    }

    public int getColor() {
        return color;
    }

    public String getResult() {
        return result;
    }

    public String getNick() {
        return nick;
    }

    public String getPercentProgress() {
        return percentProgress;
    }

    public String getownWordsNum() {  return ownWordsNum; }

}
