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


class DeleteStudentThread extends AsyncTask<String, Void, String> {
    Student_of_lectures m;
    int id;
    String email;
    DeleteStudentThread(Student_of_lectures m,int id,String email)
    {
        this.m=m;
        this.id=id;
        this.email=email;
    }
    protected String doInBackground(String...params) {
        PrintWriter out;
        Scanner in;
        Socket s=null;
        try {
            //s = new Socket("192.168.43.157",8082);

            s=new Socket();
            // s.connect(new InetSocketAddress("192.168.43.157",8082),4000); // alaa server
            s.connect(new InetSocketAddress("192.168.0.100",8082),4000); //alaa server by wifi
            //s.connect(new InetSocketAddress("192.168.1.66",8082),4000); // mohamad server
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            out.println("deleteStudent--#--"+id+"--#--"+email);
            String r=in.nextLine();
            if(r.equals("done"))
                m.db.deleteStudent(id,email);
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

        m.students=m.db.getStudents(id);
        m.refreshPageData();
    }
}
