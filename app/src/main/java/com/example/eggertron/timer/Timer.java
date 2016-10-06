package com.example.eggertron.timer;

import java.util.Calendar;

/**
 * Created by Edgar Han on 10/5/16.
 */
public class Timer {
    String list;
    boolean isStarted;
    int seconds;
    int mins;
    int hours;
    int index;
    int mil = 0;

    public Timer() {
        list = "";
        isStarted = false;
        seconds = 0;
        mins = 0;
        hours = 0;
        index = 0;
    }
    public Timer(int s, int m, int h, String l, boolean i, int x) {
        seconds = s;
        mins = m;
        hours = h;
        list = l;
        isStarted = i;
        index = x;
    }

    public String getTime() {
        return "" + String.format("%02d", hours) + ":" + String.format("%02d", mins)
                + ":" + String.format("%02d", seconds);
    }

    public void start() {
        isStarted = true;
    }

    public void lap() {
        list += index + ". " + getTime() + "\n";
    }

    public void stop() {
        isStarted = false;
    }

    public String getList() {
        return list;
    }

    public void addSec() {
        seconds++;
        if (mins > 59) {
            hours++;
            mins = 0;
        }
        if (seconds > 59) {
            mins++;
            seconds = 0;
        }
    }

    public void addMil() {
        mil++;
        if (mil > 10) {
            addSec();
            mil = 0;
        }
    }

}
