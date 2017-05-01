package com.kassem.mohamad.checkinclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Size;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Student_of_lectures extends AppCompatActivity {

    String result;
    int classid;
    Student_of_lectures m;
    DatabaseHandler db;
    ArrayList<Student> students;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_of_lectures);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i=getIntent();
        classid=Integer.valueOf(i.getStringExtra("ClassId"));
        m=this;
        result="";
        db=new DatabaseHandler(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinearLayout LL=(LinearLayout)findViewById(R.id.addStudent);
                LL.removeAllViews();

                final EditText email=new EditText(m);
                email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                email.setHint("email");
                final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
               // email.setWidth();
                email.setLayoutParams(lparams);
                LL.addView(email);

                final EditText pass=new EditText(m);
                pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                pass.setHint("password");
                final ViewGroup.LayoutParams lparams2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                pass.setLayoutParams(lparams2);
                LL.addView(pass);

                Button b=new Button(m);
                b.setText("Add");
                b.setGravity(Gravity.CENTER);
                b.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result="";
                        addStudentThread ast=new addStudentThread(m,email.getText().toString(),pass.getText().toString(),classid);
                        ast.execute();
                    }
                });
                LL.addView(b);
            }
        });

        getDataFromSqlLite();
    }
    public void finishAddStudent()
    {
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            LinearLayout l=(LinearLayout) findViewById(R.id.addStudent);
            l.removeAllViews();
            getDataFromSqlLite();
    }
    public void getDataFromSqlLite()
    {
        students=db.getStudents(classid);
        if(students.size()==0)
        {
            UpdateRegistreThread2 u = new UpdateRegistreThread2(m, classid);
            u.execute();
        }
        else refreshPageData();
    }
    public void refreshPageData()
    {
        RegistreAdapter l = new RegistreAdapter(students);
        ListView lv = (ListView) findViewById(R.id.StudentsOfClasslist);
        lv.setAdapter(l);
    }
    class RegistreAdapter extends BaseAdapter {

        ArrayList<Student> s = new ArrayList<Student>();

        RegistreAdapter(ArrayList<Student> s) {
            this.s =s;
        }

        @Override
        public int getCount() {
            return s.size();
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
            View view1 = layoutInflater.inflate(R.layout.registre,null);

            TextView fullname = (TextView) view1.findViewById(R.id.registreFullName);
            TextView email = (TextView) view1.findViewById(R.id.registreEmail);
            //ImageView IV=(ImageView)view1.findViewById(R.id.img2);
            fullname.setText(s.get(i).fullname);
            // Toast.makeText(getApplicationContext(),presence.get(i).fullname,Toast.LENGTH_SHORT).show();
            email.setText(s.get(i).email);
            return view1;
        }
    }
}
