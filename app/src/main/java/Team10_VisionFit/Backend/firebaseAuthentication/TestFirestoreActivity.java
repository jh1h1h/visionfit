package Team10_VisionFit.Backend.firebaseAuthentication;
//This is an activity that shows how test/data from the view is sent to firebase
//implementation of reading/writing data might looks like this:
/*
***Reading***
username: Display username in profile
streak:display as statistics in summary page/main page
repsToday:display in main page
repsAllTime: (Most reps or cumulative(?), display in summary page
Country:can be used render flag in leaderboard

***Update **
username: Change username upon request
streak: increment once a day until slacking is detected
repsAllTime/repsToday: update if new>current value as well (after every attempt)
country:not neccesarily updated
TODO: write and delete entires in the db
TODO: write up how to do queries universally for leaderboard
 */

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.teamten.visionfit.R;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class TestFirestoreActivity extends AppCompatActivity {
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test_firestore);
        // getting our instance
        // from Firebase Firestore.


        // initializing our edittext and buttons
        EditText userNameEdt = (EditText) findViewById(R.id.idEdtUserName);
        EditText countryEdt = (EditText)findViewById(R.id.idEdtCountry);
        EditText streakEdt = (EditText)findViewById(R.id.idEdtStreak);
        EditText repsAllTimeEdt = (EditText)findViewById(R.id.idEdtRepsAllTime);
        EditText repsTodayEdt =(EditText) findViewById(R.id.idEdtRepsToday);
        Button submitUserBtn = (Button)findViewById(R.id.idBtnSubmitUser);

        // adding on click listener for button
        submitUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting data from edittext fields.
                String username = userNameEdt.getText().toString();
                String country = countryEdt.getText().toString();
                int streak = Integer.parseInt(streakEdt.getText().toString());
                int repsToday = Integer.parseInt(repsTodayEdt.getText().toString());
                int repsAllTime = Integer.parseInt(repsAllTimeEdt.getText().toString());


                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(username)) {
                    userNameEdt.setError("Please enter username");
                } else if (TextUtils.isEmpty(country)) {
                    streakEdt.setError("Please enter country");

                } else {
                    // calling method to add data to Firebase Firestore.
                    //addDataToFirestore(username,country,streak,repsToday,repsAllTime);
                }
            }
        });
    }

    public static void addDataToFirestore(String uid, String username, String country, String dob, int streak, int pushupToday,
                                   int pushupAllTime,
                                   int situpToday,
                                   int situpAllTime,
                                   int squatToday,
                                   int squatAllTime,
                                   int weightliftToday,
                                   int weightliftAllTime,
                                   int numSquatsChallengeCompleted,
                                   int numPushupsChallengeCompleted,
                                   boolean hasCompletedSquatsChallengeToday,
                                   boolean hasCompletedPushupsChallengeToday,
                                          boolean streakChange, int current_points, int lifetime_points, int num_rewards1, int num_rewards2, int num_rewards3, int num_rewards4) {

        // creating a collection reference
        // for our Firebase Firestore database.
        User user=new User(username,country,dob, streak,pushupToday,
         pushupAllTime,
         situpToday,
        situpAllTime,
        squatToday,
         squatAllTime,
        weightliftToday,
         weightliftAllTime,
        numSquatsChallengeCompleted,
        numPushupsChallengeCompleted,
        hasCompletedSquatsChallengeToday,
        hasCompletedPushupsChallengeToday,
                streakChange, current_points, lifetime_points, num_rewards1, num_rewards2, num_rewards3, num_rewards4);
        db.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(TestFirestoreActivity.this,"User successfully added!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(TestFirestoreActivity.this, "Fail to add user \n" + e, Toast.LENGTH_SHORT).show();
                    }
                });

        // adding our data to our User class.


        // below method is use to add data to Firebase Firestore.
    }
    public User getDataFromFirestore(String username){
        DocumentReference docRef = db.collection("users").document(username);
        User user=new User();
        DocumentSnapshot document=docRef.get().getResult();
        if (document.exists()){
            user=document.toObject(User.class);
        }
        return user;

    }
    public void deleteDataInFirestore(String username){

        db.collection("users").document(username)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void updateDataInFirestore(String username, int caseId, String value){
        DocumentReference docRef=db.collection("users").document(username);
        DocumentSnapshot document=docRef.get().getResult();
        switch(caseId) {
            case 0://update username
                User user=getDataFromFirestore(username);
                if(user.streak==-1){//username not found in db
                    Log.d(TAG, "User does not exist");
                }
                else{
                    deleteDataInFirestore(username);
                    //addDataToFirestore(value,user.country,user.streak,user.repsToday,user.repsAllTime);
                }

            case 1://update country
                if(document.exists()){
                    docRef.update("country",value);
                }

            case 2://increment daily streak by 1
                if(document.exists()){
                    docRef.update("streak", FieldValue.increment(1));
                }
            case 3://reset daily streak
                if(document.exists()){
                    docRef.update("streak",0);
                }

            case 4://update(highest) reps done today
                if(document.exists()){
                    docRef.update("repsToday",Integer.valueOf(value));
                }

            case 5://update record highest reps
                if(document.exists()){
                    docRef.update("repsAllTime",Integer.valueOf(value));
                }
            default:
                // code block
                break;
        }
    }
}