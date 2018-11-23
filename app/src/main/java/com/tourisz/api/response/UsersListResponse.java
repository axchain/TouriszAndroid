package com.tourisz.api.response;

import java.util.ArrayList;

public class UsersListResponse extends BaseResponse {

    public boolean result;
    public String response;
    public ArrayList<UsersData> data = new ArrayList<>();

    @Override
    public boolean isResult() {
        return result;
    }

    @Override
    public void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public String getResponse() {
        return response;
    }

    @Override
    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<UsersData> getData() {
        return data;
    }

    public void setData(ArrayList<UsersData> data) {
        this.data = data;
    }
}
