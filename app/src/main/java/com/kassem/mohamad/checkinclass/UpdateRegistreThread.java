package com.kassem.mohamad.checkinclass;

/**
 * Created by ALAA on 12/3/2017.

 */

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;


class UpdateRegistreThread extends AsyncTask<String, Void, String> {
    ProfPresence m;
    //ArrayList<Class> lc2;
    int lecid;
    UpdateRegistreThread(ProfPresence m,int lecId)
    {
        this.m=m;
        this.lecid=lecId;
    }
    protected String doInBackground(String...params) {
        PrintWriter out;
        Scanner in;
        Socket s=null;
        try {
            //s = new Socket("192.168.43.157",8082);
            s=new Socket();
            //s.connect(new InetSocketAddress("192.168.43.157",8082),4000); // alaa server by phn
            s.connect(new InetSocketAddress(m.getResources().getString(R.string.server_ip),8082),4000);
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            out.println("updateregistre--#--"+lecid);
            String more=in.nextLine();
            int nb=0;
            while(true) {
                if(more.equals(new String("end")))
                    break;
                int classid = Integer.valueOf(in.nextLine());
                if(nb==0)
                {
                    int done=m.db.deleteregistre(classid);
                    nb++;
                    if(done==0)
                        break;
                }
                String email = in.nextLine();
                String fullname = in.nextLine();
                m.db.addregistre(classid,email,fullname);
                more=in.nextLine();

            }
            s.close();
            if(more.equals(new String("end")))
            return "done";
            else return "bad connection";
        }
        catch (Exception e)
        {
            //error=e.getMessage();
            try {if(s!=null)s.close();}
            catch (IOException e1) {}
            finally {return "failure";}
        }

    }
    protected void onPostExecute(String r) {
        super.onPostExecute(r);
        m.result=r;
        //if(r.split("//")[0].equals("profClasses"))
           // m.finishUpdate();
        //else m.finishUpdate();
        m.finishGetPresence();
    }
}
