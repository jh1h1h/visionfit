package Team10_VisionFit.UI;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
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

public class DailyChallengeActivity extends AppCompatActivity{
    public int counter = 3;
    FirebaseAuth auth;
    int numSquatsChallengeCompleted;
    int numPushupsChallengeCompleted;
    boolean hasCompletedSquatsChallengeToday;
    boolean hasCompletedPushupsChallengeToday;
    String LOGLABEL = "DailyChallengeActivity";
    int baseReps = 5; //"Starting" number of reps
    int numSquatsRepsToDo;
    int numPushupsRepsToDo;
    int repCount;
    String classType;
    TextView timer_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_challenge);

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
                    numSquatsChallengeCompleted = document.getLong("numSquatsChallengeCompleted").intValue();
                    numPushupsChallengeCompleted = document.getLong("numPushupsChallengeCompleted").intValue();
                    hasCompletedSquatsChallengeToday = document.getBoolean("hasCompletedSquatsChallengeToday");
                    hasCompletedPushupsChallengeToday = document.getBoolean("hasCompletedPushupsChallengeToday");

                    Log.d(LOGLABEL, "INSIDE: " + String.valueOf(numSquatsChallengeCompleted) + " " + String.valueOf(numPushupsChallengeCompleted) + " "
                            + String.valueOf(hasCompletedSquatsChallengeToday) + " " + String.valueOf(hasCompletedPushupsChallengeToday));

                    Button pushUpButton = findViewById(R.id.pushUpButton);
                    Button squatsButton = findViewById(R.id.squatsButton);

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

                    //To set colour to green
                    int[][] states = new int[][] {
                            new int[] { android.R.attr.state_enabled } // enabled
                    };
                    int[] colors = new int[] {
                            Color.GREEN // tint color
                    };
                    //Calculate the number of reps to do based on number of daily challenges completed
                    //The idea is that every 5 days, increment the reps by 2
                    numSquatsRepsToDo = baseReps + ((numSquatsChallengeCompleted / 5) * 2); //the divide will give an int, so its basically integer divide, then I multiply that by 2 because I want to add 2 to each increment of 5 days
                    numPushupsRepsToDo = baseReps + ((numPushupsChallengeCompleted / 5) * 2); //the divide will give an int, so its basically integer divide, then I multiply that by 2 because I want to add 2 to each increment of 5 days

                    //Get intent to retrieve if we are returning from live preview activity
                    Intent intent = getIntent();
                    repCount = intent.getIntExtra("repCount",0);
                    classType = intent.getStringExtra("ClassType");

                    if (repCount != 0 && classType != null && !classType.equals("Free Style")) {
                        if (classType.equals("Push Ups")) {
                            classType = "pushup";
                        }
                        if (classType.equals("Squats")) {
                            classType = "squat";
                        }
                        long prevToday = document.getLong(classType + "Today");
                        userRef.update(classType + "Today", prevToday + repCount);

                        long prevAllTime = document.getLong(classType + "AllTime");
                        if (repCount > prevAllTime){
                            userRef.update(classType + "AllTime", repCount);
                        }
                        Toast.makeText(this,"You have logged "+repCount+" "+classType+"s!", Toast.LENGTH_SHORT).show();
                    }

                    if (intent.getStringExtra("Push Ups Reps") != null) {
                        int numReps = Integer.parseInt(intent.getStringExtra("Push Ups Reps"));

                        //Check if it exceeds the number of reps required to pass the challenge
                        if (numReps >= numPushupsRepsToDo) {
                            hasCompletedPushupsChallengeToday = true;
                            numPushupsChallengeCompleted++;

                            //Update firestore
                            userRef.update("hasCompletedPushupsChallengeToday", true).addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    Log.i(LOGLABEL, "Update to Firestore successful");
                                }
                            });

                            userRef.update("numPushupsChallengeCompleted", numPushupsChallengeCompleted).addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    Log.i(LOGLABEL, "Update to Firestore successful");
                                }
                            });

                        }

                    } else if (intent.getStringExtra("Squats Reps") != null) {
                        int numReps = Integer.parseInt(intent.getStringExtra("Squats Reps"));

                        //Check if it exceeds the number of reps required to pass the challenge
                        if (numReps >= numSquatsRepsToDo) {
                            hasCompletedSquatsChallengeToday = true;
                            numSquatsChallengeCompleted++;

                            //Update firestore
                            userRef.update("hasCompletedSquatsChallengeToday", true).addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    Log.i(LOGLABEL, "Update to Firestore successful");
                                }
                            });

                            userRef.update("numSquatsChallengeCompleted", numSquatsChallengeCompleted).addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    Log.i(LOGLABEL, "Update to Firestore successful");
                                }
                            });
                        }
                    }


                    //Determine if any challenges has already been completed for the day
                    if (hasCompletedSquatsChallengeToday) {
                        //If yes, change button colour to green
                        squatsButton.setBackgroundTintList(new ColorStateList(states, colors));
                    } else {
                        //Add the number to the text in the button
                        String challengeText = String.valueOf(numSquatsRepsToDo) + " SQUATS";
                        squatsButton.setText(challengeText);
                    }

                    if (hasCompletedPushupsChallengeToday) {
                        //If yes, change button colour to green
                        pushUpButton.setBackgroundTintList(new ColorStateList(states, colors));
                    } else {
                        //Add the number to the text in the button
                        String challengeText = String.valueOf(numPushupsRepsToDo) + " PUSH UPS";
                        pushUpButton.setText(challengeText);
                    }

                    //Setup on click listeners
                    pushUpButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Button Check", "Push Up Button Clicked");
                            Intent intent = new Intent(DailyChallengeActivity.this, LivePreviewActivity.class);
                            intent.putExtra("ClassType", "Push Ups");
                            startActivity(intent);
                        }
                    });

                    squatsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Button Check", "Squats Button Clicked");
                            Intent intent = new Intent(DailyChallengeActivity.this, LivePreviewActivity.class);
                            intent.putExtra("ClassType", "Squats");
                            startActivity(intent);
                        }
                    });
                }
            }
        });




    }

    public  void customExitDialog()
    {
        // creating custom dialog
        final Dialog dialog = new Dialog(DailyChallengeActivity.this);

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
