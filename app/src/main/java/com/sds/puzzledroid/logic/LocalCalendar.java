package com.sds.puzzledroid.logic;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class LocalCalendar {

    private Context context;

    public LocalCalendar(Context context) {
        this.context = context;
    }

    public void addNewCalendar() {
        Uri calUri = CalendarContract.Calendars.CONTENT_URI;

        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, "com.puzzledroid.android");
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        cv.put(CalendarContract.Calendars.NAME, "Puzzledroid");
        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Puzzledroid");
        cv.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.GREEN);
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, "Software Development Students");
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_READ);
        cv.put(CalendarContract.Calendars.VISIBLE, 1);
        cv.put(CalendarContract.Calendars.SYNC_EVENTS, 1);

        calUri = calUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "com.puzzledroid.android")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
        Uri result = context.getContentResolver().insert(calUri, cv);
    }

    public long getLocalCalendarId() {
        final String[] EVENT_PROJECTION = new String[] {
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.NAME
        };
        final int PROJECTION_ID_INDEX = 0;

        long calendarId = 0;
        Cursor cursor = null;
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = CalendarContract.Calendars.NAME + " = ?";
        String[] selectionArg = new String[] {"Puzzledroid"};

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            Permissions permissions = new Permissions(context);
            permissions.verifyPermissions();
        }

        cursor = contentResolver.query(uri, EVENT_PROJECTION, selection, selectionArg, null);

        if(cursor != null && cursor.moveToFirst()) {
            calendarId = cursor.getLong(PROJECTION_ID_INDEX);
            cursor.close();
        }

        return calendarId;
    }

    public void addEvent(Score score) {
        long calID = getLocalCalendarId();
        TimeZone tz = TimeZone.getDefault();
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE));
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE));
        endMillis = endTime.getTimeInMillis();

        //Checks for WRITE_CALENDAR permissions
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            Permissions permissions = new Permissions(context);
            permissions.verifyPermissions();
        }

        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, score.getTotalScore());
        values.put(CalendarContract.Events.DESCRIPTION,
                score.getDifficulty() == 0 ? "Dificultad: Fácil" : score.getDifficulty() == 1 ? "Dificultad: Media" : "Dificultad: Difícil");
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());

        System.out.println(tz.getID());

        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values);
    }

    public ArrayList<LCalendarEvent> getAllEvents() {
        ArrayList<LCalendarEvent> events = new ArrayList<>();
        final String[] INSTANCE_PROJECTION = new String[] {
                CalendarContract.Events.CALENDAR_ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DESCRIPTION
        };
        final int PROJECTION_CALENDAR_ID = 0;
        final int PROJECTION_TITLE = 1;
        final int PROJECTION_DESCRIPTION = 2;

        final long idLocalCal = getLocalCalendarId();
        final String sIdLocalCal = String.valueOf(idLocalCal);

        String selection = CalendarContract.Events.CALENDAR_ID + " = ?";
        String[] selectionArg = new String[] {sIdLocalCal};

        Cursor cursor = null;
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;

        //Checks for READ_CALENDAR permissions
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            Permissions permissions = new Permissions(context);
            permissions.verifyPermissions();
        }

        cursor = contentResolver.query(uri, INSTANCE_PROJECTION, selection, selectionArg, null);

        if(cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                LCalendarEvent event = new LCalendarEvent(context);

                event.setTitle(cursor.getString(PROJECTION_TITLE));
                event.setDescription(cursor.getString(PROJECTION_DESCRIPTION));

                events.add(event);

                cursor.moveToNext();
            }
            cursor.close();
        }

        return events;
    }

}
