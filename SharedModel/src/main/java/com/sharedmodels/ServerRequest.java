package com.sharedmodels;

import java.io.Serializable;
import java.lang.reflect.Type;

public class ServerRequest implements Serializable {
    private MethodType methodType;
    private Object payload;
    private Type payloadType;

    public MethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
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