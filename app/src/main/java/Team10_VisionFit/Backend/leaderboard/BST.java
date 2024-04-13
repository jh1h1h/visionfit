package Team10_VisionFit.Backend.leaderboard;

import android.content.res.Resources;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

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
        if (node.getParent().equals("root")){
            this.root = node;
        }
    }

    public ArrayList<Node> inorder_path(Node root){
        ArrayList<Node> path = new ArrayList<>();
        inorder_traverse_desc(root, path);
        return path;
    }

    public void inorder_traverse_desc(Node root, ArrayList<Node> path) {
        if (root != null) { // the tree does not exist
            if (root.getRight() != null){
                inorder_traverse_desc(nodes.get(root.getRight()), path); // traverse the left subtree first
            }
            path.add(root);
            if (root.getLeft() != null){
                inorder_traverse_desc(nodes.get(root.getLeft()), path); // traverse the right subtree next
            }
        }
    }

    public void rebalance(){
        // TODO: rebalance tree since need AVL for optimal time complexity
    }

    // Insert a new node n at the subtree rooted at subtreeroot
    public Node tree_insert(Node node, Node subtreeroot, String parentid) {
        if (subtreeroot == null) { // tree is empty
            node.setParent(parentid, dbRef, classType, this);
            this.nodes.put(node.id, node);
            rebalance();
            return node;
        }
        // if tree not empty, traverse the tree to insert based on key value
//        Node subtreeroot = nodes.get(subtreerootid);
        if (node.key.compareTo(subtreeroot.key) < 0) {
            Node left;
            if (subtreeroot.getLeft() != null){
                left = nodes.get(subtreeroot.getLeft());
            }else{
                left = null;
            }
            if (tree_insert(node, left, subtreeroot.id) != null){
                subtreeroot.setLeft(node.id, dbRef, classType);
            }
        } else {
            Node right;
            if (subtreeroot.getRight() != null){
                right = nodes.get(subtreeroot.getRight());
            }else{
                right = null;
            }
            if (tree_insert(node, right, subtreeroot.id) != null){
                subtreeroot.setRight(node.id, dbRef, classType);
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
            if (subtreeroot.getLeft() == null) {
                if(!Objects.equals(subtreeroot.getParent(), "root")){
                    if (nodes.get(subtreeroot.getParent()).getLeft() != null && nodes.get(subtreeroot.getParent()).getLeft().equals(node.id)){
                        nodes.get(subtreeroot.getParent()).setLeft(subtreeroot.getRight(), dbRef, classType);
                    } else{
                        nodes.get(subtreeroot.getParent()).setRight(subtreeroot.getRight(), dbRef, classType);
                    }
                }
                if (subtreeroot.getRight() != null){
                    nodes.get(subtreeroot.getRight()).setParent(subtreeroot.getParent(), dbRef, classType, this);
                }
            } else if (subtreeroot.getRight() == null) {
                if (!Objects.equals(subtreeroot.getParent(), "root")){
                    if (nodes.get(subtreeroot.getParent()).getLeft().equals(node.id)){
                        nodes.get(subtreeroot.getParent()).setLeft(subtreeroot.getLeft(), dbRef, classType);
                    } else{
                        nodes.get(subtreeroot.getParent()).setRight(subtreeroot.getLeft(), dbRef, classType);
                    }
                }
                nodes.get(subtreeroot.getLeft()).setParent(subtreeroot.getParent(), dbRef, classType, this);
            } else{
                // Case 2: Node has two children
                // Find the inorder successor (minimum value in the right subtree)
                Node successor = tree_min(nodes.get(subtreeroot.getRight()));
                tree_delete(successor, subtreeroot);
                this.nodes.put(successor.id, successor);
                if (!Objects.equals(subtreeroot.getParent(), "root")){
                    if (nodes.get(subtreeroot.getParent()).getLeft().equals(subtreeroot.id)){
                        nodes.get(subtreeroot.getParent()).setLeft(successor.id, dbRef, classType);
                    } else{
                        nodes.get(subtreeroot.getParent()).setRight(successor.id, dbRef, classType);
                    }
                }
                successor.setParent(subtreeroot.getParent(), dbRef, classType, this);
                successor.setLeft(subtreeroot.getLeft(), dbRef, classType);
                successor.setRight(subtreeroot.getRight(), dbRef, classType);
                if (!Objects.equals(successor.getParent(), "root")){
                    nodes.get(successor.getParent()).setLeft(successor.getRight(), dbRef, classType);
                }
                if (successor.getRight() != null){
                    nodes.get(successor.getRight()).setParent(successor.getParent(), dbRef, classType, this);
                }
            }
            subtreeroot.delete(dbRef, classType, this);
        }
        // if tree not empty and node not found, traverse the tree to insert based on key value
        else {
            if (node.key.compareTo(subtreeroot.key) < 0) {
                tree_delete(node, nodes.get(subtreeroot.getLeft()));
            } else {
                tree_delete(node, nodes.get(subtreeroot.getRight()));
            }
        }
    }

    public Node tree_min(Node x) {
        while (x.getLeft() != null) {
            x = nodes.get(x.getLeft()); // update x to be left child of x
        }
        return x;
    }

    public Node tree_max(Node x) {
        while (x.getRight() != null) {
            x = nodes.get(x.getRight()); // update x to be right child of x
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
        if (x.getRight() != null) { // check if right node exist
            return tree_min(nodes.get(x.getRight())); // if it does, find the last left node of that subtree
        }

        Node y = null;
        while (x != null) {
            if (nodes.get(x.getParent()).getLeft().equals(x.id)) { // continue to loop as long as x is the y.right
                y = nodes.get(x.getParent());
                break;
            }
            x = nodes.get(x.getParent());
        }
        return y;
    }

    public Node predecessor(Node x) {
        if (x.getLeft() != null) { // check if left node exist
            return tree_max(nodes.get(x.getLeft())); // if it does, find the last right node of that subtree
        }

        Node y = null;
        while (x != null) {
            if (nodes.get(x.getParent()).getRight().equals(x.id)) { // continue to loop as long as x is the y.right node
                y = nodes.get(x.getParent());
                break;
            }
            x = nodes.get(x.getParent());
        }
        return y;
    }
}
