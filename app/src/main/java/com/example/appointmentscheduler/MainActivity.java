package com.example.appointmentscheduler;

import static android.content.Intent.ACTION_TIME_TICK;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Calendar date;
    TextView dateView, yesterdayView, tmrwView, timeView;
    ListView list;
    SharedPreferences selectedDate;
    SharedPreferences.Editor editor;
    ArrayList<Appointment> appList;
    ArrayList<AppointmentPerHour> appPerHourList;
    AppointmentAdaptar adapter;
    String PREF_NAME;
    int year, month, day;
    DateHolder dateHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        date = Calendar.getInstance();
        PREF_NAME = "SELECTED_DATE";

        //stores current date as a string for save file
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
        String today = format.format(date.getTime());
        selectedDate = getSharedPreferences(PREF_NAME, 0);
        editor = selectedDate.edit();
        editor.putString("selectedDate", today);
        editor.commit();

        LoadDate(); //loads the saved date when switching back from other activities
        if(dateHolder != null) {
            // checks if the same date is currently stored on disk and removes it if true to set the current date to today
            if(dateHolder.getYear() == date.get(Calendar.YEAR) && dateHolder.getMonth() == date.get(Calendar.MONTH)
                                && dateHolder.getDay() == date.get(Calendar.DAY_OF_MONTH)){
                File file = getBaseContext().getFileStreamPath("current_date_selected.ser");
                file.delete(); //deletes file to keep current date accurate
            }else {
                year = dateHolder.getYear();
                month = dateHolder.getMonth();
                day = dateHolder.getDay();
                date.set(year, month, day);
                today = format.format(date.getTime());
                editor.putString("selectedDate", today);
                editor.commit();
                File file = getBaseContext().getFileStreamPath("current_date_selected.ser");
                file.delete();
            }
        }

        dateView = findViewById(R.id.DateView);
        yesterdayView = findViewById(R.id.YesterdayView);
        tmrwView = findViewById(R.id.TmrwView);
        timeView = findViewById(R.id.TimeView);
        list = findViewById(R.id.Times);
        list.setDivider(null);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewAppointment(position);
            }
        });

        BroadcastReceiver timeTick = new TimeReceiver();
        IntentFilter filter = new IntentFilter(ACTION_TIME_TICK);
        registerReceiver(timeTick, filter);

        appList = new ArrayList<Appointment>();
        appPerHourList = new ArrayList<AppointmentPerHour>();


        setYesterdayView();
        setTmrwView();
        setDateView();
        setTimeView();
        loadApps();

    }

    // starts the view appointment activity to see the current appointments schedule per hour
    private void ViewAppointment(int position) {
        Intent intent = new Intent(MainActivity.this, ViewAppointment.class);
        for(Appointment app: appList) {
            if(position == app.getStartHour()) {
                intent.putExtra("AppointmentPosition", position);
                startActivity(intent);
            }
        }
    }

    // loads all appointments saved to the file based on YYYY-MM-DD
    // to the arraylist
    public void loadApps(){
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
        loadAppPerHour();
    }

    // Loads an arraylist that holds appointments per hour
    public void loadAppPerHour(){
        String tempTime;
        Appointment tempApp;
        for(int hour = 0; hour < 24; hour++){
            ArrayList<Appointment> tempList = new ArrayList<>();
           for(int i = 0; i < appList.size(); i++){
               tempApp = appList.get(i);
               if(tempApp.getStartHour() == hour){
                   tempList.add(tempApp);
               }
           }
           if(hour == 0){
               tempTime = "12:00 am";
               AppointmentPerHour tempAppPerHour = new AppointmentPerHour(tempTime, tempList);
               appPerHourList.add(tempAppPerHour);
           }
           else {
               if(hour > 11){
                   if(hour == 12){
                       tempTime = Integer.toString(hour) + ":00 pm";
                       AppointmentPerHour tempAppPerHour = new AppointmentPerHour(tempTime, tempList);
                       appPerHourList.add(tempAppPerHour);
                   }else {
                       int temp = hour - 12;
                       tempTime = Integer.toString(temp) + ":00 pm";
                       AppointmentPerHour tempAppPerHour = new AppointmentPerHour(tempTime, tempList);
                       appPerHourList.add(tempAppPerHour);
                   }
               }
               else{
                   tempTime = Integer.toString(hour) + ":00 am";
                   AppointmentPerHour tempAppPerHour = new AppointmentPerHour(tempTime, tempList);
                   appPerHourList.add(tempAppPerHour);
               }
           }
        }
        adapter = new AppointmentAdaptar(this, appPerHourList);
        adapter.setAppointmentsList(appList);
        list.setAdapter(adapter);
    }

    private void setTimeView() {
        SimpleDateFormat format = new SimpleDateFormat("h:mm a");
        String time = format.format(date.getTime());
        timeView.setText(time);
    }

    private void setTmrwView() {
        date.add(Calendar.DATE, 2);
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        String dateTmrw = format.format(date.getTime());
        tmrwView.setText(dateTmrw);
    }

    private void setYesterdayView() {
        date.add(Calendar.DATE, -1);
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        String dateYest = format.format(date.getTime());
        yesterdayView.setText(dateYest);
    }

    private void setDateView() {
        date.add(Calendar.DATE, -1);
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy");
        String dateToday = format.format(date.getTime());
        dateView.setText(dateToday);
    }

    public void NewEvent(View v){
        Intent intent = new Intent(MainActivity.this, NewEvent.class);
        startActivity(intent);
    }

    // updates the current date to yesterday
    public void ShowYesterday(View v){

        SimpleDateFormat formatTmrw = new SimpleDateFormat("dd-MMM-yyyy");
        String dateTmrw = formatTmrw.format(date.getTime());
        tmrwView.setText(dateTmrw);

        date.add(Calendar.DATE, -2);
        SimpleDateFormat formatYest = new SimpleDateFormat("dd-MMM-yyyy");
        String dateYest = formatYest.format(date.getTime());
        yesterdayView.setText(dateYest);

        date.add(Calendar.DATE, 1);
        SimpleDateFormat formatToday = new SimpleDateFormat("EEEE, MMMM d, yyyy");
        String dateToday = formatToday.format(date.getTime());
        dateView.setText(dateToday);

        DateHolder dateHolder = new DateHolder(date);
        SaveDate(dateHolder);

        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
        String today = format.format(date.getTime());
        editor.putString("selectedDate", today);
        editor.commit();
        appList.clear();
        appPerHourList.clear();
        loadApps();
    }

    // updates the current date to the next day
    public void ShowTomorrow(View v){
        SimpleDateFormat formatYest = new SimpleDateFormat("dd-MMM-yyyy");
        String dateYest = formatYest.format(date.getTime());
        yesterdayView.setText(dateYest);

        date.add(Calendar.DATE, 2);
        SimpleDateFormat formatTmrw = new SimpleDateFormat("dd-MMM-yyyy");
        String dateTmrw = formatTmrw.format(date.getTime());
        tmrwView.setText(dateTmrw);

        date.add(Calendar.DATE, -1);
        SimpleDateFormat formatToday = new SimpleDateFormat("EEEE, MMMM d, yyyy");
        String dateToday = formatToday.format(date.getTime());
        dateView.setText(dateToday);

        DateHolder dateHolder = new DateHolder(date);
        SaveDate(dateHolder);

        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
        String today = format.format(date.getTime());
        editor.putString("selectedDate", today);
        editor.commit();
        appList.clear();
        appPerHourList.clear();
        loadApps();
    }

    // Broadcast Receiver that will set the time every minute
    public class TimeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().compareTo(Intent.ACTION_TIME_TICK)==0){
                setTimeView();
            }
        }
    }

    // Method when the calendar textview is selected, shows a date picker dialog and changes the current date
    // to the selected date
    public void DateSelect(View v){

        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int yearSelected, int monthSelected, int dayOfMonthSelected) {
                year = view.getYear();
                month = view.getMonth();
                day = view.getDayOfMonth();
                date.set( year, month, day);
                DateHolder dateHolder = new DateHolder(date);
                SaveDate(dateHolder);
                SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
                String today = format.format(date.getTime());
                editor.putString("selectedDate", today);
                editor.commit();
                appList.clear();
                appPerHourList.clear();
                setYesterdayView();
                setTmrwView();
                setDateView();
                setTimeView();
                loadApps();
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);
        datePickerDialog.setTitle("Pick Date");
        datePickerDialog.show();

    }

    public void SaveDate(DateHolder dateHolder){
        String fileName = "current_date_selected.ser";
        File file = getBaseContext().getFileStreamPath(fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dateHolder);
            oos.close();
            fos.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void LoadDate(){
        String fileName = "current_date_selected.ser";
        File file = getBaseContext().getFileStreamPath(fileName);
        if(file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                dateHolder = (DateHolder) ois.readObject();
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

}