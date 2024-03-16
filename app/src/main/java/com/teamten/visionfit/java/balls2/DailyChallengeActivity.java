package com.teamten.visionfit.java.balls2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamten.visionfit.R;
import com.teamten.visionfit.java.LivePreviewActivity;

public class DailyChallengeActivity extends AppCompatActivity{
    public int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_challenge);

        Button pushUpButton = findViewById(R.id.pushUpButton);
        Button squatsButton = findViewById(R.id.squatsButton);

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





        Button startButton = (Button) findViewById(R.id.startButton);
        TextView timer = (TextView) findViewById(R.id.timer);
        startButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                new CountDownTimer(3000, 1000){
                    public void onTick(long millisUntilFinished){

                        timer.setText(String.valueOf(counter));
                        counter--;
                    }
                    public  void onFinish(){
                        timer.setText("FINISH!!");
                    }
                }.start();
            }
        });
    }
}
