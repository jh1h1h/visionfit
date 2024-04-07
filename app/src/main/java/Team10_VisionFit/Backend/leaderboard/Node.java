package Team10_VisionFit.Backend.leaderboard;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import Team10_VisionFit.Backend.firebaseAuthentication.User;

public class Node{
    String key;
    String left;
    String right;
    String parent;
    public String id;
    Long points;

    // No-argument constructor
    public Node() {
        this.points = 0L;
        this.key = ""; // or any default value
        this.left = null;
        this.right = null;
        this.parent = null;
        this.id = null;
    }

    // A utility function to create a new BST node
    public Node(Long points, String left, String right, String parent, String id) {
        this.points = points;
        this.key = String.format("%010d",points) + id; // Append with id to resolve tiebreaks
        this.left = left;
        this.right = right;
        this.parent = parent;
        this.id = id;
    }

    public Node(Long points, String id){
        this.points = points;
        this.key = String.format("%010d",points) + id; // Append with id to resolve tiebreaks
        this.left = null;
        this.right = null;
        this.parent = null;
        this.id = id;
    }
}
