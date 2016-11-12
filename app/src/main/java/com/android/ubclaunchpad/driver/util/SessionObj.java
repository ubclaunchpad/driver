package com.android.ubclaunchpad.driver.util;


import com.android.ubclaunchpad.driver.models.User;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.List;

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
}
