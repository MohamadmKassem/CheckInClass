package com.kassem.mohamad.checkinclass;

/**
 * Created by Mohamad on 3/23/2017.
 */

public class Class {
    private String name;
    private String id;

    public Class(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public String getId(){
        return this.id;
    }
}
