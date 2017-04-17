package com.kassem.mohamad.checkinclass;

/**
 * Created by ALAA on 17/4/2017.
 */

public class closingAfterTimeThread extends Thread {
    private int time;
    DatabaseHandler d;
    int id;
    closingAfterTimeThread(int time,DatabaseHandler d,int id)
    {
        this.id=id;
        this.time=time;
        this.d=d;
    }
    public void run()
    {
        try {
            sleep(1000*60*time);
            d.setlecture("false",id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
