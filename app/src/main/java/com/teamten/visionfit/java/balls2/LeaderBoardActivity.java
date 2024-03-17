package com.teamten.visionfit.java.balls2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebaseCloudFireStore.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamten.visionfit.R;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity {

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
    }
}
        /*
         TextView tv=findViewById(R.id.leaderboardTitle);
        YoYo.with(Techniques.SlideInRight).duration(2000).playOn(tv);
        */
