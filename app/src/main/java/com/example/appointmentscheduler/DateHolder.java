package com.example.appointmentscheduler;

import java.io.Serializable;
import java.util.Calendar;

public class DateHolder implements Serializable {

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    // custom class to save the instance of a date
    int year, month, day;

    public  DateHolder(Calendar date){
        year = date.get(Calendar.YEAR);
        month = date.get(Calendar.MONTH);
        day = date.get(Calendar.DAY_OF_MONTH);
    }

}
