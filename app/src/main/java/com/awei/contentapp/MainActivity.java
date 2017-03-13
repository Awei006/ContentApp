package com.awei.contentapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.TextView;

import java.util.List;

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
        ContentResolver resolver = getContentResolver();
        String[] projection = {Contacts._ID,Contacts.DISPLAY_NAME,Phone.NUMBER};
        Cursor cursor = resolver.query(Contacts.CONTENT_URI,
                null,null,null,null);
        /*while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Log.d("RECORD",id+"/"+name);
        }*/

        SimpleCursorAdapter adap = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME,Contacts.HAS_PHONE_NUMBER},
                new int[]{android.R.id.text1,android.R.id.text2},
                1
        ){
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                super.bindView(view, context, cursor);
                TextView phone = (TextView) view.findViewById(android.R.id.text2);
                if(cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))==0){
                    phone.setText("");
                }
                else{
                    int id = cursor.getInt(cursor.getColumnIndex(Contacts._ID));
                    Cursor pCursor = getContentResolver().query(
                            Phone.CONTENT_URI,
                            null,
                            Phone.CONTACT_ID + "=?",
                            new String[]{String.valueOf(id)},
                            null
                    );
                    if(pCursor.moveToFirst()){
                        String number = pCursor.getString(pCursor.getColumnIndex(Phone.DATA));
                        phone.setText(number);
                    }
                }
            }
        };
        ListView list = (ListView)findViewById(R.id.lists);
        list.setAdapter(adap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CONTACTS:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readContacts();
                }
                else{
                    new AlertDialog.Builder(this)
                            .setMessage("必須取得聯絡人權限才能顯示資料")
                            .setPositiveButton("OK",null)
                            .show();
                }
                return;
        }


    }
}
