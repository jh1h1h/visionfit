package com.teamten.visionfit.java.balls2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.teamten.visionfit.R;
import com.teamten.visionfit.java.LivePreviewActivity;

public class DailyChallengeActivity extends AppCompatActivity{
    public int counter = 3;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_challenge);

        Button pushUpButton = findViewById(R.id.pushUpButton);
        Button squatsButton = findViewById(R.id.squatsButton);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_settings:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_userProfile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_logout:
                    Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), com.firebaseAuthentication.Login.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    SharedPreferences sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);
                    sharedPreferences.edit().clear().commit();
                    finish();
                    return true;
            }
            return false;
        });

        //Setup on click listeners
        pushUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyChallengeActivity.this, LivePreviewActivity.class);
                intent.putExtra("ClassType", "Push Ups");
                startActivity(intent);
            }
        });

        squatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyChallengeActivity.this, LivePreviewActivity.class);
                intent.putExtra("ClassType", "Squats");
                startActivity(intent);
            }
        });





        //Button startButton = (Button) findViewById(R.id.startButton);
        //TextView timer = (TextView) findViewById(R.id.timer);
        //startButton.setOnClickListener(new View.OnClickListener()
        //{
            //@Override
            //public void onClick(View v) {
                //new CountDownTimer(3000, 1000){
                    //public void onTick(long millisUntilFinished){

                        //timer.setText(String.valueOf(counter));
                        //counter--;
                    //}
                    //public  void onFinish(){
                        //timer.setText("FINISH!!");
                    //}
                //}.start();
            //}
        //});
    }
}