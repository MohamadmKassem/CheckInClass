package com.kassem.mohamad.checkinclass;

/**
 * Created by Mohamad on 3/23/2017.
 */

public class Class {
    private String name;
    private String id;
    private String email;
    private String location;

    public Class(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Class(String name, String id, String email, String location) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.location = location;
    }

    public String getName(){
        return this.name;
    }

    public String getId(){
        return this.id;
    }

    public String getEmail(){
        return this.email;
    }

    public String getLocation(){
        return this.location;
    }
}
