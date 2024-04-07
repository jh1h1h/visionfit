package Team10_VisionFit.Backend.leaderboard;

import android.content.res.Resources;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

public class BST extends Node{

    public Node root; // Initialize BST root node
    public Hashtable<String, Node> nodes;
    private CollectionReference dbRef;
    private String classType;

    // Constructor
    public BST(CollectionReference dbRef, String classType) {
        this.root = null;
        this.nodes = new Hashtable<>();
        this.dbRef = dbRef;
        this.classType = classType;
    }

    public void populate_node(Node node){
        this.nodes.put(node.id, node);
        if (node.parent.equals("root")){
            this.root = node;
        }
    }

    public void inorder_traverse(Node root) {
        if (root != null) { // the tree does not exist
            inorder_traverse(nodes.get(root.left)); // traverse the left subtree first
            System.out.print(root.key + " ");
            inorder_traverse(nodes.get(root.right)); // traverse the right subtree next
        }
    }

    public void rebalance(){
        // TODO: rebalance tree since need AVL for optimal time complexity
    }

    // Insert a new node n at the subtree rooted at subtreeroot
    public Node tree_insert(Node node, Node subtreeroot, String parentid) {
        if (subtreeroot == null) { // tree is empty
            node.parent = parentid;
            this.nodes.put(node.id, node);
            dbRef.document(node.id).update(classType+"BSTparent",parentid);
            rebalance();
            return node;
        }
        // if tree not empty, traverse the tree to insert based on key value
//        Node subtreeroot = nodes.get(subtreerootid);
        if (node.key.compareTo(subtreeroot.key) < 0) {
            Node left;
            if (subtreeroot.left != null){
                left = nodes.get(subtreeroot.left);
            }else{
                left = null;
            }
            if (tree_insert(node, left, subtreeroot.id) != null){
                subtreeroot.left = node.id;
                dbRef.document(subtreeroot.id).update(classType+"BSTleft",node.id);
            }
        } else {
            Node right;
            if (subtreeroot.right != null){
                right = nodes.get(subtreeroot.right);
            }else{
                right = null;
            }
            if (tree_insert(node, right, subtreeroot.id) != null){
                subtreeroot.right = node.id;
                dbRef.document(subtreeroot.id).update(classType+"BSTright",node.id);
            }
        }
        return null;
    }

    // Insert a new node n at the subtree rooted at subtreeroot
    public void tree_delete(Node node, Node subtreeroot) throws Resources.NotFoundException {
        if (subtreeroot == null) { // tree is empty
            Log.d("ERROR","BST: Node to be deleted not found");
            throw new Resources.NotFoundException("BST: Node to be deleted not found");
        }
        if (subtreeroot.id.equals(node.id)){
            // Case 1: Node has no children or only one child
            if (subtreeroot.left == null) {
                if (nodes.get(subtreeroot.parent).left != null && nodes.get(subtreeroot.parent).left.equals(node.id)){
                    nodes.get(subtreeroot.parent).left = subtreeroot.right;
                    dbRef.document(subtreeroot.parent).update(classType+"BSTleft",subtreeroot.right);
                } else{
                    nodes.get(subtreeroot.parent).right = subtreeroot.right;
                    dbRef.document(subtreeroot.parent).update(classType+"BSTright",subtreeroot.right);
                }
                nodes.remove(subtreeroot.id);
                return;
            } else if (subtreeroot.right == null) {
                if (nodes.get(subtreeroot.parent).left.equals(node.id)){
                    nodes.get(subtreeroot.parent).left = subtreeroot.left;
                    dbRef.document(subtreeroot.parent).update(classType+"BSTleft",subtreeroot.left);
                } else{
                    nodes.get(subtreeroot.parent).right = subtreeroot.left;
                    dbRef.document(subtreeroot.parent).update(classType+"BSTright",subtreeroot.left);
                }
                nodes.remove(subtreeroot.id);
                return;
            }

            // Case 2: Node has two children
            // Find the inorder successor (minimum value in the right subtree)
            Node successor = tree_min(nodes.get(subtreeroot.right));
            tree_delete(successor, subtreeroot);
            Log.d("leaderboardsuccessor", String.valueOf(successor.key)); // TODO: make sure successor.key still exists after delete
            if (nodes.get(subtreeroot.parent).left.equals(subtreeroot.id)){
                nodes.get(subtreeroot.parent).left = successor.id;
                dbRef.document(subtreeroot.parent).update(classType+"BSTleft",successor.id);
            } else{
                nodes.get(subtreeroot.parent).right = successor.id;
                dbRef.document(subtreeroot.parent).update(classType+"BSTright",successor.id);
            }
            successor.left = subtreeroot.left;
            dbRef.document(successor.left).update(classType+"BSTright",subtreeroot.left);
            successor.right = subtreeroot.right;
            dbRef.document(successor.right).update(classType+"BSTright",subtreeroot.right);
            nodes.get(successor.parent).left = successor.right;
            dbRef.document(nodes.get(successor.parent).left).update(classType+"BSTright",successor.right);
            return;
        }
        // if tree not empty and node not found, traverse the tree to insert based on key value
        if (node.key.compareTo(subtreeroot.key) < 0) {
            tree_delete(node, nodes.get(subtreeroot.left));
        } else {
            tree_delete(node, nodes.get(subtreeroot.right));
        }
    }

    public Node tree_min(Node x) {
        while (x.left != null) {
            x = nodes.get(x.left); // update x to be left child of x
        }
        return x;
    }

    public Node tree_max(Node x) {
        while (x.right != null) {
            x = nodes.get(x.right); // update x to be right child of x
        }
        return x;
    }

//    public Node tree_search(Node x, int key) {
//        while (x != null && key != x.key) { // if node exist and key value is not key
//            if (key < x.key) { // check if k is less than key value
//                x = nodes.get(x.left); // if it is then continue to find left node
//            } else { // check if k is more than key value
//                x = nodes.get(x.right); // if it is then continue to find right node
//            }
//        }
//        return x;
//    }

    public Node successor(Node x) {
        if (x.right != null) { // check if right node exist
            return tree_min(nodes.get(x.right)); // if it does, find the last left node of that subtree
        }

        Node y = null;
        while (x != null) {
            if (nodes.get(x.parent).left.equals(x.id)) { // continue to loop as long as x is the y.right
                y = nodes.get(x.parent);
                break;
            }
            x = nodes.get(x.parent);
        }
        return y;
    }

    public Node predecessor(Node x) {
        if (x.left != null) { // check if left node exist
            return tree_max(nodes.get(x.left)); // if it does, find the last right node of that subtree
        }

        Node y = null;
        while (x != null) {
            if (nodes.get(x.parent).right.equals(x.id)) { // continue to loop as long as x is the y.right node
                y = nodes.get(x.parent);
                break;
            }
            x = nodes.get(x.parent);
        }
        return y;
    }
}
