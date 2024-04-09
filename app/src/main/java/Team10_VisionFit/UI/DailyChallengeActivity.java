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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamten.visionfit.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Team10_VisionFit.Backend.firebaseAuthentication.Login;
import Team10_VisionFit.Backend.leaderboard.BST;
import Team10_VisionFit.Backend.leaderboard.Node;
import Team10_VisionFit.UI.BaseActivity;
import Team10_VisionFit.PoseDetector.LivePreviewActivity;

public class DailyChallengeActivity extends BaseActivity {
    public int counter = 3;
    FirebaseAuth auth;
    int numSquatsChallengeCompleted;
    int numPushupsChallengeCompleted;
    int streakCount = 0;
    boolean hasCompletedSquatsChallengeToday;
    boolean hasCompletedPushupsChallengeToday;
    int current_points = 0;
    int lifetime_points = 0;
    String LOGLABEL = "DailyChallengeActivity";
    int baseReps = 5; //"Starting" number of reps
    int numSquatsRepsToDo;
    int numPushupsRepsToDo;
    int repCount;
    String classType;
    TextView timer_text;
    TextView challengeStreakMessage;
    boolean streakChange;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_challenge);

        challengeStreakMessage = findViewById(R.id.challengeStreakMessage);

        //Getting the user's number of completed challenges to decide on the new challenge, and whether the user has completed today's challenges
        auth = FirebaseAuth.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //Get the current logged in User's ID
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(uid); //Using that ID, get the user's data from firestore

        //The task is done on another thread, hence need to put everything inside
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    //Get the current status of whether challenge has been completed and total challenges completed
                    numSquatsChallengeCompleted = document.getLong("numSquatsChallengeCompleted").intValue();
                    numPushupsChallengeCompleted = document.getLong("numPushupsChallengeCompleted").intValue();
                    hasCompletedSquatsChallengeToday = document.getBoolean("hasCompletedSquatsChallengeToday");
                    hasCompletedPushupsChallengeToday = document.getBoolean("hasCompletedPushupsChallengeToday");
                    streakCount = document.getLong("streak").intValue();
                    streakChange = document.getBoolean("streakChange");
                    current_points = document.getLong("current_points").intValue();
                    lifetime_points = document.getLong("lifetime_points").intValue();

//                    Log.d(LOGLABEL, "INSIDE: " + String.valueOf(numSquatsChallengeCompleted) + " " + String.valueOf(numPushupsChallengeCompleted) + " "
//                            + String.valueOf(hasCompletedSquatsChallengeToday) + " " + String.valueOf(hasCompletedPushupsChallengeToday));

                    Button pushUpButton = findViewById(R.id.pushUpButton);
                    Button squatsButton = findViewById(R.id.squatsButton);

                    //To setup nav bar
                    setUpBottomNavBar(R.id.bottom_logout);

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
                            CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");
                            usersRef.get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    QuerySnapshot lb = task1.getResult();
                                    BST lbBST = new BST(usersRef, classType);
                                    ArrayList<DocumentSnapshot> lbList = new ArrayList<>(lb.getDocuments());
                                    for (DocumentSnapshot lbNode: lbList) {
                                        if (lbNode.get(classType+"BSTparent",String.class) != null){
                                            lbBST.populate_node(new Node(
                                                    lbNode.getLong(classType + "AllTime"),
                                                    lbNode.get(classType+"BSTleft",String.class),
                                                    lbNode.get(classType+"BSTright",String.class),
                                                    lbNode.get(classType+"BSTparent",String.class),
                                                    lbNode.getId(),
                                                    lbNode
                                            ));
                                        }
                                    }

                                    // add workout to client BST and upload to firebase
                                    // TODO: potential bug: document has outdated count and gets reflected onto node document
                                    if (document.get(classType+"BSTparent",String.class) != null){
                                        lbBST.tree_delete(new Node((long) repCount, uid, document),lbBST.root);
                                    }
                                    lbBST.tree_insert(new Node((long) repCount, uid, document),lbBST.root,"root");
                                }
                            });
                        }
                    }

                    if (intent.getStringExtra("Push Ups Reps") != null) {
                        int numReps = Integer.parseInt(intent.getStringExtra("Push Ups Reps"));

                        //Check if it exceeds the number of reps required to pass the challenge
                        if ((numReps >= numPushupsRepsToDo) && (!(hasCompletedPushupsChallengeToday))){
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
                        if ((numReps >= numSquatsRepsToDo) && (!(hasCompletedSquatsChallengeToday))){
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
                    }
                    //Add the number to the text in the button
                    String challengeText = String.valueOf(numSquatsRepsToDo) + " SQUATS";
                    squatsButton.setText(challengeText);


                    if (hasCompletedPushupsChallengeToday) {
                        //If yes, change button colour to green
                        pushUpButton.setBackgroundTintList(new ColorStateList(states, colors));
                    }
                    //Add the number to the text in the button
                    challengeText = String.valueOf(numPushupsRepsToDo) + " PUSH UPS";
                    pushUpButton.setText(challengeText);

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

                    if ((hasCompletedPushupsChallengeToday || hasCompletedSquatsChallengeToday) && (!streakChange)) {
                        streakCount++;
                        current_points += 100;
                        lifetime_points += 100;
                        userRef.update("streakChange", true);
                        userRef.update("streak", streakCount);
                        userRef.update("current_points", current_points);
                        userRef.update("lifetime_points", lifetime_points);
                        String streakText = "Your current streak is " + streakCount + " days!" + "\n" +"Keep it going!";
                        challengeStreakMessage.setText(streakText);
                    } else {
                        String streakText = "Your current streak is " + streakCount + " days!" + "\n" +"Keep it going!";
                        if (challengeStreakMessage != null) {
                            challengeStreakMessage.setText(streakText);
                        }
                    }

                    if (hasCompletedSquatsChallengeToday && hasCompletedPushupsChallengeToday && streakChange) {
                        current_points += 50;
                        lifetime_points += 50;
                        userRef.update("current_points", current_points);
                        userRef.update("lifetime_points", lifetime_points);
                    }
                }
            }
        });
    }

    //@Override
    //public void onBackPressed() {
        // Do nothing (disable back button functionality)
    //}
}