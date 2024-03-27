package Team10_VisionFit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teamten.visionfit.R;

import java.util.ArrayList;

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

    private static final int PERMISSION_REQUESTS = 1; //Its just a request code, arbitrary
    private static final String[] REQUIRED_RUNTIME_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(uid);


        textView = findViewById(R.id.user_details);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    String displayName = document.getString("username");
                    textView.setText("Hello, "+displayName+"!");}
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
                    Intent intent = new Intent(getApplicationContext(), LivePreviewActivity.class);
                    intent.putExtra("ClassType", "Free Style");
                    startActivity(intent);
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
                    return true;
            }
            return false;
        });
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

    @Override
    public void onBackPressed() {
        // Do nothing (disable back button functionality)
    }
}