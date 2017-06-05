package com.kassem.mohamad.checkinclass;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
public class SpeceficStudentClass extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient googleApiClient; // lib com.google.android.gms:play-services
    Location location;
    int classid;
    String email;
    DatabaseHandler db;
    String result;
    SpeceficStudentClass m;
    String loc;
    ArrayList<StudentLecture> lc2;
    LocationListener locationListener;
    GetStudentLectureDataThread gd;
    SendPresenceThread ST;
    LocationManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specefic_student_class);
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        Intent I=getIntent();
        loc="";
        locationListener=null;
        manager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Toast.makeText(this,"turn on gps",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            this.finish();
        }
        else
        {
            int i=reqpermission();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                this.finish();
            try{
                locationListener=new MyLocationListener2(this);
                manager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,1, 1, locationListener);
            }
            catch (Exception e)
            {this.finish();}

        }
        m=this;
        db=new DatabaseHandler(this);
        classid=Integer.valueOf(I.getStringExtra("ClassId"));
        email=I.getStringExtra("email");
        String classname=I.getStringExtra("className");
        TextView t=(TextView)findViewById(R.id.classN);
        t.setText(classname);
        //this.getActionBar().setTitle(classname);
        refreshStudentTab();
    }
    @Override
    public void onStart() {
        googleApiClient.connect();

        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
    public void onConnected(@Nullable Bundle bundle) {
        /*if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if(location != null)
            loc="" + location.getAltitude() + "//" + location.getLongitude();


        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new com.google.android.gms.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(location != null)
                    loc="" + location.getLatitude() + "//" + location.getLongitude();
            }
        });*/
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
            Toast.makeText(getApplicationContext(),"no lectures yet",Toast.LENGTH_SHORT).show();
        else Toast.makeText(getApplicationContext(),"no connection",Toast.LENGTH_SHORT).show();
        gd.cancel(true);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                int idExitImage = getResources().getIdentifier("com.kassem.mohamad.checkinclass:drawable/ic_adjust_black_24dp" , null, null);
                IV.setImageResource(idExitImage);
            }
            else
            {

                if(lectures.get(i).open)
                {   IV.setTag(lectures.get(i).id);
                    int idExitImage = getResources().getIdentifier("com.kassem.mohamad.checkinclass:drawable/ic_adjust_black2_24dp" , null, null);
                    IV.setImageResource(idExitImage);
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
            Toast.makeText(getApplication(),result,Toast.LENGTH_SHORT).show();
            refreshStudentTab();

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
