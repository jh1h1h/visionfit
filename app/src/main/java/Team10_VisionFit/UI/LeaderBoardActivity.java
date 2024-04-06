package Team10_VisionFit.UI;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class LeaderBoardActivity extends BaseNavActivity {
    FirebaseAuth auth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // asynchronously retrieve all users
        CollectionReference users = db.collection("users");
        Query query = users.orderBy("pushupAllTime", Query.Direction.DESCENDING);
        setContentView(R.layout.activity_leader_board);

        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> ranks = new ArrayList<>();
        ArrayList<String> reps = new ArrayList<>();

//        int[] nameRows = {R.id.Name1, R.id.Name2, R.id.Name3, R.id.Name4,
//                R.id.Name5, R.id.Name6, R.id.Name7, R.id.Name8, R.id.Name9,
//                R.id.Name10, R.id.Name11, R.id.Name12, R.id.Name13, R.id.Name14,
//                R.id.Name15};
//        int[] repRows = {R.id.Rep1, R.id.Rep2, R.id.Rep3, R.id.Rep4, R.id.Rep5,
//                R.id.Rep6, R.id.Rep7, R.id.Rep8, R.id.Rep9, R.id.Rep10, R.id.Rep11,
//                R.id.Rep12, R.id.Rep13, R.id.Rep14, R.id.Rep15,};
//
        // Fetch user's leaderboard info
        Task<QuerySnapshot> querySnapshotTask = query.get();
        querySnapshotTask
                .addOnSuccessListener(result -> {
                    QuerySnapshot qsnapshot = result;
                    ArrayList<DocumentSnapshot> userArrList = new ArrayList<>(qsnapshot.getDocuments());
                    DocumentSnapshot currentUser = null;

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    for (int i = 0; i < Math.min(15, userArrList.size()); i++) {
//                        TextView name = findViewById(nameRows[i]);
//                        TextView reps = findViewById(repRows[i]);
                        DocumentSnapshot userDoc = userArrList.get(i);
//
                        if (userDoc != null) {

                            Long repsLong = userDoc.get("pushupAllTime", Long.class);
                            String repsAmt = (repsLong != null) ? repsLong.toString() : "0";
                            names.add(userDoc.get("username",String.class));
                            reps.add(repsAmt);
                            ranks.add("abc");
//                            name.setText(userDoc.get("username",String.class));
//                            reps.setText(repsAmt);
                        }
//                        else {
//                            name.setText("");
//                            reps.setText("");
//                        }
                    }

                    // RecyclerView
                    RecyclerView leaderboard = findViewById(R.id.leaderboard);

                    RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardHolder> adapter
                            = new LeaderBoardAdapter(this, names, ranks, reps);
                    leaderboard.setAdapter( adapter );
                    leaderboard.setLayoutManager( new LinearLayoutManager(this));

                    for (DocumentSnapshot doc : userArrList) {
                        Log.d("uid:",doc.getId());
                        if (doc.getId().equals(uid)) {
                            currentUser = doc;
                            break;
                        }
                    }

                    if (currentUser != null) {
                        TextView yourRank = findViewById(R.id.yourRank);
                        yourRank.setText(String.valueOf(userArrList.indexOf(currentUser) + 1));

                        TextView yourName = findViewById(R.id.yourName);
                        TextView yourRep = findViewById(R.id.yourRep);
                        yourName.setText(currentUser.get("username",String.class));
                        Long currentUserReps = currentUser.get("pushupToday", Long.class);
                        yourRep.setText((currentUserReps != null) ? currentUserReps.toString() : "0");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the exception
                });

        //To setup nav bar
        setUpBottomNavBar(R.id.bottom_logout);
    }

    @Override
    public void onBackPressed() {
        // Do nothing (disable back button functionality)
    }
}

