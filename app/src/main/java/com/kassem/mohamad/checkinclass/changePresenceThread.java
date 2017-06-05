package com.kassem.mohamad.checkinclass;

/**
 * Created by ALAA on 12/3/2017.

 */

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.net.*;
import java.util.Scanner;


class changePresenceThread extends AsyncTask<String, Void, String> {
    ProfPresence m;
    String StudentEmail;
    int LectureId;
    String todo;
    changePresenceThread(ProfPresence m,String todo)
    {
        this.m=m;
        LectureId=Integer.valueOf(todo.split("--#--")[2]);
        StudentEmail=todo.split("--#--")[1];
        this.todo=todo.split("--#--")[0];
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
            out.println("changePresence--#--"+todo+"--#--"+StudentEmail+"--#--"+LectureId);
            String r=in.nextLine();
            if(r.equals("cannot change presence"))
                return"cannot change presence";
            else
            {
                if(todo.equals("to false"))
                m.db.changepresence(LectureId,StudentEmail,0);
                else m.db.changepresence(LectureId,StudentEmail,1);
                return"done";
            }
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
        if(r!="")m.result=r;
        m.finishChange();
    }
}
