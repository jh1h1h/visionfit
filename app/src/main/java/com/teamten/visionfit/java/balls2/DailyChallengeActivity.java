package com.teamten.visionfit.java.balls2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamten.visionfit.R;

public class DailyChallengeActivity extends AppCompatActivity{
    public int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_challenge);
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