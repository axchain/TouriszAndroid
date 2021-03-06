package com.tourisz.api.response;

import java.util.ArrayList;

public class AgentsListResponse extends BaseResponse {

    public boolean result;
    public String response;
    public ArrayList<AgentsData> data = new ArrayList<>();

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

    public ArrayList<AgentsData> getData() {
        return data;
    }

    public void setData(ArrayList<AgentsData> data) {
        this.data = data;
    }
}
