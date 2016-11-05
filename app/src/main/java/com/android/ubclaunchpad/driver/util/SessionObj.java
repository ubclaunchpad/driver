package com.android.ubclaunchpad.driver.util;


/**
 * Created by Marina on 10/15/16.
 */
public class SessionObj {

    private String sSessionName;
    private String sID;
    private String sUserName;

    public SessionObj(String SessionName, String ID, String UserName){
        this.sSessionName = SessionName;
        this.sID = ID;
        this.sUserName = UserName;
    }

    public String getSessionName(){
        return sSessionName;
    }

    public String getSessionID(){
        return sID;
    }

    public String getUserName(){
        return sUserName;
    }
}
