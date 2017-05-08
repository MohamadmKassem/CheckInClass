package com.kassem.mohamad.checkinclass;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SpeceficStudentClass extends AppCompatActivity {

    int classid;
    String email;
    DatabaseHandler db;
    String result;
    SpeceficStudentClass m;
    String loc;
    ArrayList<StudentLecture> lc2;
    final LocationListener locationListener=new MyLocationListener2(this);
    GetStudentLectureDataThread gd;
    SendPresenceThread ST;
    LocationManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specefic_student_class);
        Intent I=getIntent();
        loc="";

        manager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            this.finish();
        }
        else
        {
            //LocationListener locationListener = new MyLocationListener(this);
            int i=reqpermission();
            try
            {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,0, locationListener);
                //Location l=manager.getLastKnownLocation(manager.getBestProvider(new Criteria(),false));
                //loc=l.getAltitude()+"//"+l.getLongitude();
                Toast.makeText(getApplicationContext(), "use gps", Toast.LENGTH_LONG).show();
            }
            catch(SecurityException e)
            {
                Toast.makeText(getApplicationContext(), "cannot use gps", Toast.LENGTH_LONG).show();
                this.finish();
            }
        }
        m=this;
        db=new DatabaseHandler(this);
        classid=Integer.valueOf(I.getStringExtra("ClassId"));
        email=I.getStringExtra("email");
        refreshStudentTab();
    }

    public void refreshStudentTab() {
        //Toast.makeText(getApplicationContext(),"here",Toast.LENGTH_SHORT).show();
        //final ArrayList<Class> lc2 =new ArrayList<Class>();
        result="";
        lc2=new ArrayList<StudentLecture>();
        gd=new GetStudentLectureDataThread(this,lc2);
        gd.execute(classid);
    }
    public void finishGetLec()
    {
        if(!result.equals("done:0") && !result.equals("failure"))
        {
            if(lc2.size()!=0) {
                LectureAdapter LA= new LectureAdapter(lc2);
                ListView LectureListView = (ListView) findViewById(R.id.LV2);
                LectureListView.setAdapter(LA);
            }
        }
        else if(result.equals("done:0"))
            Toast.makeText(getApplicationContext(),"no classes yet",Toast.LENGTH_SHORT).show();
        else Toast.makeText(getApplicationContext(),"no connection",Toast.LENGTH_SHORT).show();
        gd.cancel(true);
    }
    class LectureAdapter extends BaseAdapter {

        ArrayList<StudentLecture> lectures = new ArrayList<StudentLecture>();

        LectureAdapter(ArrayList<StudentLecture> l) {
            this.lectures = l;
        }

        @Override
        public int getCount() {
            return lectures.size();
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
            View view1 = layoutInflater.inflate(R.layout.student_lectures,null);

            TextView lectureNb = (TextView) view1.findViewById(R.id.lectureNb);
            TextView lecturedate= (TextView) view1.findViewById(R.id.lectureDate2);
            lectureNb.setText("Lecture "+lectures.get(i).number);
            lecturedate.setText(lectures.get(i).date);
            ImageView IV=(ImageView)view1.findViewById(R.id.img);
            if(lectures.get(i).here)
            {
                int idExitImage = getResources().getIdentifier("com.kassem.mohamad.checkinclass:drawable/ic_done_all_black_24dp" , null, null);
                IV.setImageResource(idExitImage);
            }
            else
            {
                IV.setTag(lectures.get(i).id);
                int idExitImage = getResources().getIdentifier("com.kassem.mohamad.checkinclass:drawable/ic_exposure_plus_1_black_24dp" , null, null);
                IV.setImageResource(idExitImage);
                if(lectures.get(i).open)
                {
                    IV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getApplicationContext(),"send presence...not real",Toast.LENGTH_SHORT).show();

                            ImageView i=(ImageView)v;
                            i.setClickable(false);
                            result="";
                            try
                            {
                                //manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5,0, locationListener);
                                //Location l=manager.getLastKnownLocation(manager.getBestProvider(new Criteria(),false));
                                //loc=l.getAltitude()+"//"+l.getLongitude();
                                ST=new SendPresenceThread(m,loc,email,(int)i.getTag());
                                ST.execute();
                                //Toast.makeText(getApplicationContext(), "use gps", Toast.LENGTH_LONG).show();
                            }
                            catch(SecurityException e)
                            {
                                //Toast.makeText(getApplicationContext(), "cannot use gps", Toast.LENGTH_LONG).show();
                                m.finish();
                            }


                        }
                    });
                }
            }
            return view1;
        }
    }
    public void finishSendPresence()
    {
        if(result.equals("done"))
            refreshStudentTab();
        else {


            Toast.makeText(getApplication(),result,Toast.LENGTH_SHORT).show();
            refreshStudentTab();
        }
        ST.cancel(true);
    }
    public int reqpermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(getApplicationContext(), "You should accept", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1340);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1340);
            }
        }
        return 1;
    }

}
