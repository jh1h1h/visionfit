package Team10_VisionFit.Backend.leaderboard;

import Team10_VisionFit.Backend.firebaseAuthentication.User;

public class Node {
    int key;
    Node left;
    Node right;
    Node parent;
    User user;

    // No-argument constructor
    public Node() {
        this.key = 0; // or any default value
        this.left = null;
        this.right = null;
        this.parent = null;
        this.user = null;
    }

    // A utility function to create a new BST node
    Node(int key, User user) {
        this.key = key;
        this.left = null;
        this.right = null;
        this.parent = null;
        this.user = user;
    }
}
