package org.jorgechato.mybeat.base;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by jorge on 27/12/14.
 */
public class Control {
    private String note, daytime,type;
    private Date date;
    private Time time;
    private int glucose, insulin, id;

    public Control(String note, String daytime, String type, Date date, Time time, int glucose, int insulin) {
        this.note = note;
        this.daytime = daytime;
        this.date = date;
        this.time = time;
        this.glucose = glucose;
        this.insulin = insulin;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDaytime() {
        return daytime;
    }

    public void setDaytime(String daytime) {
        this.daytime = daytime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public float getGlucose() {
        return glucose;
    }

    public void setGlucose(int glucose) {
        this.glucose = glucose;
    }

    public float getInsulin() {
        return insulin;
    }

    public void setInsulin(int insulin) {
        this.insulin = insulin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
