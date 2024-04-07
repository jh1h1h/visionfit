package Team10_VisionFit.UI;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teamten.visionfit.R;

public class MyStatisticsActivity extends BaseActivity {

    FirebaseAuth auth;
    TextView num_of_situps, num_of_pushups, num_of_squats, num_of_weightlift;


    private FirebaseFirestore firestore;
    int streakCount;

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

                                streakCount = document.getLong("streak").intValue();

                                final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                                final TextView streakText = (TextView) findViewById(R.id.streakText);

                                progressBar.setProgress(Integer.parseInt(String.valueOf(streakCount)));
                                String streak = streakCount + " days";
                                streakText.setText(streak);

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

        //To setup nav bar
        setUpBottomNavBar(R.id.bottom_logout);

    }

    @Override
    public void onBackPressed() {
        // Do nothing (disable back button functionality)
    }
}