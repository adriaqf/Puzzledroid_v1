package com.sds.puzzledroid.logic;

public class Settings {
    private int music;
    private int theme;

    public Settings(){}
    public Settings(int music, int theme){
            this.music = music;
            this.theme = theme;
        }


    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public int getMusic() {
        return music;
    }

    public void setMusic(int music) {
        this.music = music;
    }
    }

