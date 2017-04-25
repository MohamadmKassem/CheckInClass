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
        Socket s;
        try {
            //s = new Socket("192.168.43.157",8082);

            s=new Socket();
            //s.connect(new InetSocketAddress("192.168.43.157",8082),4000); // alaa server
            s.connect(new InetSocketAddress("192.168.0.100",8082),4000); //alaa server by wifi
            //s.connect(new InetSocketAddress("192.168.1.66",8082),4000); // mohamad server
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            out.println("Presence--#--"+id+"--#--"+email+"--#--"+loc);
            String r=in.nextLine();
            if(r.equals("cannot add presence"))
                return"cannot add presence";
            else
            {
                String[] data=new String[3];
                data=r.split("--#--");

                Location profLoc=new Location("prof");
                profLoc.setAltitude(Double.parseDouble(data[1].split("//")[0]));
                profLoc.setLongitude(Double.parseDouble(data[1].split("//")[1]));
                while(m.loc.equals(""))
                {}
                Location stdloc=new Location("student");
                stdloc.setAltitude(Double.parseDouble(loc.split("//")[0]));
                stdloc.setLongitude(Double.parseDouble(loc.split("//")[1]));

                float dis=profLoc.distanceTo(stdloc);
                if(dis>Integer.valueOf(data[2]))
                {
                    out.println("fail");
                    return "cannot add presence "+dis;
                }
                else{
                    out.println("add");
                    out.println(dis);
                    if(in.nextLine().equals("done"))
                        return "done "+dis ;
                    else
                        return "cannot add presence";
                }
            }
            //DatagramSocket D = new DatagramSocket();
            //byte[] b ="hello".getBytes();
            //InetAddress ip = InetAddress.getByName("192.168.43.153");
            //DatagramPacket p;
            //p=new DatagramPacket(b,b.length,ip,8082);
            //D.send(p);

        }
        catch (Exception e)
        {
            //error=e.getMessage();
            return "failure";
        }

    }
    protected void onPostExecute(String r) {
        super.onPostExecute(r);
        if(r!="")m.result=r;
    }
}
