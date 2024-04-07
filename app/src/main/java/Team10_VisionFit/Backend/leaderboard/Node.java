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
    Node(int key, String left, String right, String parent,String user, String id) {
        this.key = String.format("%010d",key) + id; // Append with id to resolve tiebreaks
        this.left = left;
        this.right = right;
        this.parent = parent;
        this.user = user;
        this.id = id;
    }
}
