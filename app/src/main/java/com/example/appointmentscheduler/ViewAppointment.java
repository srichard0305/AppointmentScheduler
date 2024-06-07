package com.example.appointmentscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ViewAppointment extends AppCompatActivity {
    ListView list;
    ArrayList<Appointment> totalApps, hourApps;
    AppointmentViewAdaptor adaptor;
    SharedPreferences selectedDate;
    String PREF_NAME;
    Intent intent;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);

        list = findViewById(R.id.AppointmentToday);

        PREF_NAME = "SELECTED_DATE";
        selectedDate = getSharedPreferences(PREF_NAME, 0);
        totalApps = new ArrayList<>();
        hourApps = new ArrayList<>();

        loadApps();

        intent = getIntent();
        position = intent.getIntExtra("AppointmentPosition", 0);

        setList();

    }

    private void setList() {

        for(Appointment app: totalApps){
            if(position == app.getStartHour())
                hourApps.add(app);
        }
        if(hourApps.size() > 0) {
            adaptor = new AppointmentViewAdaptor(this, hourApps);
            list.setAdapter(adaptor);
        }
    }

    public void loadApps(){
        String fileName = selectedDate.getString("selectedDate", "NOT FOUND") + ".ser";
        File file = getBaseContext().getFileStreamPath(fileName);
        if(file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                totalApps = (ArrayList<Appointment>) ois.readObject();
                ois.close();
                fis.close();
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    public void CancelView(View v){
        startActivity(new Intent(ViewAppointment.this, MainActivity.class));
    }


}