package com.tourisz.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UISettings {

    @SerializedName("button")
    @Expose
    private Button button;

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

}