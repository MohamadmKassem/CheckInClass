package com.kassem.mohamad.checkinclass;

/**
 * Created by ALAA on 15/4/2017.
 */

public class Lecture {
    public String date;
    public int id;
    public int nb;
    public int classId;
    public Boolean open;

    public Lecture(String d,Boolean b,int nb,int id,int classId)
    {
        this.classId=classId;
        this.id=id;
        this.nb=nb;
        date=d;
        open=b;
    }

}
