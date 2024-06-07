package com.example.appointmentscheduler;

import java.util.ArrayList;

public class AppointmentPerHour {
    String time;
    ArrayList<Appointment> apps;

    // custom class to store all appointments for each hour of the day
    public AppointmentPerHour(String time, ArrayList<Appointment>apps){
        this.time = time;
        this.apps = apps;
    }

    public String getId() {
        return time;
    }

    public void setId(String time) {
        this.time = time;
    }

    public ArrayList<Appointment> getApps() {
        return apps;
    }

    public void setApps(ArrayList<Appointment> apps) {
        this.apps = apps;
    }
}
