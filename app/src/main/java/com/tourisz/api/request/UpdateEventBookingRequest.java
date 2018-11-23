package com.tourisz.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateEventBookingRequest extends Object{

    @SerializedName("bookingId")
    @Expose
    private Integer bookingId;
    @SerializedName("attachment")
    @Expose
    private String attachment;
    @SerializedName("attachmentDescription")
    @Expose
    private String attachmentDescription;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
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

    @Override
    public String toString() {
        return "UpdateEventBookingRequest{" +
                "bookingId=" + bookingId +
                ", attachment='" + attachment + '\'' +
                ", attachmentDescription='" + attachmentDescription + '\'' +
                '}';
    }
}