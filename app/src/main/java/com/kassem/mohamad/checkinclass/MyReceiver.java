package com.kassem.mohamad.checkinclass;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase("com.example.broadcast"))
        {
            final Resources res = context.getResources();
            final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.button_shape);
            //int id=intent.getIntExtra("id",0);
            String result = intent.getStringExtra("result");
            String type=intent.getStringExtra("type");
            //Toast.makeText(context,"Broadcast:receve...",Toast.LENGTH_SHORT).show();
            NotificationCompat.Builder mBuilder;
            NotificationManager mNotifyManager;
            mNotifyManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(context);
            // Toast.makeText(context, type+"  "+result , Toast.LENGTH_SHORT).show();
            if(type.equals("request"))
            {
//                Toast.makeText(context,"Broadcast:request",Toast.LENGTH_SHORT).show();
                /*Intent myIntent = new Intent(context, request_lectures.class);
                myIntent.putExtra("ClassId",result.split("//")[1]);

                mBuilder.setContentTitle("request for registration");
                mBuilder.setContentText(result.split("//")[0]+" want to registre on classid="+result.split("//")[1]);
                mBuilder.setSmallIcon(R.drawable.button_shape);
                mBuilder.setLargeIcon(picture);
                mBuilder.setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                myIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT));
                mNotifyManager.notify(1, mBuilder.build());*/

                NewMessageNotification n=new NewMessageNotification();
                n.notify(context,result,1);
            }
            else
                if(type.equals("Accepted"))
                {
  //                  Toast.makeText(context,"Broadcast:Accepted",Toast.LENGTH_SHORT).show();
                    /*Intent myIntent = new Intent(context, MainActivity.class);
                    mBuilder.setContentTitle("Accepted on class");
                    mBuilder.setContentText("you are accepted on course of id "+result);
                    mBuilder.setSmallIcon(R.drawable.button_shape);
                    mBuilder.setLargeIcon(picture);
                    mBuilder.setContentIntent(
                            PendingIntent.getActivity(
                                    context,
                                    0,
                                    myIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT));
                    mNotifyManager.notify(2, mBuilder.build());*/

                    NewMessageNotification2 n=new NewMessageNotification2();
                    n.notify(context,result,2);
                }
            else if(type.equals("presence"))
                {
    //                Toast.makeText(context,"Broadcast:presence",Toast.LENGTH_SHORT).show();
                    /*Intent myIntent = new Intent(context, ProfPresence.class);
                    myIntent.putExtra("lectureId",Integer.valueOf(result.split("//")[3]));
                    mBuilder.setContentTitle("presence of class "+result.split("//")[2]);
                    mBuilder.setContentText(Integer.valueOf(result.split("//")[0])+" of "+Integer.valueOf(result.split("//")[1]));
                    mBuilder.setSmallIcon(R.drawable.button_shape);
                    mBuilder.setLargeIcon(picture);
                    mBuilder.setContentIntent(
                            PendingIntent.getActivity(
                                    context,
                                    0,
                                    myIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT));
                    mNotifyManager.notify(Integer.valueOf(result.split("//")[3]), mBuilder.build());*/

                    NewMessageNotification3 n=new NewMessageNotification3();
                    n.notify(context,result,3);
                }

        }
    }
}
