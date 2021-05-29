package com.example.justalk_main.models;

public class ModelComment {
    String cId,comment,timestamp,uEmail,uId,uEmail1,uDp,uName;

    public ModelComment() {
    }

    public ModelComment(String cId, String comment, String timestamp, String uEmail, String uId, String uEmail1, String uDp, String uName) {
        this.cId = cId;
        this.comment = comment;
        this.timestamp = timestamp;
        this.uEmail = uEmail;
        this.uId = uId;
        this.uEmail1 = uEmail1;
        this.uDp = uDp;
        this.uName = uName;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuEmail1() {
        return uEmail1;
    }

    public void setuEmail1(String uEmail1) {
        this.uEmail1 = uEmail1;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }
}
