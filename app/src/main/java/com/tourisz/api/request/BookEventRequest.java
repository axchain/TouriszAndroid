package com.tourisz.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookEventRequest extends Object{

    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("eventId")
    @Expose
    private Integer eventId;
    @SerializedName("numberOfPersons")
    @Expose
    private Integer numberOfPersons;
    @SerializedName("bookingCharges")
    @Expose
    private Integer bookingCharges;
    @SerializedName("paymentStatus")
    @Expose
    private Integer paymentStatus;
    @SerializedName("paymentTransactionId")
    @Expose
    private String paymentTransactionId;
    @SerializedName("attachment")
    @Expose
    private String attachment;
    @SerializedName("attachmentDescription")
    @Expose
    private String attachmentDescription;
    @SerializedName("agentId")
    @Expose
    private Integer agentId;
    @SerializedName("totalBookingAmount")
    @Expose
    private long total;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

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

    public Integer getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(Integer numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public Integer getBookingCharges() {
        return bookingCharges;
    }

    public void setBookingCharges(Integer bookingCharges) {
        this.bookingCharges = bookingCharges;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public String getAttachmentDescription() {
        return attachmentDescription;
    }

    public void setAttachmentDescription(String attachmentDescription) {
        this.attachmentDescription = attachmentDescription;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    @Override
    public String toString() {
        return "BookEventRequest{" +
                "customerName='" + customerName + '\'' +
                ", userId=" + userId +
                ", eventId=" + eventId +
                ", numberOfPersons=" + numberOfPersons +
                ", bookingCharges=" + bookingCharges +
                ", paymentStatus=" + paymentStatus +
                ", paymentTransactionId='" + paymentTransactionId + '\'' +
                ", attachment='" + attachment + '\'' +
                ", attachmentDescription='" + attachmentDescription + '\'' +
                ", agentId=" + agentId +
                '}';
    }
}