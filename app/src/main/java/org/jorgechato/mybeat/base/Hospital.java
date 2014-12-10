package org.jorgechato.mybeat.base;

/**
 * Created by jorge on 5/12/14.
 */
public class Hospital {
    private String name,timetable,phone,description,email,direction,imageURL;
    private float longitude,latitude;

    public Hospital(String name, String timetable, String phone, String description, String direction, String email, String imageURL, float longitude, float latitude) {
        this.name = name;
        this.timetable = timetable;
        this.phone = phone;
        this.description = description;
        this.direction = direction;
        this.email = email;
        this.imageURL = imageURL;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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
