package com.tourisz.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tourisz.api.request.Object;

public class BookedHotelData extends Object {

    @SerializedName("bookingId")
    @Expose
    private Integer bookingId;

    public BookedHotelData() {
    }

    public BookedHotelData(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }
}
