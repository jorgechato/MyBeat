package org.jorgechato.mybeat.base;

import android.media.Image;

/**
 * Created by jorge on 5/12/14.
 */
public class Hospital {
    //private Image image;
    private String name,timetable,phone,description,email,map,direction;
    private String image;
    private float longitude,latitude;

    public Hospital(String name, String timetable, String phone, String description, String direction, String email, String map, String image, float longitude, float latitude) {
        this.name = name;
        this.timetable = timetable;
        this.phone = phone;
        this.description = description;
        this.direction = direction;
        this.email = email;
        this.map = map;
        this.image = image;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimetable() {
        return timetable;
    }

    public void setTimetable(String timetable) {
        this.timetable = timetable;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
