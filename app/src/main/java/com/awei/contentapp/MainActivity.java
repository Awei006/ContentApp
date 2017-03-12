package com.awei.contentapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import static android.Manifest.permission.*;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permission = ActivityCompat.checkSelfPermission(this, READ_CONTACTS);

        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{READ_CONTACTS,WRITE_CONTACTS},REQUEST_CONTACTS);
        }else{
            readContacts();
        }
    }

    private void readContacts() {
                
    }
}
