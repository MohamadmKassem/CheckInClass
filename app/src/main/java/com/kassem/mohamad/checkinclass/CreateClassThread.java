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


class CreateClassThread extends AsyncTask<String, Void, String> {
    MainActivity m;
    String email;
    String name;
    CreateClassThread(MainActivity m,String email,String name)
    {
        this.m=m;
        this.email=email;
        this.name=name;
    }
    protected String doInBackground(String...params) {
        PrintWriter out;
        Scanner in;
        Socket s=null;
        try {
            //s = new Socket("192.168.43.157",8082);

            s=new Socket();
           // s.connect(new InetSocketAddress("192.168.43.157",8082),4000); // alaa server
            s.connect(new InetSocketAddress(m.getResources().getString(R.string.server_ip),8082),4000);
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            out.println("addClass--#--"+email+"--#--"+name);
            String r=in.nextLine();
            //DatagramSocket D = new DatagramSocket();
            //byte[] b ="hello".getBytes();
            //InetAddress ip = InetAddress.getByName("192.168.43.153");
            //DatagramPacket p;
            //p=new DatagramPacket(b,b.length,ip,8082);
            //D.send(p);
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
        m.finishCreate();
    }
}
