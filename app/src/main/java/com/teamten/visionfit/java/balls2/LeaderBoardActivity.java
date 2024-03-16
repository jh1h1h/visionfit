package com.teamten.visionfit.java.balls2;

import android.os.Bundle;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.teamten.visionfit.R;

public class LeaderBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        /*
         TextView tv=findViewById(R.id.leaderboardTitle);
        YoYo.with(Techniques.SlideInRight).duration(2000).playOn(tv);
        */
    }
}