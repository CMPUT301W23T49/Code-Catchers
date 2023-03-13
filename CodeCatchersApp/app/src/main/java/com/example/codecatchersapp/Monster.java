package com.example.codecatchersapp;

import android.view.View;

import java.security.NoSuchAlgorithmException;

public class Monster {
    private String hashValue;
    private String score;
    private View monsterView;
    private String monsterName;
    public Monster(String hashValue)  {
        this.hashValue = hashValue;
        try {
            this.score = new Score("BFG5DGW54").getScore();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        this.monsterName = new MonsterNameGenerator().generateName("BFG5DGW54");
    }

    public String getHashValue() {
        return hashValue;
    }

    public String getScore() {
        return score;
    }

    public View getMonsterView() {
        return monsterView;
    }

    public String getName() {
        return monsterName;
    }
}
