package Team10_VisionFit.Backend.leaderboard;

import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

public class BST extends Node{

    public Node root; // Initialize BST root node
    public Hashtable<String, Node> nodes;

    // Constructor
    public BST() {
        this.root = null;
        this.nodes = new Hashtable<>();
    }

    public void populate_node(Node node){
        this.nodes.put(node.id, node);
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
    public void tree_insert(Node node, Node subtreeroot) {
        if (subtreeroot == null) { // tree is empty
            subtreeroot = node;
        }
        // if tree not empty, traverse the tree to insert based on key value
        if (node.key.compareTo(subtreeroot.key) < 0) {
            tree_insert(node, nodes.get(subtreeroot.left));
        } else {
            tree_insert(node, nodes.get(subtreeroot.right));
        }
        rebalance();
        // TODO: [Firebase] Upload node and update subtreeroot
    }

    // Insert a new node n at the subtree rooted at subtreeroot
    public void tree_delete(Node node, Node subtreeroot) throws Resources.NotFoundException {
        if (subtreeroot == null) { // tree is empty
            Log.d("ERROR","BST: Node to be deleted not found");
            throw new Resources.NotFoundException("BST: Node to be deleted not found");
        }
        if (subtreeroot.userid.equals(node.userid)){
            // Case 1: Node has no children or only one child
            if (subtreeroot.left == null) {
                if (nodes.get(subtreeroot.parent).left.equals(node.id)){
                    nodes.get(subtreeroot.parent).left = subtreeroot.right;
                } else{
                    nodes.get(subtreeroot.parent).right = subtreeroot.right;
                }
                nodes.remove(subtreeroot.id);
                // TODO: Update subtreeroot.parent on firestore and delete subtreeroot node
            } else if (subtreeroot.right == null) {
                if (nodes.get(subtreeroot.parent).left.equals(node.id)){
                    nodes.get(subtreeroot.parent).left = subtreeroot.left;
                } else{
                    nodes.get(subtreeroot.parent).right = subtreeroot.left;
                }
                nodes.remove(subtreeroot.id);
                // TODO: Update subtreeroot.parent on firestore and delete subtreeroot node
            }

            // Case 2: Node has two children
            // Find the inorder successor (minimum value in the right subtree)
            Node successor = tree_min(nodes.get(subtreeroot.right));
            tree_delete(successor, subtreeroot);
            Log.d("leaderboardsuccessor", String.valueOf(successor.key)); // TODO: make sure successor.key still exists after delete
            subtreeroot.key = successor.key;
            subtreeroot.userid = successor.userid;
            nodes.get(successor.parent).left = successor.right;
            // TODO: [Firebase] Upload node and update subtreeroot and parent
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
