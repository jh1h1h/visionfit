package Team10_VisionFit.Backend.leaderboard;

import android.content.res.Resources;
import android.util.Log;

public class BST extends Node{

    public Node root; // Initialize BST root node

    // Constructor
    public BST() {
        this.root = null;
    }

    public void inorder_traverse(Node root) {
        if (root != null) { // the tree does not exist
            inorder_traverse(root.left); // traverse the left subtree first
            System.out.print(root.key + " ");
            inorder_traverse(root.right); // traverse the right subtree next
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
        if (node.key < subtreeroot.key) {
            tree_insert(node, subtreeroot.left);
        } else {
            tree_insert(node, subtreeroot.right);
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
        // TODO: Code user equate function (probably by id)
        if (subtreeroot.user == node.user){
            // Case 1: Node has no children or only one child
            if (subtreeroot.left == null) {
                if (subtreeroot.parent.left == node){
                    subtreeroot.parent.left = subtreeroot.right;
                } else{
                    subtreeroot.parent.right = subtreeroot.right;
                }
                subtreeroot = null;
            } else if (subtreeroot.right == null) {
                if (subtreeroot.parent.left == node){
                    subtreeroot.parent.left = subtreeroot.left;
                } else{
                    subtreeroot.parent.right = subtreeroot.left;
                }
                subtreeroot = null;
            }

            // Case 2: Node has two children
            // Find the inorder successor (minimum value in the right subtree)
            Node successor = tree_min(subtreeroot.right);
            tree_delete(successor, subtreeroot);
            subtreeroot.key = successor.key;
            subtreeroot.user = successor.user;
            // TODO: asdfghjkl BST not done yet, i think there will be a problem we have to search by key and not node
            if (root.right != null) {
                root.right.parent = root; // Update parent reference
            }
            rebalance();
            // TODO: [Firebase] Upload node and update subtreeroot
        }
        // if tree not empty and node not found, traverse the tree to insert based on key value
        if (node.key < subtreeroot.key) {
            tree_delete(node, subtreeroot.left);
        } else {
            tree_delete(node, subtreeroot.right);
        }

//        if (root == null) { // Base case: tree is empty
//            return root;
//        }
//
//        // Find the node to delete recursively
//        if (key < root.key) {
//            root.left = tree_delete(root.left, key);
//            if (root.left != null) {
//                root.left.parent = root; // Update parent reference
//            }
//        } else if (key > root.key) {
//            root.right = tree_delete(root.right, key);
//            if (root.right != null) {
//                root.right.parent = root; // Update parent reference
//            }
//        } else { // Node to delete found
//            // Case 1: Node has no children or only one child
//            if (root.left == null) {
//                Node temp = root.right;
//                if (temp != null) {
//                    temp.parent = root.parent; // Update parent reference
//                }
//            } else if (root.right == null) {
//                Node temp = root.left;
//                if (temp != null) {
//                    temp.parent = root.parent; // Update parent reference
//                }
//                return temp;
//            }
//
//            // Case 2: Node has two children
//            // Find the inorder successor (minimum value in the right subtree)
//            Node successor = tree_min(root.right);
//            // Copy the successor's key to the current node
//            root.key = successor.key;
//            // Delete the inorder successor from the right subtree
//            root.right = tree_delete(root.right, successor.key);
//            if (root.right != null) {
//                root.right.parent = root; // Update parent reference
//            }
//        }
//        return root;
    }

    public Node tree_min(Node x) {
        while (x.left != null) {
            x = x.left; // update x to be left child of x
        }
        return x;
    }

    public Node tree_max(Node x) {
        while (x.right != null) {
            x = x.right; // update x to be right child of x
        }
        return x;
    }

    public Node tree_search(Node x, int key) {
        while (x != null && key != x.key) { // if node exist and key value is not key
            if (key < x.key) { // check if k is less than key value
                x = x.left; // if it is then continue to find left node
            } else { // check if k is more than key value
                x = x.right; // if it is then continue to find right node
            }
        }
        return x;
    }

    public Node successor(Node x) {
        if (x.right != null) { // check if right node exist
            return tree_min(x.right); // if it does, find the last left node of that subtree
        }

        Node y = null;
        while (x != null) {
            if (x == x.parent.left) { // continue to loop as long as x is the y.right
                y = x.parent;
                break;
            }
            x = x.parent;
        }
        return y;
    }

    public Node predecessor(Node x) {
        if (x.left != null) { // check if left node exist
            return tree_max(x.left); // if it does, find the last right node of that subtree
        }

        Node y = null;
        while (x != null) {
            if (x == x.parent.right) { // continue to loop as long as x is the y.right node
                y = x.parent;
                break;
            }
            x = x.parent;
        }
        return y;
    }
}
