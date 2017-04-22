package com.kassem.mohamad.checkinclass;

/**
 * Created by ALAA on 21/4/2017.
 */

public class StudentLecture {
    String date;
    boolean open;
    boolean here;
    int number;
    int id;
    StudentLecture(int id,String date,boolean open,int number,boolean here)
    {
        this.id=id;
        this.date=date;
        this.open=open;
        this.number=number;
        this.here=here;
    }
}
