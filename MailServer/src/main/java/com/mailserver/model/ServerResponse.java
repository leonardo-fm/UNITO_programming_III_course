package com.mailserver.model;

import java.lang.reflect.Type;

public class ServerResponse {
    private ResponseType responseType;
    private String responseDescription;
    private Object payload;
    private Type payloadType;

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

    public Type getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(Type payloadType) {
        this.payloadType = payloadType;
    }
}
