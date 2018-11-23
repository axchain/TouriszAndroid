package com.tourisz.api.response;

public class LoginResponse extends BaseResponse {

    public boolean result;
    public String response;
    public UserData data;

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


    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }


}
