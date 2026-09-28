package com.example.calendarproject;

import java.util.ArrayList;

public class Dates {
    private int year;
    private int month;
    private int day;
    private String dateName;
    private ArrayList<String> events;

    public Dates(int y, int m, int d){
        year = y;
        month = m;
        day = d;
        dateName = m+"-"+d+"-"+y;
        events = new ArrayList<>();
    }

    public String getName(){
        return dateName;
    }

    public void AddEvent(String e){
        events.add(e);
    }

    public void RemoveEventAt(int n){
        events.remove(n);
    }

    public ArrayList<String> getEventList(){
        return events;
    }

}
