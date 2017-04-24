package com.kassem.mohamad.checkinclass;

/**
 * Created by ALAA on 12/3/2017.

 */

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;


class GetPresenceThread extends AsyncTask<Integer, Void, String> {
     ProfPresence m;
    GetPresenceThread(ProfPresence m)
    {
        this.m=m;
    }
    protected String doInBackground(Integer...params) {
        PrintWriter out;
        Scanner in;
        Socket s;
        int LectureId =(int)params[0];
        try {
            s=new Socket();
            //s.connect(new InetSocketAddress("192.168.43.157",8082),4000); // alaa server
            s.connect(new InetSocketAddress("192.168.0.100",8082),4000); //alaa server by wifi
            //s.connect(new InetSocketAddress("192.168.1.66",8082),4000); // mohamad server
            ArrayList<presence> AP=new ArrayList<presence>();
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            out.println("GetPresence--#--"+LectureId);
            String more=in.nextLine();
            int nb=0;
            String rf="";
            while(true) {
                errorThread e=new errorThread();
                rf+="/"+nb;
                nb++;
                if(more.equals(new String("end")))
                    break;
                String fullname = in.nextLine();
                String email=in.nextLine();
                boolean here=in.nextLine().equals("true");

                presence p=new presence(fullname,email,here);
                AP.add(p);
                rf+="/"+AP.size();
                more=in.nextLine();
            }
            //m.refreshProfData(true);
            String r="";
            if(more.equals(new String("end")))
            {
                r=m.db.updatePresence(LectureId,AP);
                if(r.equals("done"))
                    return rf;
            }
            return rf+"--"+r;
        }
        catch (Exception e)
        {
            //error=e.getMessage();
            return e.toString();
        }

    }
    protected void onPostExecute(String r) {
        super.onPostExecute(r);
        if(r!="")m.result=r;
    }
}
