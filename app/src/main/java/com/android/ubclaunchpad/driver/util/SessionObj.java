package com.android.ubclaunchpad.driver.util;


/**
 * Created by Marina on 10/15/16.
 */
public class SessionObj {

    private String sSessionName;
    private String sID;
    private String sLatLong;
    private boolean isOpen;
    private String uID;
    // hashmap with String = userID, User - user object

    public SessionObj(){}

    public SessionObj(String SessionName, String ID, String LatLon, String userID){
        this.sSessionName = SessionName;
        this.sID = ID;
        this.uID = userID;
        this.sLatLong = LatLon;
        this.isOpen = true;
    }

    public String getSessionName(){
        return sSessionName;
    }

    public String getSessionID(){
        return sID;
    }

    public String getUserID(){
        return uID;
    }

    public String getLatLong(){return sLatLong;}

    public boolean getIsOpen(){return isOpen;}

    public void setsSessionName(String sSessionName) {
        this.sSessionName = sSessionName;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public void setsLatLong(String sLatLong) {
        this.sLatLong = sLatLong;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }
}
