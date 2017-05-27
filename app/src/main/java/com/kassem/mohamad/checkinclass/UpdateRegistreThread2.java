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


class UpdateRegistreThread2 extends AsyncTask<String, Void, String> {
    Student_of_lectures m;
    //ArrayList<Class> lc2;
    int classid;
    UpdateRegistreThread2(Student_of_lectures m,int ClassId)
    {
        this.m=m;
        this.classid=ClassId;
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
            //s.connect(new InetSocketAddress("192.168.1.66",8082),4000); // mohamad server
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            out.println("updateregistre2--#--"+classid);
            String more=in.nextLine();
            int nb=0;
            while(true) {
                if(more.equals(new String("end")))
                    break;
                //int classid = Integer.valueOf(in.nextLine());
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
        m.students=m.db.getStudents(classid);
        m.refreshPageData();
    }
}
