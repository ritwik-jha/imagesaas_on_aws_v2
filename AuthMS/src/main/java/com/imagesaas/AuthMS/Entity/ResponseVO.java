package com.imagesaas.AuthMS.Entity;

public class ResponseVO {
    private boolean status;
    private String message;
    private Object obj;

    public ResponseVO(boolean status, String message, Object obj){
        this.status = status;
        this.message = message;
        this.obj = obj;
    }

    public boolean isStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }

    public Object getObject(){
        return obj;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    
}
