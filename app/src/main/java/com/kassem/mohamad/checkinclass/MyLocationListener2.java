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

/**
 * Created by ALAA on 15/3/2017.
 */

public class MyLocationListener2 implements LocationListener {

    SpeceficStudentClass a;
    public MyLocationListener2(SpeceficStudentClass a)
    {
        this.a=a;
    }
    @Override
    public void onLocationChanged(Location location) {
        //a.loc=location.toString();
        a.loc=""+location.getAltitude()+"//"+location.getLongitude();

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

