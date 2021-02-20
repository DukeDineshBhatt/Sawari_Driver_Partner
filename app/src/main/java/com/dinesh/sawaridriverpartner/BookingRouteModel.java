package com.dinesh.sawaridriverpartner;

public class BookingRouteModel {

    public BookingRouteModel() {
    }

    String from,to,available,number,price,route,name;

    public BookingRouteModel(String from, String to, String available, String number, String price,String route,String name) {
        this.from = from;
        this.to = to;
        this.available = available;
        this.number = number;
        this.price = price;
        this.route = route;
        this.name = name;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
