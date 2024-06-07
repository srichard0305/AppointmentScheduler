package com.example.appointmentscheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AppointmentAdaptar extends ArrayAdapter<AppointmentPerHour> {
    ArrayList<Appointment> appointmentsList;

    ArrayList<Appointment> event1List = new ArrayList<>();
    ArrayList<Appointment> event2List = new ArrayList<>();

    //Custom array adaptor to display all appointments and times in a listview
    public AppointmentAdaptar(@NonNull Context context, ArrayList<AppointmentPerHour> appointments) {
        super(context, 0, appointments);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        AppointmentPerHour app = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cells, parent,false);
        }
        setHour(convertView, app.time);
        setAppointments(convertView, position, app.apps);

        return convertView;
    }

    private void setAppointments(View convertView, int position, ArrayList<Appointment> apps) {
        TextView event1 = convertView.findViewById(R.id.Event1);
        TextView event2 = convertView.findViewById(R.id.Event2);

        if(apps.size() == 0){
            hideEvent(event1);
            hideEvent(event2);
        }
        else if(apps.size() == 1){
            String petName = apps.get(0).getPetName();
            event1.setText(petName);
            showEvent(event1);
            event1.setAlpha(1f);
            event1List.add(apps.get(0));
        }
        else if(apps.size() == 2){
            String petName = apps.get(0).getPetName();
            event1.setText(petName);
            showEvent(event1);
            event1.setAlpha(1f);
            event1List.add(apps.get(0));
            petName = apps.get(1).getPetName();
            event2.setText(petName);
            showEvent(event2);
            event2.setAlpha(1f);
            event2List.add(apps.get(1));
        } else if(apps.size() > 2) {
            String petName = apps.get(0).getPetName();
            event1.setText(petName);
            showEvent(event1);
            event1.setAlpha(1f);
            event1List.add(apps.get(0));
            String numOfApps = String.valueOf(apps.size() - 1) + "more appointments";
            event2.setText(numOfApps);
            showEvent(event2);
            event2.setAlpha(1f);
            for(int i = 1; i < apps.size(); i++)
                event2List.add(apps.get(i));
        }

        for(Appointment app: event1List){
            if(position > app.getStartHour() && position < app.getEndHour()) {
                showEvent(event1);
                event1.setAlpha(0.55f);
            }
        }
        for(Appointment app: event2List){
            if(position > app.getStartHour() && position < app.getEndHour()) {
                showEvent(event2);
                event2.setAlpha(0.55f);
            }
        }

    }

    private void setHour(View v, String time) {
        TextView tv = v.findViewById(R.id.Times);
        tv.setText(time);
    }

    public void hideEvent(TextView tv){
        tv.setVisibility(View.INVISIBLE);
    }

    public void showEvent(TextView tv){
        tv.setVisibility(View.VISIBLE);
    }

    public void setAppointmentsList(ArrayList<Appointment> appointmentsList){
        this.appointmentsList = appointmentsList;
    }
}
