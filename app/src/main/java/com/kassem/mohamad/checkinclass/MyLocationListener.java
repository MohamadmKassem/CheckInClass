package com.kassem.mohamad.checkinclass;

/**
 * Created by ALAA on 18/4/2017.
 */

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by ALAA on 15/3/2017.
 */

public class MyLocationListener implements LocationListener {

    prof_lectures a;
    public MyLocationListener(prof_lectures a)
    {
        this.a=a;
    }
    @Override
    public void onLocationChanged(Location location) {
        //a.loc=location.toString();
       // location.dis
        a.loc=""+location.getLatitude()+"//"+location.getLongitude();
        //Toast.makeText(a,""+location.getLatitude()+"//"+location.getLongitude(),Toast.LENGTH_SHORT).show();

        //a.loc+=location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        a.finish();

    }
}

