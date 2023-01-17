package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class AccidentActivity extends AppCompatActivity {
//    ImageView backBtnAccident;
//    FrameLayout accident_container ;
//
//    String[] permissions = {
//            Manifest.permission.CAMERA
//    };
//    int PERM_CODE = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident);

//        backBtnAccident = findViewById(R.id.backBtnAccident);
//        accident_container = findViewById(R.id.accident_container);
//
//        String sessionId = getIntent().getStringExtra("accident");
//        if (sessionId.equalsIgnoreCase("1")){
//            getSupportFragmentManager().beginTransaction().replace(R.id.accident_container, new ChatFragment()).commit();
//
//        }else {
//            getSupportFragmentManager().beginTransaction().replace(R.id.accident_container, new AddAccidentFragment()).commit();
//
//        }
    }

//    public void ReturnHome(View view) {
//        startActivity(new Intent(this , MainActivity.class));
//        finish();
//
//    }
//
//    private boolean checkpermissions(){
//        List<String> listofpermisssions = new ArrayList<>();
//        for (String perm: permissions){
//            if (ContextCompat.checkSelfPermission(getApplicationContext()  , perm) != PackageManager.PERMISSION_GRANTED){
//                listofpermisssions.add(perm);
//            }
//        }
//        if (!listofpermisssions.isEmpty()){
//            ActivityCompat.requestPermissions(this, listofpermisssions.toArray(new String[listofpermisssions.size()]), PERM_CODE);
//            return false;
//        }
//        return true;
//    }
}
