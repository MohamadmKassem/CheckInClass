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


class SignUpThread extends AsyncTask<String, Void, String> {

    SignupActivity m;

    public SignUpThread(SignupActivity m) {
        this.m = m;
    }

    protected String doInBackground(String...params) {
        PrintWriter out;
        Scanner in;
        Socket s=null;
        try {
            //s = new Socket("192.168.43.157",8082);
            s = new Socket();
            s.connect(new InetSocketAddress(m.getResources().getString(R.string.server_ip),8082),4000);
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            out.println("signUp--#--"+params[0]+"--#--"+params[1]+"--#--"+params[2]);
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
        if(r != "")
            m.result=r;
    }
}
