package com.sharedmodels;

import java.io.Serializable;
import java.lang.reflect.Type;

public class ServerResponse implements Serializable {
    private ResponseType responseType;
    private String responseDescription;
    private Object payload;

    public ServerResponse() { }
    public ServerResponse(ResponseType responseType, String responseDescription, Object payload) {
        this.responseType = responseType;
        this.responseDescription = responseDescription;
        this.payload = payload;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
