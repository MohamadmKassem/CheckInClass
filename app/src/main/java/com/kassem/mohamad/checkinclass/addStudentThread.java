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


class addStudentThread extends AsyncTask<String, Void, String> {
    Student_of_lectures m;
    String email;
    String pass;
    int classid;
    addStudentThread(Student_of_lectures m,String email,String pass,int id)
    {
        classid=id;
        this.m=m;
        this.email=email;
        this.pass=pass;
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
            out.println("addRegistre--#--"+email+"--#--"+pass+"--#--"+classid);
            String r=in.nextLine();
            if(r.split("--#--")[0].equals("done"))
                m.db.addregistre(classid,email,r.split("--#--")[1]);
            s.close();
            return r.split("--#--")[0];
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
       m.finishAddStudent();
    }
}
