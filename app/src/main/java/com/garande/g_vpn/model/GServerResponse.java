package com.garande.g_vpn.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class GServerResponse implements Serializable {
    public String type;

    public Object payload;

    public GServerResponse() {
    }

    public GServerResponse(String type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public GServerResponse getResponse(String msg){
        Gson gson = new Gson();
        GServerResponse gServerResponse = gson.fromJson(msg, GServerResponse.class);
        return gServerResponse;
    }

    public String toStringJson(){
        Gson gson = new Gson();
        String str = gson.toJson(this);
        return str;
    }

}
