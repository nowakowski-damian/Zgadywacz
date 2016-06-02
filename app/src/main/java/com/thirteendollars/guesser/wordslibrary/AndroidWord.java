package com.thirteendollars.guesser.wordslibrary;

/**
 * Created by Damian on 2016-01-04.
 */
public class AndroidWord {

    private int id;
    private int wordLength;
    private String word;


    public AndroidWord(int id,int wordLength, String word) {
        this.id=id;
        this.wordLength = wordLength;
        this.word = word;
    }

    public int getId() {
        return id;
    }

    public int getLength() {
        return wordLength;
    }

    public String getWord() {
        return word.toUpperCase();
    }


}
