package com.example.appointmentscheduler;

import java.io.Serializable;

public class Appointment implements Serializable{

    // Appointment class to store all customer data
    private String petName, breed, ownerName, address, phone;
    private int startHour, startMinute, endHour, endMinute;
    double totalPay;

    public Appointment(){
        petName = breed = ownerName = address = phone = "";
        startHour = startMinute = endHour = endMinute = 0;
        totalPay = 0.0;
    }

    public Appointment(String petName, String breed, String ownerName, String address, String phone,
                       int startHour, int startMinute, int endHour, int endMinute, int totalPay) {
        this.petName = petName;
        this.breed = breed;
        this.ownerName = ownerName;
        this.address = address;
        this.phone = phone;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.totalPay = totalPay;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }

    public boolean compareTo(Appointment a){
        if(a.getPetName().equals(petName) && a.getBreed().equals(breed) && a.getOwnerName().equals(ownerName) &&
                        a.getAddress().equals(address) && a.getPhone().equals(phone) && a.getStartHour() == startHour &&
                        a.getStartMinute() == startMinute && a.getEndHour() == endHour &&
                        a.getEndMinute() == endMinute && a.getTotalPay() == totalPay){
            return true;
        }
        else
            return false;
    }
}
