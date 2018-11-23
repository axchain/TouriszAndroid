package com.tourisz.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Button {

    @SerializedName("startColor")
    @Expose
    private String startColor;
    @SerializedName("centerColor")
    @Expose
    private String centerColor;
    @SerializedName("endColor")
    @Expose
    private String endColor;
    @SerializedName("angle")
    @Expose
    private String angle;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;

    public String getStartColor() {
        return startColor;
    }

    public void setStartColor(String startColor) {
        this.startColor = startColor;
    }

    public String getCenterColor() {
        return centerColor;
    }

    public void setCenterColor(String centerColor) {
        this.centerColor = centerColor;
    }

    public String getEndColor() {
        return endColor;
    }

    public void setEndColor(String endColor) {
        this.endColor = endColor;
    }

    public String getAngle() {
        return angle;
    }

    public void setAngle(String angle) {
        this.angle = angle;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}