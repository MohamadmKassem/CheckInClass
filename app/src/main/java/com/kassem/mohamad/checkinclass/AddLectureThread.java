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
        Socket s;
        try {
            //s = new Socket("192.168.43.157",8082);

            s=new Socket();
            //s.connect(new InetSocketAddress("192.168.43.157",8082),4000); // alaa server
            s.connect(new InetSocketAddress("192.168.0.100",8082),4000); //alaa server by wifi
            //s.connect(new InetSocketAddress("192.168.1.66",8082),4000); // mohamad server
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            out.println("addLecture--#--"+id+"--#--"+date);
            String r=in.nextLine();
            //DatagramSocket D = new DatagramSocket();
            //byte[] b ="hello".getBytes();
            //InetAddress ip = InetAddress.getByName("192.168.43.153");
            //DatagramPacket p;
            //p=new DatagramPacket(b,b.length,ip,8082);
            //D.send(p);
            return r;
        }
        catch (Exception e)
        {
            //error=e.getMessage();
            return "";
        }

    }
    protected void onPostExecute(String r) {
        super.onPostExecute(r);
        if(r!="") m.result=r;
    }
}
