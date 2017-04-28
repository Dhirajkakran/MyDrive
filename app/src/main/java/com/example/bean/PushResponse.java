package com.example.bean;

import java.io.Serializable;

/**
 * Created by Footkar on 7/25/2015.
 */
public class PushResponse implements Serializable {

    private String userId;
    private String message;

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    private boolean isAccepted;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String isSuccess) {
        this.userId = isSuccess;
    }


}
