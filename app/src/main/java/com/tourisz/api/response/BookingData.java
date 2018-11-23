package com.tourisz.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tourisz.api.request.Object;

public class BookingData extends Object{

    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("eventId")
    @Expose
    private Integer eventId;
    @SerializedName("bookedOn")
    @Expose
    private String bookedOn;
    @SerializedName("numberOfPersons")
    @Expose
    private Integer numberOfPersons;
    @SerializedName("paymentTransactionId")
    @Expose
    private String paymentTransactionId;
    @SerializedName("attachment")
    @Expose
    private String attachment;
    @SerializedName("agentId")
    @Expose
    private Integer agentId;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getBookedOn() {
        return bookedOn;
    }

    public void setBookedOn(String bookedOn) {
        this.bookedOn = bookedOn;
    }

    public Integer getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(Integer numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    @Override
    public String toString() {
        return "BookingData{" +
                "customerName='" + customerName + '\'' +
                ", userId=" + userId +
                ", eventId=" + eventId +
                ", bookedOn='" + bookedOn + '\'' +
                ", numberOfPersons=" + numberOfPersons +
                ", paymentTransactionId='" + paymentTransactionId + '\'' +
                ", attachment='" + attachment + '\'' +
                ", agentId=" + agentId +
                '}';
    }
}