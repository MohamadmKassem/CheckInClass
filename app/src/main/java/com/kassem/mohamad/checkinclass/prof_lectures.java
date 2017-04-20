package com.kassem.mohamad.checkinclass;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.data;

public class prof_lectures extends AppCompatActivity {

    String classId;
    DatabaseHandler db;
    prof_lectures m;
    String result;
    LecturesAdapter lectureAdapter;
    ArrayList<Lecture> createdLectures;
    String loc;
    final LocationListener locationListener=new MyLocationListener(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_lectures);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent I=getIntent();
        classId=I.getStringExtra("ClassId");
        //Toast.makeText(this, classId, Toast.LENGTH_SHORT).show();
        db=new DatabaseHandler(this);
        m=this;
        loc="";

        LocationManager manager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            this.finish();
        }
        else
        {
            LocationListener locationListener = new MyLocationListener(this);
            int i=reqpermission();
            try
            {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);
                Toast.makeText(getApplicationContext(), "use gps", Toast.LENGTH_LONG).show();
            }
            catch(SecurityException e)
            {
                Toast.makeText(getApplicationContext(), "cannot use gps", Toast.LENGTH_LONG).show();
                this.finish();

            }
        }

        //LocationListener locationListener = new MyLocationListener(this);
        //WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //WifiInfo info = manager.getConnectionInfo();
        //String address = info.getMacAddress();
        //Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();
        refreshLectures(false);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog diaBox = AskOption(classId);
                diaBox.show();
            }
        });
    }
    private AlertDialog AskOption(String s)
    {
        final String s1=s;
        final Date d=new Date();
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Add lecture")
                .setMessage("are you sure want to add lecture at "+d)


                .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        //db.delete(s1);
                        addLecture(s1,d.toString());
                        dialog.dismiss();

                    }

                })



                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }
    public void addLecture(String s1,String d)//classid and date
    {
        final String date=d;
        final String classid=s1;
        final ProgressDialog progressDialog = new ProgressDialog(prof_lectures.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Add Lecture please wait ...");
        progressDialog.show();

        AddLectureThread a=new AddLectureThread(m,s1,d);
        a.execute();
        result="";
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run()
                    {
                        if(result!="")
                        {
                            db.addLecture(Integer.parseInt(result),date,Integer.parseInt(classid),"false");

                            refreshLectures(false);
                        }else Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
                    }
                }, 5000);
    }
    public void refreshLectures(boolean b)
    {
         ArrayList<Lecture> a=db.getLectures(Integer.parseInt(classId));
         if(a.size()==0 && b==false)
         {
             GetLecturesDataThread gd=new GetLecturesDataThread(this);
             result="done:0";
             gd.execute(Integer.parseInt(classId));

             new android.os.Handler().postDelayed(
                     new Runnable() {
                         public void run()
                         {
                             if(!result.equals("done:0") && !result.equals("failure"))
                             {
                                 Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                 refreshLectures(true);
                             }
                             else if(result.equals("done:0"))
                                 Toast.makeText(getApplicationContext(),"no lectures yet",Toast.LENGTH_SHORT).show();
                             else Toast.makeText(getApplicationContext(),"no connection",Toast.LENGTH_SHORT).show();
                         }
                     }, 3500);
         }
         if(a.size()!=0) {
             LecturesAdapter l = new LecturesAdapter(a);
             ListView lv = (ListView) findViewById(R.id.lectures_created_listView);
             lv.setAdapter(l);

         /*for(i=0;i<lc.size();i++)
         {
                Boolean open=lc.get(i).open;
               String date=lc.get(i).date;
               int nb =lc.get(i).nb;
           }*/
        /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
               public void onItemClck(AdapterView<?> adapterView, View view, int i, long l) {
                  TextView className = (TextView) view.findViewById(R.id.classname);
                  TextView classid = (TextView) view.findViewById(R.id.classid);
                  Intent I=new Intent(m,SpeceficClass.class);
                  I.putExtra("ClassName",className.getText().toString());
                   I.putExtra("ClassId",classid.getText().toString());
                   startActivity(I);
                   Toast.makeText(getApplicationContext(), "ID: " + classid.getText().toString() + ", Name: " + className.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });*/
         }
    }
    private AlertDialog AskOption(int id)
    {
        final int i=id;

        AlertDialog mydeleteDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("sure you want Delete this lecture")


        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        result="";
                        deleteLectureThread dl=new deleteLectureThread(m,i);
                        dl.execute();
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run()
                                    {
                                        if(result.equals("done"))
                                        {
                                            db.deleteLecture(i);
                                            refreshLectures(false);
                                        }
                                        else Toast.makeText(getApplicationContext(),"no connection",Toast.LENGTH_SHORT).show();

                                    }
                                }, 3500);

                        dialog.dismiss();
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();


        return mydeleteDialogBox;

    }
    class LecturesAdapter extends BaseAdapter {

        ArrayList<Lecture> createdLectures = new ArrayList<Lecture>();

        LecturesAdapter(ArrayList<Lecture> createdLectures) {
            this.createdLectures = createdLectures;
        }

        @Override
        public int getCount() {
            return createdLectures.size();
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
            View view1 = layoutInflater.inflate(R.layout.created_lectures,null);

            TextView lNb = (TextView) view1.findViewById(R.id.lectureNb);
            TextView lDate = (TextView) view1.findViewById(R.id.lectureDate);
            Switch s=(Switch) view1.findViewById(R.id.lectureOpen) ;

            lNb.setText("Lecture "+Integer.toString(createdLectures.get(i).nb));
            lDate.setText(createdLectures.get(i).date);
            s.setChecked(createdLectures.get(i).open);
            s.setTag(Integer.valueOf(createdLectures.get(i).id));
            s.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Switch c=(Switch)v;
                    c.setClickable(false);
                    int id=(int)c.getTag();
                    if(!c.isChecked())
                        changeLectureState(id,v,0,0);
                    if(c.isChecked())
                    {
                        AlertDialog diaBox = AskOption1(id,v);
                        Window window = diaBox.getWindow();
                        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        diaBox.setOnCancelListener(new DialogInterface.OnCancelListener()
                        {
                        @Override
                             public void onCancel(DialogInterface dialog)
                            {
                                 if(c.isChecked())c.setChecked(false);
                                else c.setChecked(true);
                                c.setClickable(true);
                            }
                        });
                        diaBox.show();
                    }
                }
            });
            view1.setOnLongClickListener(new AdapterView.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v)
                {
                    Switch s=(Switch) v.findViewById(R.id.lectureOpen);
                    int id=(int)s.getTag();
                    AlertDialog diaBox = AskOption(id);

                    diaBox.show();
                    return false;
                }
            });
            return view1;
        }
    }

    private AlertDialog AskOption1(int id,View v)
    {
        final int i=id;
        final Switch a=(Switch) v;
       // final int distance=d;
        final Spinner input =new Spinner(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT ,LinearLayout.LayoutParams.WRAP_CONTENT);
        String array_spinner[]=new String[5];
        array_spinner[0]="2 metrr";
        array_spinner[1]="5 metre";
        array_spinner[2]="10 metre";
        array_spinner[3]="20 metre";
        array_spinner[4]="40 metre";
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        input.setAdapter(adapter);
        input.setLayoutParams(lp);
        AlertDialog myopenDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("open lecture")
                .setMessage("choose distance")
                .setPositiveButton("next", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int whichButton) {
                        String s = input.getSelectedItem().toString().split(" ")[0];
                        int distance = Integer.valueOf(s);
                        AlertDialog diaBox = AskOption2(i,a,distance);
                        Window window = diaBox.getWindow();
                        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        diaBox.setOnCancelListener(new DialogInterface.OnCancelListener()
                        {
                            @Override
                            public void onCancel(DialogInterface dialog)
                            {
                                if(a.isChecked())a.setChecked(false);
                                else a.setChecked(true);
                                a.setClickable(true);
                            }
                        });
                        diaBox.show();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        a.setClickable(true);
                        if(a.isChecked())a.setChecked(false);
                        else a.setChecked(true);
                        dialog.dismiss();


                    }
                })

                .create();

        myopenDialogBox.setView(input);
        return myopenDialogBox;

    }
    private AlertDialog AskOption2(int id,View v,int d)
    {
        final int i=id;
        final Switch a=(Switch) v;
        final int distance=d;
        final Spinner input =new Spinner(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT ,LinearLayout.LayoutParams.WRAP_CONTENT);
        String array_spinner[]=new String[5];
        array_spinner[0]="2 minutes";
        array_spinner[1]="4 minutes";
        array_spinner[2]="8 minutes";
        array_spinner[3]="15 minutes";
        array_spinner[4]="30 minutes";
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        input.setAdapter(adapter);
        input.setLayoutParams(lp);
        AlertDialog myopenDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("open lecture")
                .setMessage("sure you want open this lecture")
                .setPositiveButton("open", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int whichButton) {

                        String s = input.getSelectedItem().toString().split(" ")[0];
                        //String[] d = s.split(" ");
                        int m = Integer.valueOf(s);

                        changeLectureState(i, a, m,distance);

                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        a.setClickable(true);
                        if(a.isChecked())a.setChecked(false);
                        else a.setChecked(true);
                        dialog.dismiss();


                    }
                })

                .create();

        myopenDialogBox.setView(input);
        return myopenDialogBox;

    }
    public void changeLectureState(int id,View v,int time,int d)
    {
         result="";
         final int distance=d;
         final int i=id;
         final Switch s=(Switch)v;
         final int minute=time;
         String open="false";
         if(s.isChecked())
         {
             open = "true";
         }
         final ProgressDialog progressDialog = new ProgressDialog(prof_lectures.this, R.style.AppTheme_Dark_Dialog);
         progressDialog.setIndeterminate(true);
         progressDialog.setMessage("changing lecture state ...");
         progressDialog.show();
         OpenCloseLectureThread O=new OpenCloseLectureThread(this,i,open,time,loc);
         O.execute();
         new android.os.Handler().postDelayed(
                 new Runnable()
                 {
                     public void run()
                     {
                         if(result.equals("done"))
                         {
                                        if(s.isChecked())
                                        {
                                            db.setlecture("true", i);
                                            closingAfterTimeThread cAt=new closingAfterTimeThread(minute,db,i);
                                            cAt.start();
                                        }
                                        else db.setlecture("false",i);
                         }
                         else
                         {
                             if(s.isChecked())
                                 s.setChecked(false);
                             else
                                 s.setChecked(true);
                             Toast.makeText(getApplicationContext(),"no connection", Toast.LENGTH_SHORT).show();
                         }
                         progressDialog.dismiss();
                         s.setClickable(true);
                     }
                 }, 3000);
        Toast.makeText(getApplicationContext(), loc, Toast.LENGTH_LONG).show();
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
