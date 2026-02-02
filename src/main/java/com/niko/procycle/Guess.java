package com.niko.procycle;

public class Guess {
    private Cyclist guessedCyclist;
    private String[] colors;

    public Guess(Cyclist guessedCyclist, String [] colors){
        this.guessedCyclist = guessedCyclist;
        this.colors = colors;
    }

    public Cyclist getGuessedCyclist(){
        return guessedCyclist;
    }

    public String [] getColors(){
        return colors;
    }



}

