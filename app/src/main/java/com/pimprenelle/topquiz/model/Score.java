package com.pimprenelle.topquiz.model;

public class Score {

    private String mPlayerName;
    private int mScore;

    public Score(String playerName,int score){
        this.mPlayerName = playerName;
        this.mScore = score;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public int getScore() {
        return mScore;
    }
}
