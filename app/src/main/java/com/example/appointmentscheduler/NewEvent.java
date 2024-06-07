package com.example.appointmentscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NewEvent extends AppCompatActivity {

    // Activity is for scheduling an appointment

    EditText petName, breed, ownerName, address, phoneNum, pay;
    Button startTime, endTime;
    int startTimeHour, startTimeMin, endTimeHour, endTimeMin;
    ArrayList<Appointment> appList;

    SharedPreferences selectedDate;

    String PREF_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        petName = findViewById(R.id.PetNameET);
        breed= findViewById(R.id.BreedET);
        ownerName = findViewById(R.id.OwnerNameET);
        address = findViewById(R.id.AddressET);
        phoneNum = findViewById(R.id.PhoneNumberET);
        pay = findViewById(R.id.PayET);

        startTime = findViewById(R.id.StartTimeButton);
        endTime = findViewById(R.id.EndTimeButton);

        PREF_NAME = "SELECTED_DATE";
        selectedDate = getSharedPreferences(PREF_NAME, 0);

        appList = new ArrayList<Appointment>();

        loadAppointments();
    }

    public void loadAppointments(){
        String fileName = selectedDate.getString("selectedDate", "NOT FOUND") + ".ser";
        File file = getBaseContext().getFileStreamPath(fileName);
        if(file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                appList = (ArrayList<Appointment>) ois.readObject();
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

    //schedules the appointment and checks if all the infomation entered is correct
    public void ScheduleApp(View v){
        Appointment app = new Appointment();
        String info;

        //check times
        info = startTime.getText().toString();
        if(!info.equals("Choose Start Time")){
            app.setStartHour(startTimeHour);
            app.setStartMinute(startTimeMin);
        }
        else{
            Toast toast = Toast.makeText(this, "Choose Start Time!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        info = endTime.getText().toString();
        if(!info.equals("Choose End Time")){
            app.setEndHour(endTimeHour);
            app.setEndMinute(endTimeMin);
        }
        else{
            Toast toast = Toast.makeText(this, "Choose End Time!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        //check pet info and save appointment
        info = petName.getText().toString();
        if(!info.equals("")){
            app.setPetName(info);
        }else{
            Toast toast = Toast.makeText(this, "Pet Name Empty!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        info = breed.getText().toString();
        if(!info.equals("")){
            app.setBreed(info);
        }else{
            Toast toast = Toast.makeText(this, "Breed Empty!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        info = ownerName.getText().toString();
        if(!info.equals("")){
            app.setOwnerName(info);
        }else{
            Toast toast = Toast.makeText(this, "Owner Name Empty!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        info = address.getText().toString();
        if(!info.equals("")){
            app.setAddress(info);
        }else{
            Toast toast = Toast.makeText(this, "Address Empty!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        info = phoneNum.getText().toString();
        if(!info.equals("")){
            app.setPhone(info);
        }else{
            Toast toast = Toast.makeText(this, "Phone Number Empty!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        info = pay.getText().toString();
        if(!info.equals("")){
            double pay = Double.parseDouble(info);
            app.setTotalPay(pay);
        }else{
            Toast toast = Toast.makeText(this, "Pay Empty!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        appList.add(app);

        SaveAppointment();

    }
    //gets the starting time of the appointment with a time picker dialog
    public void getStartTime(View v){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startTimeHour = hourOfDay;
                startTimeMin = minute;
                startTime.setText(String.format(Locale.getDefault(), "%02d:%02d", startTimeHour, startTimeMin));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener,
                                                startTimeHour, startTimeMin, false);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }

    //gets the end time of the appointment with a time picker dialog
    public void getEndTime(View v){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endTimeHour = hourOfDay;
                endTimeMin = minute;
                endTime.setText(String.format(Locale.getDefault(), "%02d:%02d", endTimeHour, endTimeMin));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener,
                endTimeHour, endTimeMin, false);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    //saves the whole arraylist of appointments for the day in a file
    private void SaveAppointment() {
        String fileName = selectedDate.getString("selectedDate", "NOT FOUND") + ".ser";
        File file = getBaseContext().getFileStreamPath(fileName);

            try {
                FileOutputStream fos = new FileOutputStream(file, false);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(appList);
                oos.close();
                fos.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }


        Toast toast = Toast.makeText(this, "APPOINTMENT SAVED!", Toast.LENGTH_SHORT);
        toast.show();


        startActivity(new Intent(NewEvent.this, MainActivity.class));
    }
    
    public void CancelApp(View v){
        startActivity(new Intent(NewEvent.this, MainActivity.class));
    }

}