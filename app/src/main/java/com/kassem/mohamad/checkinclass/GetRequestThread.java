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


class GetRequestThread extends AsyncTask<String, Void, String> {
    request_lectures m;
    String id;
    GetRequestThread(request_lectures m,String id) {
        this.m = m;
        this.id=id;
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
            out.println("getRequest--#--"+id);

            String more=in.nextLine();
            int nb=0;
            while(true) {
                if(more.equals(new String("end")))
                    break;
                nb++;
                String fullname = in.nextLine();
                String email= in.nextLine();
                Student S=new Student(fullname,email);
                m.AlS.add(S);
                more=in.nextLine();
            }
            s.close();
            return "done:"+nb;
        }
        catch (Exception e)
        {
            try {if(s!=null)s.close();}
            catch (IOException e1) {}
            finally {return "no connection";}
        }

    }
    protected void onPostExecute(String r) {
        super.onPostExecute(r);
        if(r!="") m.result=r;
        m.finishGet();
    }
}
