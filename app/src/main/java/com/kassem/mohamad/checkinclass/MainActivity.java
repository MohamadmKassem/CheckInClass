package com.kassem.mohamad.checkinclass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public static int request_code = 1;
    public String result;
    private String email;
    public MainActivity m;
    String nametocreate;
    private boolean isFirstOpenStudentTab = true;
    private boolean isFirstOpenProftTab = true;
    ProgressDialog progressDialog;
    ClassesAdapter classesAdapter;
    ArrayList<Class> createdClasses;
    AddClassThread a;
    DatabaseHandler db;
    SendReqThread S;
    CreateClassThread Ca;
    GetClassesDataThread gd;
    ArrayList<Class> lc2;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m=this;

        db  = new DatabaseHandler(this);

        // Toast.makeText(getApplicationContext(),db.getClass(1).getId()+","+db.getClass(1).getName()+","+db.getClass(1).getEmail()+","+db.getClass(1).getLocation(),Toast.LENGTH_SHORT).show();

        createdClasses = new ArrayList<Class>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(isFirstOpenStudentTab){
                    if(position == 0){
                        refreshStudentTab();
                        m.position=0;
                    }
                    isFirstOpenStudentTab = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    refreshStudentTab();
                    m.position=0;
                }
                else if(position == 1){
                    if(isFirstOpenProftTab){
                        refreshProfData(false);
                    }
                    isFirstOpenProftTab = false;
                    m.position=1;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    //    .setAction("Action", null).show();


                if(tabLayout.getSelectedTabPosition() == 0){
                    addClass();
                }
                else if(tabLayout.getSelectedTabPosition() == 1){
                    create_class();
                }

            }
        });

        // check if already login
        email = "";
        try{
            FileInputStream inputStream = openFileInput("login");
            int i;
            while((i=inputStream.read()) != -1){
                email += String.valueOf((char)i);
            }
            if(email.equals("")){
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent,request_code);
            }
        }
        catch (Exception ex){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent,request_code);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(),"settings", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_sign_out:
                Toast.makeText(getApplicationContext(),"sign out", Toast.LENGTH_LONG).show();
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void signOut(){
        String loginfile = "login";
        FileOutputStream outputStream;
        try{
            outputStream = openFileOutput(loginfile, Context.MODE_PRIVATE);
            outputStream.write(new String("").getBytes());
            outputStream.close();
            //this.onCreate(null);
            stopService(new Intent(this,MyService.class));
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        catch (Exception ex){
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            // Returning the current tabs
            switch (position){
                case 0:
                StudentTab studentTab = new StudentTab();
                    return studentTab;
                case 1:
                    ProfessorTab professorTab = new ProfessorTab();
                    return  professorTab;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Student";
                case 1:
                    return "Professor";

            }
            return null;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_code) {
            if (resultCode == RESULT_OK) {
                if(data == null){
                    this.finish();
                }
                else {
                    email = data.getData().toString();
                    Intent In=new Intent(this,MyService.class);
                    In.putExtra("email",email);
                    startService(In);
                }
            }
        }
    }

    // Registration to a new class
    private void addClass(){
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.search_class_linear_layout);
        linearLayout.removeAllViews();


        final EditText editText = new EditText(this);
        editText.setHint("Enter The ID");
        editText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayoutCompat.LayoutParams.WRAP_CONTENT,1));

        ImageButton searchButton = new ImageButton(this);
        int idSearchbButton = getResources().getIdentifier("com.kassem.mohamad.checkinclass:drawable/ic_search_black_24dp" , null, null);
        searchButton.setImageResource(idSearchbButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // search for the new class

               try
               {
                    String id= editText.getText().toString();
                    progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Search a class ...");
                    progressDialog.show();
                    result="";
                    a=new AddClassThread(m,id);
                    a.execute();
               }
               catch(Exception e){}
            }
        });

        ImageButton exitButton = new ImageButton(this);
        int idExitImage = getResources().getIdentifier("com.kassem.mohamad.checkinclass:drawable/ic_close_black_24dp" , null, null);
        exitButton.setImageResource(idExitImage);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.removeAllViews();
            }
        });

        linearLayout.addView(editText);
        linearLayout.addView(searchButton);
        linearLayout.addView(exitButton);
    }
    public void finishAddClass()
    {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.search_class_linear_layout);
        if(!result.equals("") && !result.equals("failure:0")){
            linearLayout.removeAllViews();
            LinearLayout newClassAdd  = new LinearLayout(m);
            newClassAdd.setOrientation(LinearLayout.VERTICAL);
            TextView newClass = new TextView(m);
            newClass.setText(result.split(":")[2]);
            TextView prof = new TextView(m);
            prof.setText(result.split(":")[1]);
            newClassAdd.addView(newClass);
            newClassAdd.addView(prof);

            ImageButton register = new ImageButton(m);
            int idSearchbButton = getResources().getIdentifier("com.kassem.mohamad.checkinclass:drawable/ic_add_box_black_24dp" , null, null);
            register.setImageResource(idSearchbButton);
            register.setTag(Integer.valueOf(result.split(":")[3]));
            register.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    ImageButton b=(ImageButton)view;
                    int id=(int)b.getTag();
                    sendrequest(id);

                }
            });

            linearLayout.addView(newClassAdd);
            linearLayout.addView(register);

        }
        else {
            Toast.makeText(getApplicationContext(),"Search class failed",Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
        a.cancel(true);
    }
    private void sendrequest(int id)
    {
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.search_class_linear_layout);
        result="";
        S=new SendReqThread(m,id,email);
        S.execute();
    }
    public void finishsendreq()
    {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.search_class_linear_layout);
        if(result.equals("done"))
        {
            linearLayout.removeAllViews();
            Toast.makeText(getApplicationContext(), "sending", Toast.LENGTH_SHORT).show();
        }
        else
        {
            linearLayout.removeAllViews();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
        S.cancel(true);
    }
    private void create_class() {
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.prof_linear_layout);
        linearLayout.removeAllViews();
        final EditText editText = new EditText(this);
        editText.setHint("Class name");
        editText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayoutCompat.LayoutParams.WRAP_CONTENT,1));
        ImageButton createButton = new ImageButton(this);
        int idButton = getResources().getIdentifier("com.kassem.mohamad.checkinclass:drawable/ic_add_box_black_24dp" , null, null);
        createButton.setImageResource(idButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // search for the new class

                try{
                    nametocreate= editText.getText().toString();
                    if(!nametocreate.equals("")) {
                        progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Creating ...");
                        progressDialog.show();

                        result = "";

                        Ca = new CreateClassThread(m, email, nametocreate);
                        Ca.execute();
                    }
                    else {
                        editText.setError("Enter a class name!");
                    }
                }
                catch(Exception e){}

            }
        });
        ImageButton exitButton = new ImageButton(this);
        int idExitImage = getResources().getIdentifier("com.kassem.mohamad.checkinclass:drawable/ic_close_black_24dp" , null, null);
        exitButton.setImageResource(idExitImage);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.removeAllViews();
            }
        });

        linearLayout.addView(editText);
        linearLayout.addView(createButton);
        linearLayout.addView(exitButton);
    }

    public void finishCreate()
    {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.prof_linear_layout);
        if(result.equals("failure:0")) {
            Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
        }
        else {
            linearLayout.removeAllViews();
            db.addClass(new Class(nametocreate, result.split(":")[1], email, "0"));
            refreshProfData(false);
        }
        Ca.cancel(true);
        progressDialog.dismiss();
    }
    private boolean loadClassesArray(){
        FileInputStream inputStream = null;
        try {
            String s2 = "";
            inputStream = openFileInput("profClass");
            int i;
            while((i=inputStream.read()) != -1){
                s2 += String.valueOf((char)i);
            }
            if(!s2.equals("")) {
                String[] data=s2.split("--#--");
                int len;
                for(len=0; len < data.length; len += 2) {
                    createdClasses.add(new Class(data[len],data[len+1]));
                }
                return true;
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public void refreshProfData(boolean fromThread) {
        //Toast.makeText(getApplicationContext(),"here",Toast.LENGTH_SHORT).show();
        //ArrayList<Class> lc =new ArrayList<Class>();
        ArrayList<Class> lc=db.getAllclass(email);
        if(lc.size()==0 && fromThread==false)
        {
            gd=new GetClassesDataThread(this,null);
            gd.execute(email,"profClasses");
        }
        else{
            classesAdapter = new ClassesAdapter(lc);
            ListView classesListView = (ListView) findViewById(R.id.classes_created_listView);
            classesListView.setAdapter(classesAdapter);
        }

    }
    private AlertDialog AskOption(int id)
    {
        final int classid=id;
        final Date d=new Date();
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("delete class")
                .setMessage("are you sure want to delete this class ")


                .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        DeleteClassThread DL=new DeleteClassThread(m,classid);
                        DL.execute();
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
    public void finishDeleteClass()
    {
        if(result.equals("done"))
            refreshProfData(false);
        else Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
    }
    public void finishGetClasses()
    {
        if(!result.equals("done:0") && !result.equals("failure"))
        {
            refreshProfData(true);
        }
        else if(result.equals("done:0"))
        {
            Toast.makeText(getApplicationContext(),"no classes yet",Toast.LENGTH_SHORT).show();
            refreshProfData(true);
        }
        else Toast.makeText(getApplicationContext(),"no connection",Toast.LENGTH_SHORT).show();
        gd.cancel(true);
    }

    class ClassesAdapter extends BaseAdapter {

        ArrayList<Class> createdClasses = new ArrayList<Class>();

        ClassesAdapter(ArrayList<Class> createdClasses) {
            this.createdClasses = createdClasses;
        }

        @Override
        public int getCount() {
            return createdClasses.size();
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
            View view1 = layoutInflater.inflate(R.layout.created_classes_layout,null);

            TextView nameTextView = (TextView) view1.findViewById(R.id.classname);
            TextView idTextView = (TextView) view1.findViewById(R.id.classid);
            nameTextView.setText(createdClasses.get(i).getName());
            idTextView.setText(createdClasses.get(i).getId());
            if(position==1)
            {
                view1.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        TextView classid = (TextView) v.findViewById(R.id.classid);
                        int CI=Integer.valueOf(classid.getText().toString());
                        AlertDialog diaBox = AskOption(CI);
                        diaBox.show();
                        return false;
                    }
                });
                view1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView className = (TextView) v.findViewById(R.id.classname);
                        TextView classid = (TextView) v.findViewById(R.id.classid);
                        Intent I=new Intent(m,SpeceficClass.class);
                        I.putExtra("ClassName",className.getText().toString());
                        I.putExtra("ClassId",classid.getText().toString());
                        startActivity(I);
                    }
                });
            }
            return view1;
        }
    }
    public void refreshStudentTab() {
        //Toast.makeText(getApplicationContext(),"here",Toast.LENGTH_SHORT).show();
            //final ArrayList<Class> lc2 =new ArrayList<Class>();
            result="";
            lc2=new ArrayList<Class>();
            gd=new GetClassesDataThread(this,lc2);
            gd.execute(email,"studentClasses");
        }
    public void finishStudentClasses()
    {
        if(!result.equals("failure"))
        {
            if(result.equals("done:0"))
                Toast.makeText(getApplicationContext(),"no classes yet",Toast.LENGTH_SHORT).show();
                classesAdapter = new ClassesAdapter(lc2);
                ListView classesListView = (ListView) findViewById(R.id.Student_class_linear_layout);
                classesListView.setAdapter(classesAdapter);
                classesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView className = (TextView) view.findViewById(R.id.classname);
                        TextView classid = (TextView) view.findViewById(R.id.classid);
                        Intent I = new Intent(m, SpeceficStudentClass.class);
                        I.putExtra("email", email);
                        I.putExtra("ClassId", classid.getText().toString());
                        startActivity(I);
                        //Toast.makeText(getApplicationContext(),"!!!",Toast.LENGTH_SHORT).show();

                    }
                });

        }
        else Toast.makeText(getApplicationContext(),"no connection",Toast.LENGTH_SHORT).show();
        gd.cancel(true);
    }
}




