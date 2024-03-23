package Team10_VisionFit.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamten.visionfit.R;

import java.util.ArrayList;

import Team10_VisionFit.Backend.firebaseAuthentication.Login;
import Team10_VisionFit.MainActivity;
import Team10_VisionFit.PoseDetector.LivePreviewActivity;

public class LeaderBoardActivity extends AppCompatActivity {
    FirebaseAuth auth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // asynchronously retrieve all users
        CollectionReference users = db.collection("users");
        Query query = users.orderBy("repsToday", Query.Direction.DESCENDING);



        int[] nameRows = {R.id.Name1, R.id.Name2, R.id.Name3, R.id.Name4,
                R.id.Name5, R.id.Name6, R.id.Name7, R.id.Name8, R.id.Name9,
                R.id.Name10, R.id.Name11, R.id.Name12, R.id.Name13, R.id.Name14,
                R.id.Name15};
        int[] repRows = {R.id.Rep1, R.id.Rep2, R.id.Rep3, R.id.Rep4, R.id.Rep5,
                R.id.Rep6, R.id.Rep7, R.id.Rep8, R.id.Rep9, R.id.Rep10, R.id.Rep11,
                R.id.Rep12, R.id.Rep13, R.id.Rep14, R.id.Rep15,};
        //query will return reps of ALL** users sorted by highest first

        Task<QuerySnapshot> querySnapshotTask =query.get();

        querySnapshotTask
                .addOnSuccessListener(result -> {
                    QuerySnapshot qsnapshot = result;
                    ArrayList<DocumentSnapshot> userArrList = new ArrayList<>(qsnapshot.getDocuments());
                    DocumentSnapshot currentUser = null; //Store current user doc Snapshot if found

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    for (int i = 0; i < 15; i++) {
                        TextView name = (TextView) findViewById(nameRows[i]);
                        TextView reps = (TextView) findViewById(repRows[i]);
                        DocumentSnapshot userDoc = userArrList.get(i);

                        String username = userDoc.getId();
                        String repsAmt = userDoc.get("repsToday", Long.class).toString();
                        name.setText(username);
                        reps.setText(repsAmt);
                    }
                    for (DocumentSnapshot doc : userArrList) {

                        if (doc.get("userUID", String.class).equals(uid)) {
                            currentUser = doc;

                        }
                    }
                    //last entry on the board, either cfm is user, determine rank
                    //TODO: update rank and entries dynamically to display beyond 15 users
                    //TODO: add 3 other leaderboards for other exercises
                        //user is beyond #15
                        TextView yourRank = (TextView) findViewById(R.id.yourRank);
                        yourRank.setText(userArrList.indexOf(currentUser)+"");//set to string

                    TextView yourName = findViewById(R.id.yourName);
                    TextView yourRep = findViewById(R.id.yourRep);
                    yourName.setText(currentUser.getId());
                    yourRep.setText(currentUser.get("repsToday", Long.class).toString());
                })
                .addOnFailureListener(e -> {
                    // now do something with the exception
                });
        setContentView(R.layout.activity_leader_board);
        /*
         TextView tv=findViewById(R.id.leaderboardTitle);
        YoYo.with(Techniques.SlideInRight).duration(2000).playOn(tv);
        */

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
                    Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_SHORT).show();
                    Log.d("Button Check", "Logout Button Clicked");
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    SharedPreferences sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);
                    sharedPreferences.edit().clear().commit();
                    finish();
                    return true;
            }
            return false;
        });
    }
}