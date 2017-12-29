package com.instigatemobile.imessenger.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostRequestData {
    @SerializedName("data")
    @Expose
    private NotificationData data;
    @SerializedName("to")
    @Expose
    private String to;

    public NotificationData getData() {
        return data;
    }

    public void setData(NotificationData data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
