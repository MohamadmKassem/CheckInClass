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

import static android.os.SystemClock.sleep;


class OpenCloseLectureThread extends AsyncTask<String, Void, String> {
    prof_lectures m;
    int id;
    String open;
    int time;
    String loc;
    int distance;
    OpenCloseLectureThread(prof_lectures m,int id,String open,int time,String loc,int d)
    {
        this.distance=d;
        this.loc=loc;
        this.time=time;
        this.m=m;
        this.id=id;
        this.open=open;
    }
    protected String doInBackground(String...params) {
        PrintWriter out;
        Scanner in;
        Socket s=null;
        try {
            //s = new Socket("192.168.43.157",8082);
            if(m.loc.equals(""))
            {
                sleep(4000);
            }
            if(m.loc.equals(""))
            {
             return"cannot get locatin try again";
            }
            loc=m.loc;
            s=new Socket();
            //s.connect(new InetSocketAddress("192.168.43.157",8082),1500); // alaa server
            s.connect(new InetSocketAddress(m.getResources().getString(R.string.server_ip),8082),4000); //alaa server by wifi
            //s.connect(new InetSocketAddress("192.168.1.66",8082),4000); // mohamad server
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            out.println("changeLecture--#--"+id+"--#--"+open+"--#--"+loc+"--#--"+time+"--#--"+distance);
            String r=in.nextLine();
            s.close();
            return r;
        }
        catch (Exception e)
        {
            //error=e.getMessage();
            try {if(s!=null)s.close();}
            catch (IOException e1) {}
            finally {return "failure:0";}

        }

    }
    protected void onPostExecute(String r) {
        super.onPostExecute(r);
        if(r!="")m.result=r;
        m.finishOpenClose();
    }
}
