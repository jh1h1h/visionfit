package Team10_VisionFit.UI;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teamten.visionfit.R;

import Team10_VisionFit.Backend.firebaseAuthentication.Login;
import Team10_VisionFit.MainActivity;
import Team10_VisionFit.PoseDetector.LivePreviewActivity;

public class MyStatisticsActivity extends AppCompatActivity {

    FirebaseAuth auth;
    TextView num_of_situps, num_of_pushups, num_of_squats, num_of_weightlift;


    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_statistics);

        firestore = FirebaseFirestore.getInstance();

        num_of_situps = findViewById(R.id.situps_num);
        num_of_pushups = findViewById(R.id.pushups_num);
        num_of_squats = findViewById(R.id.squats_num);
        num_of_weightlift = findViewById(R.id.weightlift_num);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            DocumentReference userRef = firestore.collection("users").document(userId);

            // Fetch user data from Firestore
            userRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                // Get profile picture URL and display name
                                Long situp_alltime = document.get("situpAllTime", Long.class);
                                Long pushup_alltime = document.get("pushupAllTime", Long.class);
                                Long squat_alltime = document.get("squatAllTime", Long.class);
                                Long weightlift_alltime = document.get("weightliftAllTime", Long.class);

                                num_of_situps.setText(situp_alltime.toString());
                                num_of_pushups.setText(pushup_alltime.toString());
                                num_of_squats.setText(squat_alltime.toString());
                                num_of_weightlift.setText(weightlift_alltime.toString());
                            } else {
                                // Handle the case where user data is not available
                                Log.d("MyStatisticsActivity", "No such document");
                            }
                        } else {
                            // Handle failures
                            Log.d("MyStatisticsActivity", "Failed to fetch user data: ", task.getException());
                        }
            });
        } else {
            // Handle the case where the user is not signed in
            Log.d("MyStatisticsActivity", "User is not signed in");
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
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
                    return true;
            }
            return false;
        });
    }

    public  void customExitDialog()
    {
        // creating custom dialog
        final Dialog dialog = new Dialog(MyStatisticsActivity.this);

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

    @Override
    public void onBackPressed() {
        // Do nothing (disable back button functionality)
    }
}