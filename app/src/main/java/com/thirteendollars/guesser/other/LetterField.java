package com.thirteendollars.guesser.other;

public class LetterField {


    private int id;
    private boolean isChosen;
    private char letter;

    public LetterField(int id,char letter){
        this.id=id;
        this.letter=letter;
        isChosen=false;
    }

    public void select(){
        this.isChosen=true;

    }

    public void unselect(){
        this.isChosen=false;

    }

    public int getId(){
        return this.id;
    }

    public boolean isChosen(){
        return this.isChosen;
    }

    public boolean isWordCorrect(String letter){
        if( letter.isEmpty() ) return false;
        if(this.letter==letter.charAt(0)) return true;
        else return false;

    }

    public String getLetter(){
        return this.letter+"";
    }



}
