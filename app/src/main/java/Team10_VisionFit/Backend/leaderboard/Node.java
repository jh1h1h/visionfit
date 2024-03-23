package Team10_VisionFit.Backend.leaderboard;

public class Node {
    int key;
    Node left;
    Node right;
    Node parent;

    // No-argument constructor
    public Node() {
        this.key = 0; // or any default value
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    // A utility function to create a new BST node
    Node(int key) {
        this.key = key;
        this.left = null;
        this.right = null;
        this.parent = null;
    }
}
