package Team10_VisionFit.UI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;

import Team10_VisionFit.Backend.leaderboard.BST;
import Team10_VisionFit.Backend.leaderboard.Node;

public class LeaderBoardActivity extends BaseActivity {
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
                    String classType = "squat";
                    BST lbBST = new BST(users, classType);
                    ArrayList<DocumentSnapshot> userArrList = new ArrayList<>(qsnapshot.getDocuments());
                    DocumentSnapshot currentUser = null;

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
                                        userDoc
                                ));
                            }
                        }
//                        else {
//                            name.setText("");
//                            reps.setText("");
//                        }


                    }

                    ArrayList<Node> path = lbBST.inorder_path(lbBST.root);
                    for (Node node: path){
                        Long repsLong = node.getPoints();
                        String repsAmt = (repsLong != null) ? repsLong.toString() : "0";
                        names.add(node.doc.get("username",String.class));
                        reps.add(repsAmt);
                        ranks.add("abc");
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

