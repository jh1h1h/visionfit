package com.example.visionfitv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
        }


    // FUNCTIONS FOR THE BUTTONS
    public void openDailyChallengeActivity(){
        Intent intent = new Intent(this, DailyChallangeActivity.class);
        startActivity(intent);}

    public void openMyRewards(){
        Intent intent = new Intent(this, MyRewardsActivity.class);
        startActivity(intent);}

    public void openLeaderboard(){
        Intent intent = new Intent(this, LeaderBoardActivity.class);
        startActivity(intent);}

    public void openMyStats(){
        Intent intent = new Intent(this, MyStatisticsActivity.class);
        startActivity(intent);}

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
                openMyStats();}

            }
        }