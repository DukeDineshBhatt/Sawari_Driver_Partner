package com.dinesh.sawaridriverpartner;

public class CabsModel {

    public CabsModel() {
    }

    String brand,type,ac,number,available;
    String seats;

    public CabsModel(String brand, String type, String ac, String number, String seats,String available) {
        this.brand = brand;
        this.type = type;
        this.ac = ac;
        this.number = number;
        this.seats = seats;
        this.available = available;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}
