package com.niko.procycle;

public class Guess {
    private Cyclist guessedCyclist;
    private String[] colors;
    private String[] arrows;

    public Guess(Cyclist guessedCyclist, String [] colors, String [] arrows){
        this.guessedCyclist = guessedCyclist;
        this.colors = colors;
        this.arrows = arrows;
    }

    public Cyclist getGuessedCyclist(){
        return guessedCyclist;
    }

    public String [] getColors(){
        return colors;
    }
    public String[] getArrows(){
        return arrows;
    }
}

