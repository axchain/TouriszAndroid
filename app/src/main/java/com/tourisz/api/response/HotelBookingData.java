package com.tourisz.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tourisz.api.request.Object;

public class HotelBookingData extends Object {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("poster")
    @Expose
    private String poster;
    @SerializedName("hotelName")
    @Expose
    private String hotelName;
    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("roomType")
    @Expose
    private String roomType;
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
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("hotelId")
    @Expose
    private String hotelId;
    @SerializedName("bookedOn")
    @Expose
    private String bookedOn;
    @SerializedName("bookingFromDate")
    @Expose
    private String bookingFromDate;
    @SerializedName("bookingToDate")
    @Expose
    private String bookingToDate;
    @SerializedName("numberOfPersons")
    @Expose
    private String numberOfPersons;
    @SerializedName("numberOfRooms")
    @Expose
    private String numberOfRooms;
    @SerializedName("paymentStatus")
    @Expose
    private String paymentStatus;
    @SerializedName("paymentTransactionId")
    @Expose
    private String paymentTransactionId;
    @SerializedName("bookingStatus")
    @Expose
    private String bookingStatus;
    @SerializedName("attachment")
    @Expose
    private String attachment;
    @SerializedName("attachmentDescription")
    @Expose
    private String attachmentDescription;
    @SerializedName("totalBookingAmount")
    @Expose
    private String totalBookingAmount;

    public String getTotalBookingAmount() {
        return totalBookingAmount;
    }

    public void setTotalBookingAmount(String totalBookingAmount) {
        this.totalBookingAmount = totalBookingAmount;
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

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
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

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getBookedOn() {
        return bookedOn;
    }

    public void setBookedOn(String bookedOn) {
        this.bookedOn = bookedOn;
    }

    public String getBookingFromDate() {
        return bookingFromDate;
    }

    public void setBookingFromDate(String bookingFromDate) {
        this.bookingFromDate = bookingFromDate;
    }

    public String getBookingToDate() {
        return bookingToDate;
    }

    public void setBookingToDate(String bookingToDate) {
        this.bookingToDate = bookingToDate;
    }

    public String getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(String numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public String getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(String numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
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

}
