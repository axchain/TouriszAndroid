package com.tourisz.api.response;


public class BookedHotelResponse extends BaseResponse {

    public boolean result;
    public String response;
    public BookedHotelData data;

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

    public BookedHotelData getData() {
        return data;
    }

    public void setData(BookedHotelData data) {
        this.data = data;
    }
}
