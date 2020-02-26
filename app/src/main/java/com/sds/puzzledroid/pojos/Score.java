package com.sds.puzzledroid.pojos;

import com.sds.puzzledroid.utils.FBFirestore;

public class Score implements Comparable<Score>{

    private int totalScore;
    private int difficulty;
    private String dateTime;
    private String userName;

    public Score() {
        FBFirestore firestore = new FBFirestore();
        this.userName = firestore.getUserName();
    }

    public Score(int totalScore, int difficulty) {
        this.totalScore = totalScore;
        this.difficulty = difficulty;

        FBFirestore firestore = new FBFirestore();
        this.userName = firestore.getUserName();
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

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int compareTo(Score other) {
        return this.getTotalScore() > other.getTotalScore() ? 0 :
                this.getTotalScore() == other.getTotalScore() ? 1 : -1;
    }
}
