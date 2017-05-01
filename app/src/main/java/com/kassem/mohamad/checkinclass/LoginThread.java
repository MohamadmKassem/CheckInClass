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


class LoginThread extends AsyncTask<String, Void, String> {
    LoginActivity m;
    LoginThread(LoginActivity m)
    {
        this.m=m;
    }
    protected String doInBackground(String...params) {
        PrintWriter out;
        Scanner in;
        Socket s=null;
        try {
            //s = new Socket("192.168.43.157",8082);
            s=new Socket();
            //s.connect(new InetSocketAddress("192.168.43.157",8082),4000); // alaa server phn
            s.connect(new InetSocketAddress("192.168.0.100",8082),4000);//wifi alaa
            //s.connect(new InetSocketAddress("192.168.1.66",8082),4000); // mohamad server
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            out.println("login--#--"+params[0]+"--#--"+params[1]);
            String r=in.nextLine();
            s.close();
            return r;
        }
        catch (Exception e)
        {
            try {if(s!=null)s.close();}
            catch (IOException e1) {}
            finally {return "error";}
        }

    }
    protected void onPostExecute(String r) {
        super.onPostExecute(r);
        m.result=r;
        m.finishlogin();
    }
}
