package Team10_VisionFit;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.teamten.visionfit.R;

import Team10_VisionFit.Backend.firebaseAuthentication.Login;
import Team10_VisionFit.PoseDetector.LivePreviewActivity;
import Team10_VisionFit.UI.DailyChallengeActivity;
import Team10_VisionFit.UI.LeaderBoardActivity;
import Team10_VisionFit.UI.MyRewardsActivity;
import Team10_VisionFit.UI.MyStatisticsActivity;
import Team10_VisionFit.UI.ProfileActivity;
import Team10_VisionFit.UI.SettingsActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FIREBASE STUFF
        auth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();


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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    Log.d("Button Check", "Home Button Clicked");
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_settings:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    Log.d("Button Check", "Settings Button Clicked");
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.cameraButton:
                    startActivity(new Intent(getApplicationContext(), LivePreviewActivity.class));
                    Log.d("Button Check", "Camera Button Clicked");
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_userProfile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    Log.d("Button Check", "Profile Button Clicked");
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_logout:
                    Log.d("Button Check", "Logout Button Clicked");
                    customExitDialog();

//                    Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_SHORT).show();
//                    FirebaseAuth.getInstance().signOut();
//                    Intent intent = new Intent(getApplicationContext(), Login.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                    SharedPreferences sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);
//                    sharedPreferences.edit().clear().commit();
//                    finish();
                    return true;
            }
            return false;
        });

        // CAMERA BUTTON
//        Button cameraButton = (Button) findViewById(R.id.cameraButton);
//        cameraButton.setOnClickListener(this);


    }

    // FUNCTIONS FOR THE BUTTONS

    public  void customExitDialog()
    {
        // creating custom dialog
        final Dialog dialog = new Dialog(MainActivity.this);

        // setting content view to dialog
        dialog.setContentView(R.layout.logout_dialog_box);

        // getting reference of TextView
        TextView dialogButtonYes = (TextView) dialog.findViewById(R.id.textViewYes);
        TextView dialogButtonNo = (TextView) dialog.findViewById(R.id.textViewNo);

        // click listener for No
        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismiss the dialog
                dialog.dismiss();

            }
        });
        // click listener for Yes
        dialogButtonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismiss the dialog and exit the exit
                dialog.dismiss();

                Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                SharedPreferences sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                finish();
                finish();

            }
        });

        // show the exit dialog
        dialog.show();
    }

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
            Log.d("Button Check", "Daily Challenge Clicked Successfully");
            openDailyChallengeActivity();
        } else if (v.getId() == R.id.rewardsButton) {
            Log.d("Button Check", "Rewards Clicked Successfully");
            openMyRewards();
        } else if (v.getId() == R.id.leaderboardButton) {
            Log.d("Button Check", "Leaderboard Clicked Successfully");
            openLeaderboard();
        } else if (v.getId() == R.id.mystatsButton) {
            Log.d("Button Check", "Stats Clicked Successfully");
            openMyStats();
//        } else if (v.getId() == R.id.cameraButton) {
//            Log.d("Button Check", "Clicked Successfully");
//            openCamera();

        } else if (v.getId() == R.id.layout) {
            Toast.makeText(MainActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }
    }
}