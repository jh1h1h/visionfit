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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teamten.visionfit.R;

import Team10_VisionFit.Backend.firebaseAuthentication.Login;
import Team10_VisionFit.MainActivity;
import Team10_VisionFit.PoseDetector.LivePreviewActivity;

public class MyRewardsActivity extends AppCompatActivity {
    FirebaseAuth auth;
    boolean hasCompletedSquatsChallengeToday;
    boolean hasCompletedPushupsChallengeToday;
    boolean streakChange;
    int current_points;
    int lifetime_points;
    TextView my_points_now;
    TextView my_total_points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rewards);

        my_points_now = findViewById(R.id.earnings);
        my_total_points = findViewById(R.id.lifettime_earnings);

        //Getting the user's number of completed challenges to decide on the new challenge, and whether the user has completed today's challenges
        auth = FirebaseAuth.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //Get the current logged in User's ID
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(uid); //Using that ID, get the user's data from firestore

        //The task is done ASYNCHRONOUSLY, hence need to put everything inside
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    //Get the current status of whether challenge has been completed and total challenges completed
                    hasCompletedSquatsChallengeToday = document.getBoolean("hasCompletedSquatsChallengeToday");
                    hasCompletedPushupsChallengeToday = document.getBoolean("hasCompletedPushupsChallengeToday");
                    streakChange = document.getBoolean("streakChange");
                    current_points = document.getLong("current_points").intValue();
                    lifetime_points = document.getLong("lifetime_points").intValue();

                    // Set the text of TextViews after updating points
                    my_points_now.setText(String.valueOf(current_points));
                    my_total_points.setText(String.valueOf(lifetime_points));
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

                        //case R.id.cameraButton:
                        //Intent intent = new Intent(getApplicationContext(), LivePreviewActivity.class);
                        //intent.putExtra("ClassType", "Free Style");
                        //startActivity(intent);
                        //Log.d("Button Check", "Camera Button Clicked");
                        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        //finish();
                        //return true;

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
        });
    }

    public  void customExitDialog()
    {
        // creating custom dialog
        final Dialog dialog = new Dialog(MyRewardsActivity.this);

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
