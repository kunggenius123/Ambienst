package com.example.ryan.ambienst;

import android.os.Bundle;
import android.os.PersistableBundle;

//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.SeekBar;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Map;

public class Card{
    private int imgRes;
    private String line1;
    private String line2;
    private int mSound;
    private int mRemix;
    private int mBpm;

    private String[] mTags;

    // 1st Constructor for sample cards
    public Card(int img, String title, String desc, int sound){

        imgRes = img;
        line1 = title;
        line2 = desc;
        mSound = sound;
        //mRemix = remix;

    }

    // 2nd Constructor for sample beat cards (no remix button)
    public Card(int img, String title, int bpm, String desc, int sound, String[] tags){

        imgRes = img;
        line1 = title;
        line2 = desc;

        mSound = sound;
        mBpm = bpm;
        mTags = tags;
    }


    public int getImgRes(){
        return imgRes;
    }
    public String getLine1(){
        return line1;
    }
    public String getLine2(){
        return line2;
    }
    public int getmSound(){return mSound;}
    public int getmRemix(){return mRemix;}

    public int getmBpm(){return mBpm;}

    public String[] getmTags(){
        return mTags;
    }

    public static Card getCardByName(ArrayList<Card> cards, String name){
        for (Card card : cards) {
            if(card.getLine1().equals(name)){
                return card;
            }
        }
        return null;
    }

    public static Card getCardByName2D(Map<Card, ArrayList<Card>> cards, String name){
        for (ArrayList<Card> list : cards.values()) {

            for (Card card : list) {
                if(card.getLine1().equals(name)){
                    return card;
                }
            }

        }
        return null;
    }





}
