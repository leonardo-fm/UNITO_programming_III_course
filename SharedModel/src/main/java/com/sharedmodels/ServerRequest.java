package com.sharedmodels;

import java.io.Serializable;
import java.lang.reflect.Type;

public class ServerRequest implements Serializable {
    private MethodType methodType;
    private Object payload;

    public ServerRequest() { }
    public ServerRequest(MethodType methodType) {
        this.methodType = methodType;
    }
    public ServerRequest(MethodType methodType, Object payload, Type payloadType) {
        this.methodType = methodType;
        this.payload = payload;
    }

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
}
