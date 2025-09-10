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
    public void loadLeaderboard(String classType, String lbType) {
        TextView displayText = findViewById(R.id.displaytext);
        displayText.setText(lbType.equals("AllTime") ? "All Time Leaders" : "Daily Leaders");

        // Pick the points field based on class type + timeframe
        final String pointsField = classType + (lbType.equals("AllTime") ? "AllTime" : "Today");

        final ArrayList<String> names = new ArrayList<>();
        final ArrayList<String> ranks = new ArrayList<>();
        final ArrayList<String> reps  = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference users = db.collection("users");

        // 1) Fetch all visible users (no orderBy needed; BST will sort)
        users.whereEqualTo("showRanking", true)
                .get()
                .addOnSuccessListener(qsnapshot -> {
                    // 2) Build BST in-memory from scratch
                    BST lbBST = new BST(users, classType);
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DocumentSnapshot currentUser = null;
                    long nowTs = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                            ? java.time.Instant.now().getEpochSecond() : System.currentTimeMillis()/1000L;

                    for (DocumentSnapshot doc : qsnapshot.getDocuments()) {
                        // guard: only consider visible users (already filtered), skip null
                        if (doc == null) continue;

                        // Use the selected points field (today/all-time)
                        Long pts = doc.getLong(pointsField);
                        long val = (pts != null) ? pts : 0L;

                        // Build a Node and insert into BST
                        // Use the simple constructor: (points, id, doc, timestamp)
                        Node node = new Node(val, doc.getId(), doc, nowTs);
                        lbBST.tree_insert(node, lbBST.root, "root");

                        if (doc.getId().equals(uid)) currentUser = doc;
                    }

                    // (Optional) If you still want to update your own timestamp field:
                    if (currentUser != null && lbType.equals("AllTime")) {
                        users.document(uid).update(classType + "AllTimeTimestamp", nowTs);
                    }

                    // 3) Traverse BST to get a sorted list
                    ArrayList<Node> path = lbBST.inorder_path(lbBST.root);
//                    java.util.Collections.reverse(path); // highest first

                    // 4) Build UI arrays
                    int rank = 1;
                    for (Node n : path) {
                        Long ptsL = n.getPoints();
                        String repsAmt = (ptsL != null) ? String.valueOf(ptsL) : "0";
                        String name = n.doc.getString("username");
                        if (name == null) name = "(anonymous)";

                        names.add(name);
                        reps.add(repsAmt);
                        ranks.add(String.valueOf(rank));

                        if (n.id.equals(uid)) {
                            ((TextView) findViewById(R.id.yourRank)).setText(String.valueOf(rank));
                            ((TextView) findViewById(R.id.yourName)).setText(name);
                            ((TextView) findViewById(R.id.yourRep)).setText(repsAmt);
                        }
                        rank++;
                    }

                    // 5) Bind RecyclerView
                    RecyclerView leaderboard = findViewById(R.id.leaderboard);
                    RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardHolder> adapter =
                            new LeaderBoardAdapter(this, names, ranks, reps);
                    leaderboard.setAdapter(adapter);
                    leaderboard.setLayoutManager(new LinearLayoutManager(this));
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();

                });
    }

    //@Override
    //public void onBackPressed() {
        // Do nothing (disable back button functionality)
    //}
}

