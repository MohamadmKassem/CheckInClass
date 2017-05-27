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


class answerToReqThread extends AsyncTask<String, Void, String> {
    request_lectures m;
    int id;
    answerToReqThread(request_lectures m)
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
            //s.connect(new InetSocketAddress("192.168.43.157",8082),2500); // alaa server
            s.connect(new InetSocketAddress(m.getResources().getString(R.string.server_ip),8082),4000);
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            out.println(params[0]);
            String r=in.nextLine();
            //DatagramSocket D = new DatagramSocket();
            //byte[] b ="hello".getBytes();
            //InetAddress ip = InetAddress.getByName("192.168.43.153");
            //DatagramPacket p;
            //p=new DatagramPacket(b,b.length,ip,8082);
            //D.send(p);
            s.close();
            if(r.split("--#--")[0].equals("done") && params[0].split("--#--")[0].equals("AcceptReq"))
                m.db.addregistre(Integer.valueOf(params[0].split("--#--")[1]),params[0].split("--#--")[2],r.split("--#--")[1]);
            return params[0].split("--#--")[0]+"//"+r.split("--#--")[0];
        }
        catch (Exception e)
        {
            try {if(s!=null)s.close();}
            catch (IOException e1) {}
            finally {return params[0].split("--#--")[0] + "//error";}
        }
    }
    protected void onPostExecute(String r) {
        super.onPostExecute(r);
        if(r!="") m.result=r.split("//")[1];
        if(r.split("//")[0].equals("AcceptReq"))
            m.finishAccResp();
        else m.finishReject();
    }
}
