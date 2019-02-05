package com.pimprenelle.topquiz.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
A java class to create my ScoreList object and some method to manipulate it.
addToList()
sortByScore() this method use the SortByScore class implement with Comparator to use Collections method.
sortByName() this method use the SortByName class implement with Comparator to use Collections method.
resize()

*/
public class ScoreList {

    private List<Score> mPlayersScore;

    private final int NUMBER_SCORES = 5;


    public void addToList(Score playerScore){

        // For the first add, we create a new ArrayList to avoid nullPointException
        if(this.mPlayersScore==null){
            System.out.println("Score::mPlayersScore null");
            this.mPlayersScore = new ArrayList<Score>();
        }

        this.mPlayersScore.add(playerScore);

        // Sort the list by score by default
        sortByScore();

        // Keep just the 5 better score
        resize();
    }

    public List<Score> getPlayersScore() {
        return mPlayersScore;
    }

    public void sortByScore(){
        Collections.sort(this.mPlayersScore, new SortByScore());
    }

    public void sortByName(){
        Collections.sort(this.mPlayersScore, new SortByName());
    }

    private void resize(){
        if(mPlayersScore.size()>NUMBER_SCORES){
            mPlayersScore.remove(NUMBER_SCORES);
        }
    }

}


