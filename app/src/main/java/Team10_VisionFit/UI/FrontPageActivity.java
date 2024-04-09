package Team10_VisionFit.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teamten.visionfit.R;

import java.util.ArrayList;

import Team10_VisionFit.Backend.firebaseAuthentication.Login;
import Team10_VisionFit.PoseDetector.LivePreviewActivity;

public class FrontPageActivity extends BaseActivity implements View.OnClickListener {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    int streakCount;

    public static int repCount;
    public static String classType;

    private static final int PERMISSION_REQUESTS = 1; //Its just a request code, arbitrary
    private static final String[] REQUIRED_RUNTIME_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        auth = FirebaseAuth.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //Get the current logged in User's ID
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(uid); //Using that ID, get the user's data from firestore

//        repCount = getIntent().getIntExtra("repCount",0);
//        classType = getIntent().getStringExtra("ClassType");

        textView = findViewById(R.id.user_details);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    String displayName = document.getString("username");
                    streakCount = document.getLong("streak").intValue();

                    final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                    final TextView streakText = (TextView) findViewById(R.id.streakText);

                    progressBar.setProgress(Integer.parseInt(String.valueOf(streakCount)));
                    String streak = streakCount + " days";
                    streakText.setText(streak);

                    textView.setText("Hello, "+displayName+"!");}

//                if (repCount != 0 && classType != null && !classType.equals("Free Style")) {
//                    if (classType.equals("Push Ups")) {
//                        classType = "pushup";
//                    }
//                    if (classType.equals("Squats")) {
//                        classType = "squat";
//                    }
//                    long prevToday = document.getLong(classType + "Today");
//                    userRef.update(classType + "Today", prevToday + repCount);
//
//                    long prevAllTime = document.getLong(classType + "AllTime");
//                    if (repCount > prevAllTime){
//                        userRef.update(classType + "AllTime", repCount);
//                    }
//                    Toast.makeText(this,"You have logged "+repCount+" "+classType+"s!", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        // FIREBASE STUFF
        user = auth.getCurrentUser();

        //Get Permissions from user
        if (!allRuntimePermissionsGranted()) {
            getRuntimePermissions();
        }


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

        //To setup nav bar
        setUpBottomNavBar(R.id.bottom_home);

    }

    //To confirm that it permission is given, close the app otherwise
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUESTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied. App will now close.", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(this)
                        .setTitle("Permission Denied")
                        .setMessage("You have denied the required permission. The app will now close.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        }
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
            Toast.makeText(FrontPageActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(FrontPageActivity.this, Login.class);
            startActivity(intent);
            finish();
        }
    }

    //To check and get permissions
    //Converted and adapted from Kotlin version given in Google ML Kit Demo App
    private boolean allRuntimePermissionsGranted() {
        for (String permission : REQUIRED_RUNTIME_PERMISSIONS) {
            if (permission != null) {
                if (!isPermissionGranted(this, permission)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : REQUIRED_RUNTIME_PERMISSIONS) {
            if (permission != null) {
                if (!isPermissionGranted(this, permission)) {
                    permissionsToRequest.add(permission);
                }
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    PERMISSION_REQUESTS //Request code
            );
        }
    }

    private boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            Log.i("MyMessage", "Permission granted: " + permission);
            return true;
        }
        Log.i("MyMessage", "Permission NOT granted: " + permission);
        return false;
    }

    //@Override
    //public void onBackPressed() {
        // Do nothing (disable back button functionality)

}