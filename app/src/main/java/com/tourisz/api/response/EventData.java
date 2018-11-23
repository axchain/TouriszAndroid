package com.tourisz.api.response;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tourisz.api.request.Object;
import com.tourisz.util.BookingTypes;

import java.io.Serializable;

public class EventData extends Object{

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
    @SerializedName("isBooked")
    @Expose
    private Integer isBooked;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;

    private int type = BookingTypes.Companion.getEVENT();

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(Integer isBooked) {
        this.isBooked = isBooked;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "EventData{" +
                "id='" + id + '\'' +
                ", poster='" + poster + '\'' +
                ", eventTitle='" + eventTitle + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", eventDateTime='" + eventDateTime + '\'' +
                ", bookingCharges='" + bookingCharges + '\'' +
                ", webLink='" + webLink + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", agentId='" + agentId + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", status='" + status + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", isBooked=" + isBooked +
                ", type=" + type +
                '}';
    }
}