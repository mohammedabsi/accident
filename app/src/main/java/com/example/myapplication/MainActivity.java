package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    private int clickedNavItem = 0;
    LinearLayout container;
    ImageView   userAvatarIv;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String userEmail = firebaseAuth.getCurrentUser().getEmail();
    NavigationView navigation_view ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        container = findViewById(R.id.container);



        navigation_view = findViewById(R.id.navigation_view);

        View hView =  navigation_view.inflateHeaderView(R.layout.menu_layout);
        userAvatarIv = hView.findViewById(R.id.userAvatarIv);






        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainFragment()).commit();
            Log.d("TAG", "onCreate: " + getSupportFragmentManager().getFragments().contains(new MainFragment()));
        }


        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        NavigationView navigation_view = findViewById(R.id.navigation_view);
        ConstraintLayout content = findViewById(R.id.content);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);

            }
        };
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final Snackbar snackbar = Snackbar
                        .make(drawerLayout, "Coming On next Version", Snackbar.LENGTH_SHORT).setTextColor(getResources().getColor(R.color.primary_color)).setBackgroundTint(getResources().getColor(R.color.secondry_color));
                switch (item.getItemId()) {
                    case R.id.home:
                        clickedNavItem = R.id.home;
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainFragment()).commit();
                        break;
                    case R.id.nav_profile:
                        clickedNavItem = R.id.nav_profile;
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
                        break;
//                    case R.id.nav_settings:
//                        snackbar.show();
//                        clickedNavItem = R.id.nav_settings;
//                        break;

                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        finish();
                        clickedNavItem = R.id.nav_logout;
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

                switch (clickedNavItem) {
                    case R.id.home:

                        break;
                    case R.id.nav_profile:

                        break;

                    case R.id.nav_logout:

                        break;


                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }


    public void ReturnHome(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainFragment()).commit();

    }



    private void generateQR() {



            firebaseFirestore.collection("User").document(userEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        String myText = task.getResult().getString("email");

                        //initializing MultiFormatWriter for QR code
                        MultiFormatWriter mWriter = new MultiFormatWriter();

                        try {
                            //BitMatrix class to encode entered text and set Width &amp; Height
                            BitMatrix mMatrix = mWriter.encode(myText, BarcodeFormat.QR_CODE, 350, 350);

                            BarcodeEncoder mEncoder = new BarcodeEncoder();
                            Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
                            userAvatarIv.setImageBitmap(mBitmap);//Setting generated QR code to imageView


                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });




    }

    @Override
    protected void onStart() {
        super.onStart();
        generateQR();

    }
}