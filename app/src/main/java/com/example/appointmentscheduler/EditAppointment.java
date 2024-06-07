package com.example.appointmentscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import java.util.Locale;

public class EditAppointment extends AppCompatActivity {

    String PREF_NAME;
    Appointment appointment;
    ArrayList<Appointment> apps;
    SharedPreferences selectedDate;
    EditText petName, breed, ownerName, address, phoneNum, pay;
    Button startTime, endTime, delete;
    int startTimeHour, startTimeMin, endTimeHour, endTimeMin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        PREF_NAME = "SELECTED_DATE";
        petName = findViewById(R.id.PetNameET);
        breed= findViewById(R.id.BreedET);
        ownerName = findViewById(R.id.OwnerNameET);
        address = findViewById(R.id.AddressET);
        phoneNum = findViewById(R.id.PhoneNumberET);
        pay = findViewById(R.id.PayET);

        startTime = findViewById(R.id.StartTimeButton);
        endTime = findViewById(R.id.EndTimeButton);
        delete = findViewById(R.id.Delete);
        delete.setVisibility(View.VISIBLE);

        appointment = (Appointment) getIntent().getSerializableExtra("app");
        selectedDate = getSharedPreferences(PREF_NAME, 0);

        petName.setText(appointment.getPetName());
        breed.setText(appointment.getBreed());
        ownerName.setText(appointment.getOwnerName());
        address.setText(appointment.getAddress());
        phoneNum.setText(appointment.getPhone());

        apps = new ArrayList<>();
        loadApps();

    }
    public void loadApps(){
        String fileName = selectedDate.getString("selectedDate", "NOT FOUND") + ".ser";
        File file = getBaseContext().getFileStreamPath(fileName);
        if(file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                apps = (ArrayList<Appointment>) ois.readObject();
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

        int counter = 0;
        for(Appointment temp: apps){
            if(temp.compareTo(appointment)){
                apps.remove(counter);
                break;
            }
            counter++;
        }

        apps.add(app);

        SaveAppointment();

    }

    //gets the starting time of the appointment with a time picker dialog
    public void getStartTime(View v){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startTimeHour = hourOfDay;
                startTimeMin = minute;
                startTime.setText(String.format(Locale.getDefault(), "%02d::%02d", startTimeHour, startTimeMin));
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
                endTime.setText(String.format(Locale.getDefault(), "%02d::%02d", endTimeHour, endTimeMin));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener,
                endTimeHour, endTimeMin, false);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private void SaveAppointment() {
        String fileName = selectedDate.getString("selectedDate", "NOT FOUND") + ".ser";
        File file = getBaseContext().getFileStreamPath(fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(apps);
            oos.close();
            fos.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }


        Toast toast = Toast.makeText(this, "APPOINTMENT SAVED!", Toast.LENGTH_SHORT);
        toast.show();


        startActivity(new Intent(EditAppointment.this, MainActivity.class));
    }

    //deletes appointment with a popup dialog box to confirm
    public void DeleteApp(View v){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                       int counter = 0;
                       for(Appointment app: apps){
                           if(app.compareTo(appointment)){
                                apps.remove(counter);
                                break;
                           }
                           counter++;
                       }

                        SaveAppointment();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete the appointment?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }

    public void CancelApp(View v){
        startActivity(new Intent(EditAppointment.this, MainActivity.class));
    }
}