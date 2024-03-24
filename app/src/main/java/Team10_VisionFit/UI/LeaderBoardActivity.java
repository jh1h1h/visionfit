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

        Task<QuerySnapshot> querySnapshotTask = query.get();

        querySnapshotTask
                .addOnSuccessListener(result -> {
                    QuerySnapshot qsnapshot = result;
                    ArrayList<DocumentSnapshot> userArrList = new ArrayList<>(qsnapshot.getDocuments());
                    DocumentSnapshot currentUser = null;

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    for (int i = 0; i < Math.min(15, userArrList.size()); i++) {
                        TextView name = findViewById(nameRows[i]);
                        TextView reps = findViewById(repRows[i]);
                        DocumentSnapshot userDoc = userArrList.get(i);

                        if (userDoc != null) {
                            String username = userDoc.getId();
                            Long repsLong = userDoc.get("repsToday", Long.class);
                            String repsAmt = (repsLong != null) ? repsLong.toString() : "0";
                            name.setText(username);
                            reps.setText(repsAmt);
                        } else {
                            name.setText("");
                            reps.setText("");
                        }
                    }
                    for (DocumentSnapshot doc : userArrList) {
                        if (doc.get("userUID", String.class).equals(uid)) {
                            currentUser = doc;
                            break;
                        }
                    }

                    if (currentUser != null) {
                        TextView yourRank = findViewById(R.id.yourRank);
                        yourRank.setText(String.valueOf(userArrList.indexOf(currentUser) + 1));

                        TextView yourName = findViewById(R.id.yourName);
                        TextView yourRep = findViewById(R.id.yourRep);
                        yourName.setText(currentUser.getId());
                        Long currentUserReps = currentUser.get("repsToday", Long.class);
                        yourRep.setText((currentUserReps != null) ? currentUserReps.toString() : "0");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the exception
                });
        setContentView(R.layout.activity_leader_board);

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

