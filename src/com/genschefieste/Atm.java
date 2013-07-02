package com.genschefieste;

/**
 * Representation of an ATM object
 * @author Jeppe Knockaert, Leen De Baets, Nicolas Dierck, Benjamin Mestdagh
 * (c) 2013, OKFN. All rights reserved.
 */
public class Atm {
    private float distance;
    private String name;
    private String address;
    private float longitude;
    private float latitude;

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
