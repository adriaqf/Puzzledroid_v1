package com.sds.puzzledroid.logic;

public class Score {

    private int totalScore;
    private int difficulty;
    private String dateTime;

    public Score() {}

    public Score(int totalScore, int difficulty) {
        this.totalScore = totalScore;
        this.difficulty = difficulty;
    }


    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
