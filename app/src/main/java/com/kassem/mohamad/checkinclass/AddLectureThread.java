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


class AddLectureThread extends AsyncTask<String, Void, String> {
    prof_lectures m;
    int id;
    String date;
    AddLectureThread(prof_lectures m,String id,String date)
    {
        this.date=date;
        this.m=m;
        try{this.id=Integer.parseInt(id);}
        catch(Exception e){this.id=0;}
    }
    protected String doInBackground(String...params) {
        PrintWriter out;
        Scanner in;
        Socket s=null;
        try {
            //s = new Socket("192.168.43.157",8082);

            s=new Socket();
            //s.connect(new InetSocketAddress("192.168.43.157",8082),4000); // alaa server
            s.connect(new InetSocketAddress(m.getResources().getString(R.string.server_ip),8082),4000);
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            out.println("addLecture--#--"+id+"--#--"+date);
            String r=in.nextLine();
            m.db.addLecture(Integer.parseInt(r),date,id,"false");
            s.close();
            return r;
        }
        catch (Exception e)
        {
            try {if(s!=null)s.close();}
            catch (IOException e1) {}
            finally {return "";}
        }
    }
    protected void onPostExecute(String r) {
        super.onPostExecute(r);
        m.result=r;
        m.finishAddLecture();
    }
}
