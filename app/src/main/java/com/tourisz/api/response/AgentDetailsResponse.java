package com.tourisz.api.response;


public class AgentDetailsResponse extends BaseResponse {

    public boolean result;
    public String response;
    public AgentData data;

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

    public AgentData getData() {
        return data;
    }

    public void setData(AgentData data) {
        this.data = data;
    }
}
