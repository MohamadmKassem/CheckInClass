package com.kassem.mohamad.checkinclass;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProfPresence extends AppCompatActivity {

    int LectureId;
    DatabaseHandler db;
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_presence);
        Intent I=getIntent();
        LectureId=I.getIntExtra("lectureId",0);
        db=new DatabaseHandler(this);
        UpdatePresence();

    }
    public void UpdatePresence()
    {
        final GetPresenceThread GPT=new GetPresenceThread(this);
        result="";
        GPT.execute(LectureId);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run()
                    {
                        if(!result.equals("done") && !result.equals(""))
                            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                        ArrayList<presence> AP=db.getPresence(LectureId);
                        Toast.makeText(getApplicationContext(),""+AP.size(),Toast.LENGTH_SHORT).show();
                        refreshPageData(AP);
                        GPT.cancel(true);
                    }
                }, 3500);
    }
    void refreshPageData(ArrayList<presence> a)
    {
        if(a.size()!=0) {
            PresenceAdapter l = new PresenceAdapter(a);
            ListView lv = (ListView) findViewById(R.id.LV3);
            lv.setAdapter(l);
        }
    }
    class PresenceAdapter extends BaseAdapter {

        ArrayList<presence> presence = new ArrayList<presence>();

        PresenceAdapter(ArrayList<presence> presence) {
            this.presence = presence;
        }

        @Override
        public int getCount() {
            return presence.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater layoutInflater = getLayoutInflater();
            View view1 = layoutInflater.inflate(R.layout.presence_layout,null);

            TextView fullname = (TextView) view1.findViewById(R.id.presenceFullName);
            TextView email = (TextView) view1.findViewById(R.id.presenceEmail);
            ImageView IV=(ImageView)view1.findViewById(R.id.img2);
            fullname.setText(presence.get(i).fullname);
            email.setText(presence.get(i).email);
            if(presence.get(i).here) {
                int idExitImage = getResources().getIdentifier("com.kassem.mohamad.checkinclass:drawable/ic_done_all_black_24dp", null, null);
                IV.setImageResource(idExitImage);
                IV.setTag(new String("to false--#--"+presence.get(i).email));
            }
            else
            {
                int idExitImage = getResources().getIdentifier("com.kassem.mohamad.checkinclass:drawable/ic_exposure_plus_1_black_24dp" , null, null);
                IV.setImageResource(idExitImage);
                IV.setTag(new String("to true--#--"+presence.get(i).email));
            }
            IV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView IV=(ImageView)v;
                    String t=(String)IV.getTag();
                    //changePresenceThread cP=new changePresenceThread()
                }
            });
            return view1;
        }
    }
}
