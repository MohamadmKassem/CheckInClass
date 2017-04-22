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


class GetStudentLectureDataThread extends AsyncTask<Integer, Void, String> {
    SpeceficStudentClass m;
    ArrayList<StudentLecture> lc2;
    String email;

    GetStudentLectureDataThread(SpeceficStudentClass m,ArrayList<StudentLecture> lc2)
    {
        this.lc2=lc2;
        this.m=m;
        email=m.email;
    }
    protected String doInBackground(Integer...params) {
        PrintWriter out;
        Scanner in;
        Socket s;
        int classid =(int)params[0];
        try {
            //s = new Socket("192.168.43.157",8082);
            s=new Socket();
            s.connect(new InetSocketAddress("192.168.43.157",8082),4000); // alaa server
            //s.connect(new InetSocketAddress("192.168.1.66",8082),4000); // mohamad server
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            out.println("GetStudentLectures--#--"+classid+"--#--"+email);
            String more=in.nextLine();
            int nb=0;
            while(true) {
                if(more.equals(new String("end")))
                    break;
                nb++;
                String date = in.nextLine();
                boolean open =in.nextLine().equals("true");
                boolean here=in.nextLine().equals("true");
                int id=Integer.valueOf(in.nextLine());
                StudentLecture SL=new StudentLecture(id,date,open,nb,here);
                lc2.add(SL);
                more=in.nextLine();
            }
            //m.refreshProfData(true);
            return "done:"+nb;
        }
        catch (Exception e)
        {
            //error=e.getMessage();
            return "failure";
        }

    }
    protected void onPostExecute(String r) {
        super.onPostExecute(r);
        if(r!="")m.result=r;
    }
}
