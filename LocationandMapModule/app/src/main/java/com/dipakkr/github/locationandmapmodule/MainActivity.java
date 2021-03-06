package com.dipakkr.github.locationandmapmodule;

import android.Manifest;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int MY_LOCATION_SERVICE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast.makeText(this, "Details are here", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_LOCATION_SERVICE);
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            updateUI(location);
                        }
                    }
                });

    }


    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case MY_LOCATION_SERVICE:
                if (grantResults.length > 0 && grantResults[0] > PackageManager.PERMISSION_GRANTED) {
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                    fusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        updateUI(location);
                                    }
                                }
                            });
                }
        }
    }*/

    private void updateUI(Location location) {
        TextView lat = (TextView) findViewById(R.id.latitude);
        TextView longi = (TextView) findViewById(R.id.longitude);
        TextView alti = (TextView) findViewById(R.id.altitude);


        lat.setText(String.valueOf(location.getLatitude()));
        longi.setText(String.valueOf(location.getLongitude()));


        String city = null;
        String a1=null,a2 = null, a3 = null;
        Geocoder gcd = new Geocoder(this, Locale.getDefault());

        List<Address> address;

        try{
            address = gcd.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(address.size() > 0){
                city = address.get(0).getLocality();
                a1 = address.get(0).getSubLocality();
                a2 = address.get(0).getAddressLine(0);
                a3 = address.get(0).getPostalCode();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        String addr = a2 + "," + city ;
        alti.setText(addr);


    }

}