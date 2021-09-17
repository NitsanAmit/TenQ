package com.postpc.tenq.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Room implements Serializable {

    private String id;
    private String name;
    private boolean active;
    // private boolean userActionsAllowed; TODO Noam
    private long creationTime;
    private User host;
    private List<User> guests;
    private Playlist playlist;

    public Room() {
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Room(String name, User host) {
        this.name = name;
        this.host = host;
        this.creationTime = new Date().getTime();
        this.active = true;
        this.guests = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public User getHost() {
        return host;
    }

    public List<User> getGuests() {
        return guests;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

}
