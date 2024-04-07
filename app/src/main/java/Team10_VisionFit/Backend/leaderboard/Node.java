package Team10_VisionFit.Backend.leaderboard;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import Team10_VisionFit.Backend.firebaseAuthentication.User;

public class Node{
    String key;
    String left;
    String right;
    String parent;
    String userid;
    String id;
    int points;

    // No-argument constructor
    public Node() {
        this.key = ""; // or any default value
        this.left = null;
        this.right = null;
        this.parent = null;
        this.userid = null;
        this.id = null;
    }

    // A utility function to create a new BST node
    public Node(int points, String left, String right, String parent,String userid, String id) {
        this.points = points;
        this.key = String.format("%010d",points) + userid; // Append with id to resolve tiebreaks
        this.left = left;
        this.right = right;
        this.parent = parent;
        this.userid = userid;
        this.id = id;
    }
}
