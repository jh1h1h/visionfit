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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teamten.visionfit.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import Team10_VisionFit.Backend.firebaseAuthentication.Login;
import Team10_VisionFit.MainActivity;
import Team10_VisionFit.PoseDetector.LivePreviewActivity;

public class DailyChallengeActivity extends AppCompatActivity {
    FirebaseAuth auth;
    int numSquatsChallengeCompleted;
    int numPushupsChallengeCompleted;
    boolean hasCompletedSquatsChallengeToday = false;
    boolean hasCompletedPushupsChallengeToday = false;
    String LOGLABEL = "DailyChallengeActivity";
    int baseReps = 5; //"Starting" number of reps
    int numSquatsRepsToDo;
    int numPushupsRepsToDo;
    TextView challengeStreakMessage;
    TextView timer_text;
    Button pushUpButton;
    Button squatsButton;
    DocumentReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_challenge);

        challengeStreakMessage = findViewById(R.id.challengeStreakMessage);

        // Initialize UI elements
        pushUpButton = findViewById(R.id.pushUpButton);
        squatsButton = findViewById(R.id.squatsButton);

        //Getting the user's number of completed challenges to decide on the new challenge, and whether the user has completed today's challenges
        auth = FirebaseAuth.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //Get the current logged in User's ID
        userRef = FirebaseFirestore.getInstance().collection("users").document(uid); //Using that ID, get the user's data from firestore

        // Retrieve user data from Firestore
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    // Get the current status of whether challenge has been completed and total challenges completed
                    numSquatsChallengeCompleted = document.getLong("numSquatsChallengeCompleted").intValue();
                    numPushupsChallengeCompleted = document.getLong("numPushupsChallengeCompleted").intValue();
                    hasCompletedSquatsChallengeToday = document.getBoolean("hasCompletedSquatsChallengeToday");
                    hasCompletedPushupsChallengeToday = document.getBoolean("hasCompletedPushupsChallengeToday");

                    Log.d(LOGLABEL, "User data retrieved from Firestore: Squats completed=" + numSquatsChallengeCompleted +
                            ", Pushups completed=" + numPushupsChallengeCompleted +
                            ", Squats today=" + hasCompletedSquatsChallengeToday +
                            ", Pushups today=" + hasCompletedPushupsChallengeToday);

                    // Calculate the number of reps to do based on number of daily challenges completed
                    numSquatsRepsToDo = baseReps + ((numSquatsChallengeCompleted / 5) * 2);
                    numPushupsRepsToDo = baseReps + ((numPushupsChallengeCompleted / 5) * 2);

                    // Update UI based on completion status
                    updateUI(numPushupsRepsToDo, numSquatsRepsToDo);
                    updateStreakUI();
                }
            }

        });

        // Setup bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                    return true;
                case R.id.bottom_settings:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    finish();
                    return true;
                case R.id.cameraButton:
                    startActivity(new Intent(getApplicationContext(), LivePreviewActivity.class));
                    finish();
                    return true;
                case R.id.bottom_userProfile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    finish();
                    return true;
                case R.id.bottom_logout:
                    customExitDialog();
                    return true;
            }
            return false;
        });

        // Setup onClickListeners for pushUpButton
        pushUpButton.setOnClickListener(v -> {
            Log.d(LOGLABEL, "Push Up Button Clicked");
            Intent intent = new Intent(DailyChallengeActivity.this, LivePreviewActivity.class);
            intent.putExtra("ClassType", "Push Ups");
            startActivity(intent);
            completeChallenge(hasCompletedPushupsChallengeToday, numPushupsRepsToDo, "hasCompletedPushupsChallengeToday", "numPushupsChallengeCompleted");
        });

        // Setup onClickListeners for squatsButton
        squatsButton.setOnClickListener(v -> {
            Log.d(LOGLABEL, "Squats Button Clicked");
            Intent intent = new Intent(DailyChallengeActivity.this, LivePreviewActivity.class);
            intent.putExtra("ClassType", "Squats");
            startActivity(intent);
            completeChallenge(hasCompletedSquatsChallengeToday, numSquatsRepsToDo, "hasCompletedSquatsChallengeToday", "numSquatsChallengeCompleted");
        });
    }

    // Method to handle completing a challenge and updating Firestore
    private void completeChallenge(boolean hasCompleted, int repsToDo, String completionField, String countField) {
        if (!hasCompleted) {
            // Simulate completion (replace with actual logic to determine completion)
            int numRepsCompleted = repsToDo; // Placeholder for actual logic
            if (numRepsCompleted >= repsToDo) {
                Log.d(LOGLABEL, "Challenge completed");

                // Update completion status and count
                userRef.update(completionField, true).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i(LOGLABEL, "Update to Firestore successful");
                        // Increment challenge completed count
                        userRef.update(countField, FieldValue.increment(1));
                    }
                });

                // Increment streak field if any challenge is completed
                userRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Date lastCompletedDate = documentSnapshot.getDate("lastCompletedDate");
                        Date currentDate = new Date();
                        Long streak = documentSnapshot.getLong("streak");
                        if (lastCompletedDate == null || !isSameDay(lastCompletedDate, currentDate)) {
                            streak = (streak == null ? 0 : streak) + 1;
                            userRef.update("streak", streak);
                            userRef.update("lastCompletedDate", currentDate);
                            if (streak % 3 == 0) {
                                baseReps += 1;
                                userRef.update("baseReps", baseReps);
                            }
                        }
                        updateStreakUI();
                    }
                });

                // Update UI - Change button color to green
                updateUI(numPushupsRepsToDo, numSquatsRepsToDo);
            }
        }
    }

    // Method to update UI based on completion status
    private void updateUI(int numPushupsRepsToDo, int numSquatsRepsToDo) {
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled } // enabled
        };
        int[] colors = new int[] {
                Color.GREEN // tint color
        };

        if (hasCompletedPushupsChallengeToday) {
            pushUpButton.setBackgroundTintList(new ColorStateList(states, colors));
        } else {
            String challengeText = numPushupsRepsToDo + " PUSH UPS";
            pushUpButton.setText(challengeText);
        }

        if (hasCompletedSquatsChallengeToday) {
            squatsButton.setBackgroundTintList(new ColorStateList(states, colors));
        } else {
            String challengeText = numSquatsRepsToDo + " SQUATS";
            squatsButton.setText(challengeText);
        }
    }

    // Method to handle custom exit dialog
    public void customExitDialog() {
        final Dialog dialog = new Dialog(DailyChallengeActivity.this);
        dialog.setContentView(R.layout.logout_dialog_box);

        TextView dialogButtonYes = dialog.findViewById(R.id.textViewYes);
        TextView dialogButtonNo = dialog.findViewById(R.id.textViewNo);

        dialogButtonNo.setOnClickListener(v -> dialog.dismiss());

        dialogButtonYes.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            SharedPreferences sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            finish();
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        // Do nothing (disable back button functionality)
    }

    // Method to update streak UI
    private void updateStreakUI() {
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                Long streak = document.getLong("streak");
                streak = streak != null ? streak : 0;
                challengeStreakMessage.setText("Current streak: " + streak + " days");
            }
        });
    }

    // Method to check if two dates are the same day
    private boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }
}
