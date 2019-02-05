package com.pimprenelle.topquiz.model;

import java.util.Comparator;

public class SortByScore implements Comparator<Score>
{
    // Used for sorting in descending order of score
    public int compare(Score a, Score b)
    {
        return b.getScore() - a.getScore();
    }
}