package com.example.appointmentscheduler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class AppointmentViewAdaptor extends ArrayAdapter<Appointment> {

    // another custom adaptor to show all the info on for each appointment per hour
    TextView petName, breed, owner, address, phone, startTime, endTime, totalPay;
    Button edit;

    Context context;
    public AppointmentViewAdaptor(@NonNull Context context, ArrayList<Appointment> appointments) {
        super(context, 0, appointments);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Appointment app = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.viewappointmentcell, parent,false);
        }

        setTextViews(convertView, app);

        return convertView;
    }

    private void setTextViews(View convertView, Appointment app) {
        String info;
        petName = convertView.findViewById(R.id.PetNameView);
        info = app.getPetName();
        petName.setText(info);

        breed = convertView.findViewById(R.id.BreedView);
        info = app.getBreed();
        breed.setText(info);

        owner = convertView.findViewById(R.id.OwnerVIew);
        info = app.getOwnerName();
        owner.setText(info);

        address = convertView.findViewById(R.id.AddressView);
        info = app.getAddress();
        address.setText(info);

        phone = convertView.findViewById(R.id.PhoneNumberView);
        info = app.getPhone();
        phone.setText(info);

        startTime = convertView.findViewById(R.id.StartTimeView);
        int hour = app.getStartHour();
        String time;

        if(hour >= 12 ){
            if(hour == 12){
                info = Integer.toString(hour);
                time = " pm";
            }else {
                info = Integer.toString(hour - 12);
                time = " pm";
            }
        }
        else{
            if(hour == 0) {
                info = "12";
                time = " am";
            }
            else{
                info = Integer.toString(hour);
                time = " am";
            }
        }

        int minute = app.getStartMinute();
        if(minute < 9){
            info = info + ":0" + Integer.toString(minute) + time;
        }
        else{
            info = info + ":" + Integer.toString(minute) + time;
        }
        startTime.setText(info);

        endTime = convertView.findViewById(R.id.EndTimeView);
        hour = app.getEndHour();
        if(hour >= 12 ){
            if(hour == 12){
                info = Integer.toString(hour);
                time = " pm";
            }else {
                info = Integer.toString(hour - 12);
                time = " pm";
            }
        }
        else{
            if(hour == 0) {
                info = "12";
                time = " am";
            }
            else{
                info = Integer.toString(hour);
                time = " am";
            }
        }

        minute = app.getEndMinute();
        if(minute < 9){
            info = info + ":0" + Integer.toString(minute) + time;
        }
        else{
            info = info + ":" + Integer.toString(minute) + time;
        }
        endTime.setText(info);

        totalPay = convertView.findViewById(R.id.TotalPayView);
        info = "$" + Double.toString(app.getTotalPay());
        totalPay.setText(info);

        edit = convertView.findViewById(R.id.Edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, EditAppointment.class);
                intent.putExtra("app", app);
                context.startActivity(intent);
            }
        });


    }

}
