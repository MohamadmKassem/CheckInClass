package com.kassem.mohamad.checkinclass;

/**
 * Created by ALAA on 12/3/2017.

 */

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.net.*;
import java.util.Scanner;


class GetLecturesDataThread extends AsyncTask<Integer, Void, String> {
    prof_lectures m;
    GetLecturesDataThread(prof_lectures m)
    {
        this.m=m;
    }
    protected String doInBackground(Integer...params) {
        PrintWriter out;
        Scanner in;
        Socket s;
        int classid =(int)params[0];
        try {
            //s = new Socket("192.168.43.157",8082);
            s=new Socket();
            //s.connect(new InetSocketAddress("192.168.43.157",8082),4000); // alaa server
            s.connect(new InetSocketAddress("192.168.0.100",8082),4000); //alaa server by wifi
            //s.connect(new InetSocketAddress("192.168.1.66",8082),4000); // mohamad server
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);

            out.println("GetLectures--#--"+classid);

            String more=in.nextLine();
            int nb=0;

            while(true) {
                if(more.equals(new String("end")))
                    break;
                nb++;
                String date = in.nextLine();
                String open= in.nextLine();
                if(open.equals("1"))
                    open="true";
                else open="false";
                int id=Integer.valueOf(in.nextLine());
                m.db.addLecture(id,date,classid,open);
                more=in.nextLine();

            }
            //m.refreshProfData(true);
            return "done:"+nb;
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
