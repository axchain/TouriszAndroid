package com.tourisz.api.response;


public class BookedEventResponse extends BaseResponse {

    public boolean result;
    public String response;
    public BookedEventData data;

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

    public BookedEventData getData() {
        return data;
    }

    public void setData(BookedEventData data) {
        this.data = data;
    }
}
