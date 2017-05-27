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
    int Lec;
    GetPresenceThread(ProfPresence m)
    {
        this.m=m;
    }
    protected String doInBackground(Integer...params) {
        PrintWriter out;
        Scanner in;
        Socket s=null;
        int LectureId =(int)params[0];
        Lec=LectureId;
        try {
            s=new Socket();
            //s.connect(new InetSocketAddress("192.168.43.157",8082),4000); // alaa server
            s.connect(new InetSocketAddress(m.getResources().getString(R.string.server_ip),8082),4000);
            ArrayList<presence> AP=new ArrayList<presence>();
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            out.println("GetPresence--#--"+LectureId);
            String more=in.nextLine();
            int nb=0;
            while(true) {
                errorThread e=new errorThread();
                nb++;
                if(more.equals(new String("end")))
                    break;
                String fullname = in.nextLine();
                String email=in.nextLine();
                boolean here=in.nextLine().equals("true");

                presence p=new presence(fullname,email,here);
                AP.add(p);
                more=in.nextLine();
            }
            //m.refreshProfData(true);
            String r="";
            if(more.equals(new String("end")))
            {
                r=m.db.updatePresence(LectureId,AP);
                if(r.equals("done"))
                    return "done";
            }
            s.close();
            return "try again";
        }
        catch (Exception e)
        {
            //error=e.getMessage();
            try {if(s!=null)s.close();}
            catch (IOException e1) {}
            finally {return "no connection";}
        }

    }
    protected void onPostExecute(String r) {
        super.onPostExecute(r);
        if(r!="")m.result=r;
        m.UpdateRegistreIfneed();
       // m.finishGetPresence();
    }
}
