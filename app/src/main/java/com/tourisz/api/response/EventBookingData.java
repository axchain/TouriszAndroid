package com.tourisz.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tourisz.api.request.Object;

public class EventBookingData extends Object{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("poster")
    @Expose
    private String poster;
    @SerializedName("eventTitle")
    @Expose
    private String eventTitle;
    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("eventDateTime")
    @Expose
    private String eventDateTime;
    @SerializedName("bookingCharges")
    @Expose
    private String bookingCharges;
    @SerializedName("webLink")
    @Expose
    private String webLink;
    @SerializedName("contactPerson")
    @Expose
    private String contactPerson;
    @SerializedName("contactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("agentId")
    @Expose
    private String agentId;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("isAvailable")
    @Expose
    private String isAvailable;
    @SerializedName("numberOfPersons")
    @Expose
    private String numberOfPersons;
    @SerializedName("attachment")
    @Expose
    private String attachment;
    @SerializedName("attachmentDescription")
    @Expose
    private String attachmentDescription;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("paymentTransactionId")
    @Expose
    private String paymentTransactionId;
    @SerializedName("bookedOn")
    @Expose
    private String bookedOn;
    @SerializedName("totalBookingAmount")
    @Expose
    private String totalBookingAmount;

    public String getTotalBookingAmount() {
        return totalBookingAmount;
    }

    public void setTotalBookingAmount(String totalBookingAmount) {
        this.totalBookingAmount = totalBookingAmount;
    }

    public String getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(String numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getBookingCharges() {
        return bookingCharges;
    }

    public void setBookingCharges(String bookingCharges) {
        this.bookingCharges = bookingCharges;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public String getBookedOn() {
        return bookedOn;
    }

    public void setBookedOn(String bookedOn) {
        this.bookedOn = bookedOn;
    }
}