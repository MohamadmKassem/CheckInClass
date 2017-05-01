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
    GetPresenceThread GPT;
    ProfPresence m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_presence);
        Intent I=getIntent();
        m=this;
        LectureId=I.getIntExtra("lectureId",0);
        db=new DatabaseHandler(this);
        UpdatePresence();

    }
    public void UpdatePresence()
    {
        GPT=new GetPresenceThread(this);
        result="";
        GPT.execute(LectureId);
    }
    public void UpdateRegistreIfneed()
    {
        int i=m.db.getRegistreCount(LectureId);
        if(i==0)
        {
            UpdateRegistreThread u=new UpdateRegistreThread(m,LectureId);
            u.execute();
        }
        else{m.finishGetPresence();}
    }
    public void finishGetPresence()
    {
        if(!result.equals("done") && !result.equals(""))
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
        ArrayList<presence> AP=db.getPresence(LectureId);
        refreshPageData(AP);
        GPT.cancel(true);
    }
    void refreshPageData(ArrayList<presence> a)
    {
        if(a.size()!=0) {
            PresenceAdapter l = new PresenceAdapter(a);
            final TextView t=(TextView) findViewById(R.id.presence);

            t.setBackgroundColor(getResources().getColor(R.color.green));
            final int p=db.getPresenceCount(LectureId);
            t.setText("presence:"+p);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            t.setBackgroundColor(getResources().getColor(R.color.red));
                            final int np=db.getnotPresenceCount(LectureId);
                            t.setText("not presence:"+np);
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            t.setBackgroundColor(getResources().getColor(R.color.white));
                                            int s=p+np;
                                            t.setText(p+"/"+s);
                                        }
                                    }
                                    , 2000);
                        }
                    }
                    , 2000);

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
           // Toast.makeText(getApplicationContext(),presence.get(i).fullname,Toast.LENGTH_SHORT).show();
            email.setText(presence.get(i).email);
            if(presence.get(i).here) {
                int idExitImage = getResources().getIdentifier("com.kassem.mohamad.checkinclass:drawable/ic_done_all_black_24dp", null, null);
                IV.setImageResource(idExitImage);
                IV.setTag(new String("to false--#--"+presence.get(i).email+"--#--"+LectureId));
            }
            else
            {
                int idExitImage = getResources().getIdentifier("com.kassem.mohamad.checkinclass:drawable/ic_exposure_plus_1_black_24dp" , null, null);
                IV.setImageResource(idExitImage);
                IV.setTag(new String("to true--#--"+presence.get(i).email+"--#--"+LectureId));
            }
            IV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView IV=(ImageView)v;
                    String t=(String)IV.getTag();
                    changePresenceThread cP=new changePresenceThread(m,t);
                    cP.execute();
                }
            });
            return view1;
        }
    }
    public void finishChange()
    {
        //Toast.makeText(getApplicationContext(),"here",Toast.LENGTH_SHORT).show();
        //UpdatePresence();
        finishGetPresence();
    }

}
