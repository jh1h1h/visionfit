package com.teamten.visionfit.java.balls2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebaseAuthentication.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.teamten.visionfit.EntryChoiceActivity;
import com.teamten.visionfit.R;
import com.teamten.visionfit.java.ChooserActivity;
import com.teamten.visionfit.java.LivePreviewActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DAILY CHALLENGE BUTTON
        Button dailychallengeButton = (Button) findViewById(R.id.dailychallengeButton);
        dailychallengeButton.setOnClickListener(this);

        // MY REWARDS BUTTON
        Button rewardsButton = (Button) findViewById(R.id.rewardsButton);
        rewardsButton.setOnClickListener(this);

        // LEADERBOARD BUTTON
        Button leaderboardButton = (Button) findViewById(R.id.leaderboardButton);
        leaderboardButton.setOnClickListener(this);

        // MY STATISTICS BUTTON
        Button mystatsButton = (Button) findViewById(R.id.mystatsButton);
        mystatsButton.setOnClickListener(this);

        // LOGOUT BUTTON
        Button logoutButton = (Button) findViewById(R.id.logout);
        logoutButton.setOnClickListener(this);

        // CAMERA BUTTON
        Button cameraButton = (Button) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(this);


    }

    // FUNCTIONS FOR THE BUTTONS
    public void openDailyChallengeActivity() {
        Intent intent = new Intent(this, DailyChallengeActivity.class);
        startActivity(intent);
    }


    public void openMyRewards() {
        Intent intent = new Intent(this, MyRewardsActivity.class);
        startActivity(intent);
    }

    public void openLeaderboard() {
        Intent intent = new Intent(this, LeaderBoardActivity.class);
        startActivity(intent);
    }

    public void openMyStats() {
        Intent intent = new Intent(this, MyStatisticsActivity.class);
        startActivity(intent);
    }

    public void openCamera() {
        //Intent intent = new Intent(this, LivePreviewActivity.class);
        Intent intent = new Intent(this, LivePreviewActivity.class);
        intent.putExtra("ClassType", "Free Style");
        startActivity(intent);
    }


    // LOGIC CHECKS FOR WHAT BUTTON IS PRESSED
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.dailychallengeButton) {
            Log.d("Button Check", "Clicked Successfully");
            openDailyChallengeActivity();
        } else if (v.getId() == R.id.rewardsButton) {
            Log.d("Button Check", "Clicked Successfully");
            openMyRewards();
        } else if (v.getId() == R.id.leaderboardButton) {
            Log.d("Button Check", "Clicked Successfully");
            openLeaderboard();
        } else if (v.getId() == R.id.mystatsButton) {
            Log.d("Button Check", "Clicked Successfully");
            openMyStats();
        } else if (v.getId() == R.id.cameraButton) {
            Log.d("Button Check", "Clicked Successfully");
            openCamera();

        } else if (v.getId() == R.id.logout) {
            Toast.makeText(MainActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, com.firebaseAuthentication.Login.class);
            startActivity(intent);
            finish();
        }
    }
}


