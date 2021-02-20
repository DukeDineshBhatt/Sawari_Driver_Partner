package com.dinesh.sawaridriverpartner.NOtificationPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificatioBean {
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("notification")
    @Expose
    private Notification notification;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

}
