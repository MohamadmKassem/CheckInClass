package com.kassem.mohamad.checkinclass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;

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


    ClassesAdapter classesAdapter;
    ArrayList<Class> createdClasses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m=this;
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
                    }
                    isFirstOpenStudentTab = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){

                }
                else if(position == 1){
                    if(isFirstOpenProftTab){
                        refreshProfData();
                    }
                    isFirstOpenProftTab = false;
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

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

               try{
                   String id= editText.getText().toString();

                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Search a class ...");
                progressDialog.show();

                result="";
                AddClassThread a=new AddClassThread(m,id);
                a.execute();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run()
                            {
                                editText.setText(result);
                                progressDialog.dismiss();
                            }
                        }, 5000);
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
                        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Creating ...");
                        progressDialog.show();

                        result = "";
                        final boolean[] error = {false};
                        CreateClassThread a = new CreateClassThread(m, email, nametocreate);
                        a.execute();
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        if(result.equals("failure:0")) {
                                            Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            linearLayout.removeAllViews();
                                            try {
                                                FileInputStream inputStream = openFileInput("profClass");
                                                int i;
                                                String data = "";
                                                while ((i = inputStream.read()) != -1) {
                                                    data += String.valueOf((char) i);
                                                }
                                                data += "--#--" + nametocreate + "--#--" + result.split(":")[1];
                                                inputStream.close();

                                                FileOutputStream outputStream;
                                                String filename = "profClass";
                                                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                                outputStream.write(data.getBytes());
                                                outputStream.close();
                                            }
                                            catch(Exception e) {
                                                FileOutputStream outputStream;
                                                String filename="profClass";
                                                try {
                                                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                                    outputStream.write(new String(nametocreate + "--#--" + result.split(":")[1]).getBytes());
                                                    outputStream.close();
                                                }
                                                catch (IOException e1){
                                                    error[0] = true;
                                                }

                                            }
                                            if(!error[0]){
                                                createdClasses.add(new Class(nametocreate, result.split(":")[1]));
                                                classesAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        progressDialog.dismiss();
                                    }
                                }, 5000);
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

    private void refreshProfData() {
        if(loadClassesArray()) {
            classesAdapter = new ClassesAdapter(createdClasses);
            ListView classesListView = (ListView) findViewById(R.id.classes_created_listView);
            classesListView.setAdapter(classesAdapter);
            classesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView className = (TextView) view.findViewById(R.id.classname);
                    TextView classid = (TextView) view.findViewById(R.id.classid);
                    Toast.makeText(getApplicationContext(),"ID: " + classid.getText().toString()+", Name: "+className.getText().toString(),Toast.LENGTH_SHORT).show();

                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),"Load Data is Failure",Toast.LENGTH_SHORT).show();
        }
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
            return view1;
        }
    }


}
