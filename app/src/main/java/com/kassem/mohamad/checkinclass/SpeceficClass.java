package com.kassem.mohamad.checkinclass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

public class SpeceficClass extends AppCompatActivity {

    String className;
    String classId;
    TextView T1;
    TextView T2;
    TextView T3;
    SpeceficClass S;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specefic_class);

        Intent I=getIntent();

        className=I.getStringExtra("ClassName");
        classId=I.getStringExtra("ClassId");
        Toast.makeText(getApplicationContext(), "ID: " + classId+ ", Name: " + className, Toast.LENGTH_SHORT).show();

        T1=(TextView) findViewById(R.id.lecturesOfClass);
        T2=(TextView) findViewById(R.id.StudentsOfClass);
        T3=(TextView) findViewById(R.id.StudentsRequest);
        S=this;
        T1.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I=new Intent(S,prof_lectures.class);
                I.putExtra("ClassName",className);
                I.putExtra("ClassId",classId);
                startActivity(I);
                //Toast.makeText(getApplicationContext(), "ID: " + classid.getText().toString() + ", Name: " + className.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });
        T2.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I=new Intent(S,Student_of_lectures.class);

                I.putExtra("ClassName",className);
                I.putExtra("ClassId",classId);
                startActivity(I);
                //Toast.makeText(getApplicationContext(), "ID: " + classid.getText().toString() + ", Name: " + className.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });
        T3.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I=new Intent(S,request_lectures.class);
                I.putExtra("ClassName",className);
                I.putExtra("ClassId",classId);
                startActivity(I);
                //Toast.makeText(getApplicationContext(), "ID: " + classid.getText().toString() + ", Name: " + className.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
