package com.kassem.mohamad.checkinclass;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class MyService extends Service {
    String email;
    MyThread m;
    public MyService() {
    }
    public int onStartCommand(Intent i,int f,int s)
    {
        email=i.getStringExtra("email");
        m=new MyThread();
        m.start();
        return START_REDELIVER_INTENT;
    }
    public class MyThread extends Thread
    {
        public void run()
        {
            // TODO Auto-generated method stub
            while(true)
            {
                try
                {
                    PrintWriter out;
                    Scanner in;
                    Socket s=null;
                    s=new Socket();
                    s.connect(new InetSocketAddress("192.168.0.100",8082),4000); //alaa server by wifi
                    in =new Scanner(s.getInputStream());
                    out = new PrintWriter(s.getOutputStream(),true);
                    out.println("getNotification--#--"+email);
                    String more=in.nextLine();
                    int nb=0;
                    while(true) {
                        if(more.equals(new String("end")))
                            break;
                        nb++;
                        String type=in.nextLine();
                        String result=in.nextLine();
                        Intent i=new Intent();
                        i.setAction("com.example.broadcast");
                        i.putExtra("result",result);
                        i.putExtra("type",type);
                        i.putExtra("id",nb);
                        sendBroadcast(i);
                        more=in.nextLine();
                    }
                    s.close();
                    Thread.sleep(10000);
                }
                catch(Exception e)
                {}
            }
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void onDestroy()
    {
        m.stop();
    }
}
