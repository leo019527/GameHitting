package com.example.dong.gamehit;

/**
 * Created by ljl on 2017/7/15.
 */

public class User {
    private int score;
    private String name;
    private int Image;

    public User() {
    }

    public User(int score, String name, int Image) {
        this.score = score;
        this.name = name;
        this.Image = Image;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}
