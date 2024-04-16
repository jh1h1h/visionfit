package Team10_VisionFit.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamten.visionfit.R;

import java.time.Instant;
import java.util.ArrayList;

import Team10_VisionFit.Backend.leaderboard.BST;
import Team10_VisionFit.Backend.leaderboard.Node;

public class PrivacyPolicyActivity extends BaseActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String classType = "squat"; // "pushup" or "squat"
        int repCount = 1;
//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //Get the current logged in User's ID
        String uid = "2KYSIeejD6NpI0Jf3JrZDIVBoR73";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        //To setup nav bar
        setUpBottomNavBar(R.id.bottom_settings);

        ConstraintLayout privacyPolicyTab = findViewById(R.id.PrivacyPolicyTab);

        privacyPolicyTab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                long dateTime = 10000000000L;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Instant instant = Instant.now();
                    dateTime = instant.getEpochSecond();
                }
                else{
                    Log.d("ERROR","Version incompatible for class Instant. android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O");
                }
                long dateTime1 = dateTime;
                CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");
                usersRef.get().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        long dateTime2 = dateTime1;
                        QuerySnapshot lb = task1.getResult();
                        BST lbBST = new BST(usersRef, classType);
                        ArrayList<DocumentSnapshot> lbList = new ArrayList<>(lb.getDocuments());
                        DocumentSnapshot currentUser = null; long prevAllTime = 0L; long prevTimeStamp = 10000000000L;
                        for (DocumentSnapshot lbNode: lbList) {
                            if (lbNode.get(classType+"BSTparent",String.class) != null){
                                    lbBST.populate_node(new Node(
                                            lbNode.getLong(classType + "AllTime"),
                                            lbNode.get(classType+"BSTleft",String.class),
                                            lbNode.get(classType+"BSTright",String.class),
                                            lbNode.get(classType+"BSTparent",String.class),
                                            lbNode.getId(),
                                            lbNode,
                                            lbNode.getLong(classType + "AllTimeTimestamp")
                                    ));
                                if (lbNode.getId().equals(uid)){
                                    prevAllTime = lbNode.getLong(classType + "AllTime");
                                    prevTimeStamp = lbNode.getLong(classType + "AllTimeTimestamp");
                                }
                            }else if (lbNode.getId().equals(uid)){
                                currentUser = lbNode;
                            }
                            if (lbNode.getId().equals(uid)){
                                DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(uid);
                                userRef.update(classType + "AllTime", repCount);
                                userRef.update(classType + "AllTimeTimestamp", dateTime2);
                            }
                        }

                        // add workout to client BST and upload to firebase
                        // TODO: potential bug: document has outdated count and gets reflected onto node document
                        if (currentUser != null){
                            lbBST.tree_insert(new Node((long) prevAllTime, uid, currentUser, dateTime2),lbBST.root,"root");
                        }
                        lbBST.tree_delete(new Node(prevAllTime, uid, null, prevTimeStamp),lbBST.root);
                        lbBST.tree_insert(new Node((long) repCount, uid, null, dateTime2),lbBST.root,"root");
                    }
                });
            }
        });
    }

    //@Override
    //public void onBackPressed() {
        // Do nothing (disable back button functionality)
    //}
}
