package Team10_VisionFit.Backend.leaderboard;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import Team10_VisionFit.Backend.firebaseAuthentication.User;

public class Node{
    String key;
    private String left;
    private String right;
    private String parent;
    public String id;
    Long points;
    public DocumentSnapshot doc;

    // No-argument constructor
    public Node() {
        this.points = 0L;
        this.key = ""; // or any default value
        this.left = null;
        this.right = null;
        this.parent = null;
        this.id = null;
        this.doc = null;
    }

    // A utility function to create a new BST node
    public Node(Long points, String left, String right, String parent, String id, DocumentSnapshot doc) {
        this.points = points;
        this.key = String.format("%010d",points) + id; // Append with id to resolve tiebreaks
        this.left = left;
        this.right = right;
        this.parent = parent;
        this.id = id;
        this.doc = doc;
    }

    public Node(Long points, String id, DocumentSnapshot doc){
        this.points = points;
        this.key = String.format("%010d",points) + id; // Append with id to resolve tiebreaks
        this.left = null;
        this.right = null;
        this.parent = null;
        this.id = id;
        this.doc = doc;
    }

    public Long getPoints() {
        return points;
    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }

    public String getParent() {
        return parent;
    }

    public void setLeft(String left, CollectionReference dbRef, String classType) {
        Log.d("cyril",this.key+".left: from "+this.left+" to "+left);
        this.left = left;
        dbRef.document(this.id).update(classType+"BSTleft",left);
    }

    public void setRight(String right, CollectionReference dbRef, String classType) {
        Log.d("cyril",this.key+".right: from "+this.right+" to "+right);
        this.right = right;
        dbRef.document(this.id).update(classType+"BSTright",right);
    }

    public void setParent(String parent, CollectionReference dbRef, String classType, BST bst) {
        Log.d("cyril",this.key+".parent: from "+this.parent+" to "+parent);
        this.parent = parent;
        if (parent.equals("root")){
            bst.root = this;
        }
        dbRef.document(this.id).update(classType+"BSTparent",parent);
    }

    public void delete(CollectionReference dbRef, String classType, BST bst) {
        Log.d("cyril",key+" has been deleted.");
        bst.nodes.remove(id);
        if(bst.root.id.equals(id)){
            bst.root = null;
        }
        dbRef.document(id).update(classType+"BSTparent",null);
        dbRef.document(id).update(classType+"BSTleft",null);
        dbRef.document(id).update(classType+"BSTright",null);
    }
}
