package com.tourisz.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateFlyerRequest extends Object{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("poster")
    @Expose
    private String poster;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("flyerPosition")
    @Expose
    private Integer flyerPosition;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getFlyerPosition() {
        return flyerPosition;
    }

    public void setFlyerPosition(Integer flyerPosition) {
        this.flyerPosition = flyerPosition;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UpdateFlyerRequest{" +
                "id=" + id +
                ", poster='" + poster + '\'' +
                ", title='" + title + '\'' +
                ", flyerPosition=" + flyerPosition +
                ", status=" + status +
                '}';
    }
}