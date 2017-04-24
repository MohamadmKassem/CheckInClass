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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class request_lectures extends AppCompatActivity {

    public String classid;
    public ArrayList<Student> AlS;
    public String result;
    public request_lectures m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_lectures);
        Intent I=getIntent();
        classid=I.getStringExtra("ClassId");
        AlS =new ArrayList<Student>();
        GetData();
        m=this;
        //RefreshData();
    }
    public void GetData()
    {
        result="";
        final GetRequestThread GRT=new GetRequestThread(this,classid);
        GRT.execute();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run()
                    {
                        //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                        if(result.equals("done:0"))
                         Toast.makeText(getApplicationContext(),"no request found",Toast.LENGTH_SHORT).show();
                        else if(result.equals("no connection"))
                            Toast.makeText(getApplicationContext(),"no connection",Toast.LENGTH_SHORT).show();
                        else RefreshData();
                        GRT.cancel(true);
                    }
                }, 2500);
    }
    public void RefreshData()
    {
        RequestAdapter l = new RequestAdapter(AlS);
        ListView lv = (ListView) findViewById(R.id.Request_listView);
        lv.setAdapter(l);
    }
    class RequestAdapter extends BaseAdapter {

        ArrayList<Student> REQ = new ArrayList<Student>();

        RequestAdapter(ArrayList<Student> req) {
            this.REQ = req;
        }

        @Override
        public int getCount() {
            return REQ.size();
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
            View view1 = layoutInflater.inflate(R.layout.request_layout,null);

            TextView fullname = (TextView) view1.findViewById(R.id.fullname);
            TextView emailStudent = (TextView) view1.findViewById(R.id.email_student);
            Button bt1=(Button)view1.findViewById(R.id.delete);
            Button bt2=(Button)view1.findViewById(R.id.accept);

            fullname.setText(REQ.get(i).fullname);
            emailStudent.setText(REQ.get(i).email);

            bt1.setTag(REQ.get(i).email);
            bt2.setTag(REQ.get(i).email);
            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button Btn1=(Button)v;
                    final String email=(String)Btn1.getTag();
                    final answerToReqThread a=new answerToReqThread(m);
                    a.execute("DeleteReq--#--"+classid+"--#--"+email);
                    result="";
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run()
                                {
                                    if(result.equals("done"))
                                    {
                                        int i=0;
                                        for(i=0;i<AlS.size();i++)
                                        {
                                            if((AlS.get(i).email).equals(email))
                                            {
                                                AlS.remove(i);
                                                break;
                                            }
                                        }
                                        RefreshData();
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
                                    a.cancel(true);
                                }
                            }, 2500);
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button Btn1=(Button)v;
                    final String email=(String)Btn1.getTag();
                    final answerToReqThread a=new answerToReqThread(m);
                    a.execute("AcceptReq--#--"+classid+"--#--"+email);
                    result="";
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run()
                                {
                                    if(result.equals("done"))
                                    {
                                        int i=0;
                                        for(i=0;i<AlS.size();i++)
                                        {
                                            if((AlS.get(i).email).equals(email))
                                            {
                                                AlS.remove(i);
                                                break;
                                            }
                                        }
                                        RefreshData();
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(),"no connection",Toast.LENGTH_SHORT).show();
                                    a.cancel(true);
                                }
                            }, 2500);
                }
            });
            return view1;
        }
    }
}
