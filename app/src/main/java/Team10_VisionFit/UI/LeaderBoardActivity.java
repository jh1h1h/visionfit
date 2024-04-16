package Team10_VisionFit.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamten.visionfit.R;

import java.time.Instant;
import java.util.ArrayList;

import Team10_VisionFit.Backend.leaderboard.BST;
import Team10_VisionFit.Backend.leaderboard.Node;
import Team10_VisionFit.PoseDetector.LivePreviewActivity;

public class LeaderBoardActivity extends BaseActivity {
    FirebaseAuth auth;
    String lbType;
    String classType;

    ColorStateList originalButtonColor;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        Button pushUpButton = findViewById(R.id.pushUpLeaderBoardBtn);
        Button squatsButton = findViewById(R.id.squatsLeaderBoardBtn);
        Button toggleLb = (Button) findViewById(R.id.toggleLB);

        originalButtonColor = pushUpButton.getBackgroundTintList();
        //To set colour to green
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled } // enabled
        };
        int[] colors = new int[] {
                Color.GREEN // tint color
        };

        // Set background color of the squats button to green
        squatsButton.setBackgroundTintList(new ColorStateList(states, colors));
        lbType = "AllTime"; classType = "squat";
        loadLeaderboard(classType,lbType);

        //To setup nav bar
        setUpBottomNavBar(R.id.bottom_logout);

        toggleLb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lbType.equals("AllTime")){
                    lbType = "Today";
                }else{
                    lbType = "AllTime";
                }
                loadLeaderboard(classType,lbType);
            }
        });

        pushUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button Check", "Push Up Button Clicked");
                // Set background color of the push-up button to green
                pushUpButton.setBackgroundTintList(new ColorStateList(states, colors));
                classType = "pushup";
                loadLeaderboard(classType, lbType);

                // Reset background color of the squats button to its original color
                squatsButton.setBackgroundTintList(originalButtonColor);
            }
        });

        squatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button Check", "Squats Button Clicked");
                // Set background color of the squats button to green
                squatsButton.setBackgroundTintList(new ColorStateList(states, colors));
                classType = "squat";
                loadLeaderboard(classType, lbType);

                // Reset background color of the push-up button to its original color
                pushUpButton.setBackgroundTintList(originalButtonColor);
            }
        });
    }


    // Fetch user's leaderboard info - asynchronously retrieve all users
    public void loadLeaderboard(String classType, String lbType){
        long dateTime = 10000000000L;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Instant instant = Instant.now();
            dateTime = instant.getEpochSecond();
        }
        else{
            Log.d("ERROR","Version incompatible for class Instant. android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O");
        }
        long dateTime1 = dateTime;
        TextView displayText = findViewById(R.id.displaytext);
        if (lbType.equals("AllTime")){
            displayText.setText("All Time Leaders");
        }else{
            displayText.setText("Daily Leaders");
        }
//        int[] nameRows = {R.id.Name1, R.id.Name2, R.id.Name3, R.id.Name4,
//                R.id.Name5, R.id.Name6, R.id.Name7, R.id.Name8, R.id.Name9,
//                R.id.Name10, R.id.Name11, R.id.Name12, R.id.Name13, R.id.Name14,
//                R.id.Name15};
//        int[] repRows = {R.id.Rep1, R.id.Rep2, R.id.Rep3, R.id.Rep4, R.id.Rep5,
//                R.id.Rep6, R.id.Rep7, R.id.Rep8, R.id.Rep9, R.id.Rep10, R.id.Rep11,
//                R.id.Rep12, R.id.Rep13, R.id.Rep14, R.id.Rep15,};

        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> ranks = new ArrayList<>();
        ArrayList<String> reps = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference users = db.collection("users");
//        Query query = users.orderBy("pushupAllTime", Query.Direction.DESCENDING);
//        Task<QuerySnapshot> querySnapshotTask = query.get();
        users.get()
                .addOnSuccessListener(result -> {
                    QuerySnapshot qsnapshot = result;
                    BST lbBST = new BST(users, classType);
                    ArrayList<DocumentSnapshot> userArrList = new ArrayList<>(qsnapshot.getDocuments());
                    DocumentSnapshot currentUser = null;

                    long prevAllTime = 0L;
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    for (DocumentSnapshot userDoc: userArrList) {
//                        TextView name = findViewById(nameRows[i]);
//                        TextView reps = findViewById(repRows[i]);
//                        DocumentSnapshot userDoc = userArrList.get(i);
                        if (userDoc != null) {
                            if (userDoc.get(classType+"BSTparent",String.class) != null){
                                lbBST.populate_node(new Node(
                                        userDoc.getLong(classType + "AllTime"),
                                        userDoc.get(classType+"BSTleft",String.class),
                                        userDoc.get(classType+"BSTright",String.class),
                                        userDoc.get(classType+"BSTparent",String.class),
                                        userDoc.getId(),
                                        userDoc,
                                        userDoc.getLong(classType + "AllTimeTimestamp")
                                ));
                            }else if (userDoc.getId().equals(uid)){
                                currentUser = userDoc;
                                prevAllTime = currentUser.getLong(classType + "AllTime");
                            }
                        }
//                        else {
//                            name.setText("");
//                            reps.setText("");
//                        }


                    }

                    // add workout to client BST and upload to firebase
                    // TODO: potential bug: document has outdated count and gets reflected onto node document
                    if (currentUser != null){
                        users.document(uid).update(classType + "AllTimeTimestamp", dateTime1);
                        lbBST.tree_insert(new Node(prevAllTime, uid, currentUser, dateTime1),lbBST.root,"root");
                    }


                    ArrayList<Node> path = lbBST.inorder_path(lbBST.root);
                    int counter = 1;
                    for (Node node: path){
                        Long repsLong = node.getPoints();
                        String repsAmt = (repsLong != null) ? repsLong.toString() : "0";
                        names.add(node.doc.get("username",String.class));
                        reps.add(repsAmt);
                        ranks.add(String.valueOf(counter));
                        if(node.id.equals(uid)){
                            TextView yourRank = findViewById(R.id.yourRank);
                            TextView yourName = findViewById(R.id.yourName);
                            TextView yourRep = findViewById(R.id.yourRep);
                            yourRank.setText(String.valueOf(counter));
                            yourName.setText(node.doc.get("username",String.class));
                            yourRep.setText(repsAmt);
                        }
                        counter++;
                    }

                    // RecyclerView
                    RecyclerView leaderboard = findViewById(R.id.leaderboard);

                    RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardHolder> adapter
                            = new LeaderBoardAdapter(this, names, ranks, reps);
                    leaderboard.setAdapter( adapter );
                    leaderboard.setLayoutManager( new LinearLayoutManager(this));

//                    for (DocumentSnapshot doc : userArrList) {
//                        Log.d("uid:",doc.getId());
//                        if (doc.getId().equals(uid)) {
//                            currentUser = doc;
//                            break;
//                        }
//                    }
//
//                    if (currentUser != null) {
//                        TextView yourRank = findViewById(R.id.yourRank);
//                        yourRank.setText(String.valueOf(userArrList.indexOf(currentUser) + 1));
//
//                        TextView yourName = findViewById(R.id.yourName);
//                        TextView yourRep = findViewById(R.id.yourRep);
//                        yourName.setText(currentUser.get("username",String.class));
//                        Long currentUserReps = currentUser.get(classType+"AllTime", Long.class);
//                        yourRep.setText((currentUserReps != null) ? currentUserReps.toString() : "0");
//                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the exception
                });
    }



    //@Override
    //public void onBackPressed() {
        // Do nothing (disable back button functionality)
    //}
}

