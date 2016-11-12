package com.android.ubclaunchpad.driver.util;


import com.android.ubclaunchpad.driver.models.User;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Marina on 10/15/16.
 */
public class SessionObj {

    private String sSessionName;
    private String sID;
    private String sUserName;
    private String sLatLong;
    private boolean isOpen;
    // hashmap where the key is the Driver, that is a user and the entry is the list of users(list of Passangers)
    private HashMap<User,List<User>> users;

    public SessionObj(String SessionName, String ID, String LatLon, String UserName){
        this.sSessionName = SessionName;
        this.sID = ID;
        this.sUserName = UserName;
        this.sLatLong = LatLon;
        this.isOpen = true;
        users = new HashMap<>();
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

    public String getLatLong(){return sLatLong;}

    public boolean getIsOpen(){return isOpen;}

    public HashMap getUsers(){return users;}
}
