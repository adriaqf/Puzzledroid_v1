package com.sds.puzzledroid.pojos;

import android.content.Context;

public class LCalendarEvent {

    private Context context;
    private long idEvent;
    private String title;
    private String description;

    public LCalendarEvent(Context context) {
        this.context = context;
    }

    public LCalendarEvent(Context context, long idEvent, String title, String description) {
        this.context = context;
        this.idEvent = idEvent;
        this.title = title;
        this.description = description;
    }

    public long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(long idEvent) {
        this.idEvent = idEvent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
