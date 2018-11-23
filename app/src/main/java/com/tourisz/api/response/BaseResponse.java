package com.tourisz.api.response;

public abstract class BaseResponse {

    abstract public boolean isResult();

    abstract public void setResult(boolean result);

    abstract public String getResponse();

    abstract public void setResponse(String response) ;
}
