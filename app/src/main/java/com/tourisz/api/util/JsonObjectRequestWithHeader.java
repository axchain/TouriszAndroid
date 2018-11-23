package com.tourisz.api.util;

import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class JsonObjectRequestWithHeader extends JsonObjectRequest {

    public JsonObjectRequestWithHeader(int method, String url, JSONObject requestBody, Response.Listener listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }

    public String getBodyContentType()
    {
        return "application/json";
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        Map<String, String> params = new HashMap<String, String>();
        params.put("Api-Key", "7dedd4d5c9efc413ae53b98d4d4af26e");
        return params;
    }


}
