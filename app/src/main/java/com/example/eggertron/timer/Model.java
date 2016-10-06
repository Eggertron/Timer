package com.example.eggertron.timer;

/**
 * Created by Edgar Han on 10/5/16.
 */
public class Model {
    private double time;
    private String list;

    public Model(double startTime) {
        time = startTime;
        list = "";
    }

    public double time() {
        return time;
    }

    public void setTime(double t) {
        time = t;
    }

    public void append(String str) {
        list += str;
    }

    public String toString() {
        return list;
    }
}
