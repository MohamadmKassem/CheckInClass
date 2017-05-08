package com.kassem.mohamad.checkinclass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.FileInputStream;

import static com.kassem.mohamad.checkinclass.MainActivity.request_code;

public class AutoStart extends BroadcastReceiver {
    public AutoStart() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            String email="";
            FileInputStream inputStream = context.openFileInput("login");
            int i;
            while((i=inputStream.read()) != -1){
                email += String.valueOf((char)i);
            }
            if(!email.equals("")){
                Intent in = new Intent(context, MyService.class);
                in.putExtra("email",email);
                context.startService(in);
            }
        }
        catch (Exception ex){
            Toast.makeText(context,ex.toString(),Toast.LENGTH_SHORT).show();
        }
    }
}
