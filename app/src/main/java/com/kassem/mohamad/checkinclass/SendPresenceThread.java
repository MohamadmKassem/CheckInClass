package com.kassem.mohamad.checkinclass;

/**
 * Created by ALAA on 12/3/2017.

 */

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import static android.os.SystemClock.sleep;


class SendPresenceThread extends AsyncTask<String, Void, String> {
    SpeceficStudentClass m;
    int id;
    String email;
    String loc;
    SendPresenceThread(SpeceficStudentClass m,String loc,String email,int lectId)
    {
        this.email=email;
        this.m=m;
        this.id=lectId;
        this.loc=loc;
    }
    protected String doInBackground(String...params) {
        PrintWriter out;
        Scanner in;
        Socket s=null;
        try {
            //s = new Socket("192.168.43.157",8082);

            s=new Socket();
            //s.connect(new InetSocketAddress("192.168.43.157",8082),4000); // alaa server
            s.connect(new InetSocketAddress("192.168.0.100",8082),4000); //alaa server by wifi
            //s.connect(new InetSocketAddress("192.168.1.66",8082),4000); // mohamad server
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            if(m.loc.equals(""))
                sleep(3000);
            if(m.loc.equals(""))
                return "please try agian";
            loc=m.loc;
            out.println("Presence--#--"+id+"--#--"+email+"--#--"+loc);
            String r=in.nextLine();
            if(r.equals("cannot add presence"))
                return"cannot add presence";
            else
            {
                String[] data=new String[3];
                data=r.split("--#--");
                if(loc.equals(""))
                    return "failure";
                Location profLoc=new Location("prof");
                profLoc.setAltitude(Double.parseDouble(data[1].split("//")[0]));
                profLoc.setLongitude(Double.parseDouble(data[1].split("//")[1]));

                Location stdloc=new Location("student");
                stdloc.setAltitude(Double.parseDouble(m.loc.split("//")[0]));
                stdloc.setLongitude(Double.parseDouble(m.loc.split("//")[1]));

                float dis=profLoc.distanceTo(stdloc);
                if(dis>Float.parseFloat(data[2]))
                {
                    out.println("fail");
                    s.close();
                    return "cannot add presence "+dis;
                }
                else{
                    out.println("add");
                    out.println(dis);
                    if(in.nextLine().equals("done"))
                    {
                        s.close();
                        return "done " + dis;
                    }
                    else
                    {
                        s.close();
                        return "cannot add presence";
                    }
                }
            }
        }
        catch (Exception e)
        {
            //error=e.getMessage();
            try {if(s!=null)s.close();}
            catch (IOException e1) {}
            finally {return loc;}
        }

    }
    protected void onPostExecute(String r) {
        super.onPostExecute(r);
        if(r!="")m.result=r;
        m.finishSendPresence();
    }
}
