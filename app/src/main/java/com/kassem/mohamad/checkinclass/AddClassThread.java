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
            s.connect(new InetSocketAddress("192.168.0.100",8082),4000); //alaa server by wifi
            //s.connect(new InetSocketAddress("192.168.1.66",8082),4000); // mohamad server
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            out.println("searchClass--#--"+id);
            String r=in.nextLine();
            s.close();
            return r;
        }
        catch (Exception e)
        {
            //error=e.getMessage();
            try {if(s!=null)s.close();}
            catch (IOException e1) {}
            finally {return "";}
        }
    }
    protected void onPostExecute(String r) {
        super.onPostExecute(r);
        m.result=r;
        m.finishAddClass();
    }
}
