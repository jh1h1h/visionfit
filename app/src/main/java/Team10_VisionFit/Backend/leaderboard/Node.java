package Team10_VisionFit.Backend.leaderboard;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import Team10_VisionFit.Backend.firebaseAuthentication.User;

public class Node{
    String key;
    String left;
    String right;
    String parent;
    String user;
    String id;
    int pts;

    // No-argument constructor
    public Node() {
        this.key = ""; // or any default value
        this.left = null;
        this.right = null;
        this.parent = null;
        this.user = null;
        this.id = null;
    }

    // A utility function to create a new BST node
    Node(int pts, String left, String right, String parent,String user, String id) {
        this.pts = pts;
        this.key = String.format("%010d",pts) + id; // Append with id to resolve tiebreaks
        this.left = left;
        this.right = right;
        this.parent = parent;
        this.user = user;
        this.id = id;
    }
}
