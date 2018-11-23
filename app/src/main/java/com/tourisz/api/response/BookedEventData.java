package com.tourisz.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tourisz.api.request.Object;

public class BookedEventData extends Object {

    @SerializedName("bookingId")
    @Expose
    private Integer bookingId;

    public BookedEventData() {
    }

    public BookedEventData(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }
}
