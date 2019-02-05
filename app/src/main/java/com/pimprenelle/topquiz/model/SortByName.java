package com.pimprenelle.topquiz.model;

import java.util.Comparator;

public class SortByName implements Comparator<Score>
{
    // Used for sorting in ascending order of name
    public int compare(Score a, Score b)
    {
        return a.getPlayerName().compareTo(b.getPlayerName());
    }
}