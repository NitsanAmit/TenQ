package com.postpc.tenq.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserRooms implements Serializable{



    private List<String> Rooms;

    public UserRooms(){
        this.Rooms = new ArrayList<>();


    }

    public UserRooms(List<String> Rooms) {
        this.Rooms = new ArrayList<>();
        this.Rooms = Rooms;
    }


    public List<String> getRooms() {
        return Rooms;
    }

    public void setRooms(List<String> rooms) {
        Rooms = rooms;
    }


}
