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


class AddClassThread extends AsyncTask<String, Void, String> {
    MainActivity m;
    int id;
    AddClassThread(MainActivity m,String id)
    {
        this.m=m;
        System.out.println("thread1");

        this.id=Integer.parseInt(id);
        System.out.println("thread2");
    }
    protected String doInBackground(String...params) {
        PrintWriter out;
        Scanner in;
        Socket s;
        try {
            System.out.println("thread");
            //s = new Socket("192.168.43.157",8082);
            
            s=new Socket();
            s.connect(new InetSocketAddress("192.168.43.157",8082),4000);
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            out.println("searchClass--#--"+id);
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
