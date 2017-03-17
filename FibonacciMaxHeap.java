/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vagisha Tyagi
 */
public class FibonacciMaxHeap<H extends Comparable<H>> {
    private Node<H> root;
    private int size;

    public FibonacciMaxHeap() {   //Initializing the heap
        root = null;
        size = 0;
    }

    //Constructor 1: When node and size is given
    private FibonacciMaxHeap(Node<H> root, int size) {
        this.root = root;
        this.size = size;
    }

    //constructor 2: When node is given   
    private FibonacciMaxHeap(Node<H> node) {
        root = node;
        size = 1;
    }

    //Method 1 To find out the maximum value node of heap
    public Node<H> getMax() {
        return root;
    }

    //Method 2 To insert a node into max fibonacci heap
    public Node<H> insert(H key, String tag) {
        Node<H> node = new Node<H>(key, tag);
        // When root is null, make the node as root
        if (root == null) {
            root = node;
        } else {
            // Insert the node into the doubly linked list to the right of the root
            root.right.left = node;
            node.right = root.right;
            node.left = root;
            root.right = node;
            // Replace root with max value if appropriate
            if (node.compareTo(root) > 0) {
                root = node;
            }
        }

        return node;
    }

    //Method 3
    public void increaseKey(Node<H> node, H newKey) {
        if (newKey.compareTo(node.key) < 0) {
            throw new IllegalArgumentException("New key is smaller than old key.");
        }
        node.key = newKey;
        Node<H> parent = node.parent;
        //If new key of node becomes larger than its parent, remoce node from its parent as done in cut method
        if (parent != null && node.compareTo(parent) > 0) {
            cut(node, parent);
            // If we remove from parent, we will have to travel up through parents and cut all nodes marked as true. That is done in the cascadeCut method
            cascadingCut(parent);
        }
        //Accordingly set the root, if node's key is larger than root
        if (node.compareTo(root) > 0) {
            root = node;
        }
    }

    //Method 4
    private void cut(Node<H> node, Node<H> parent) {
        // If node has siblings, remove node from the siblings list
        if (node.right != node) {
            removeNodeFromList(node);
        }
        // We have to have the parent point to a different arbitrary child if this child
        // is the one pointed to by the parent
        if (parent.child == node) {
            /* If there are any other children, shift the child pointer of parent to next child */
            if (node.right != node) {
                parent.child = node.right;
            } else {
                // If node is the only child of its parent, clear the child pointer of parent
                parent.child = null;
            }
        }
        parent.degree--;             //Decrement the degree of parent
        merge(root, node);            //Join the removed children with the root list
        node.parent = null;
        node.childCut = false;
    }

    //Method 5
    private void cascadingCut(Node<H> node) {
        Node<H> parent = node.parent;
        if (parent != null) {
            //keep on cascading cut to parent if node is marked as true
            if (node.childCut) {
                cut(node, parent);
                cascadingCut(parent);
            } else {
                //when node marked as false is found, make it true
                node.childCut = true;
            }
        }
    }

    //Method 6
    public Node<H> extractMax() {
        if (root == null) {
            return null;
        }
        Node<H> ret_max = root;
        if (ret_max != null) {
            //If root has children, remove all the children from parent by setting parent pointer to null
            if (ret_max.child != null) {
                Node<H> child = ret_max.child;
                do {
                    child.parent = null;
                    child = child.right;          //keep on traversing the siblings list
                } while (child != ret_max.child);
            }
            Node<H> rightInRootList = root.right == root ? null : root.right;
            // Separate max from root list
            removeNodeFromList(ret_max);
            size--;
            // Merge the children of the max node and the right in root list together
            root = merge(rightInRootList, ret_max.child);
            // Make the root as right in root list if it exists and then continue combining same degree trees 
            //together until all different degrees trees are left
            if (rightInRootList != null) {
                root = rightInRootList;
                combineSameDegree();
            }
        }
        return ret_max;
    }

    //Method 7
    private void combineSameDegree() {
        //Taking array list to store
        List<Node<H>> list = new ArrayList<Node<H>>();
        Traverse<H> l = new Traverse<H>(root);   //To traverse along the list we have implemented iterator into traverse class
        while (l.hasNext()) {                    //From Traverse Class
            Node<H> current = l.next();
            while (list.size() <= current.degree + 1) {
                list.add(null);
            }
            // If there exists another node already having same degree, merge them
            while (list.get(current.degree) != null) {
                //we assume current to be the big element whose child will be small element
                //if current comes to be smaller than the another same degree element then swap both of them
                if (current.key.compareTo(list.get(current.degree).key) < 0) {
                    Node<H> temp = current;
                    current = list.get(current.degree);
                    list.set(current.degree, temp);
                }
                //now link the smaller same degree element as child of current element
                linkSmallToBig(list.get(current.degree), current);
                list.set(current.degree, null); //And set the node to null at current degree
                current.degree++;               //Increase degree of current by 1 
            }
            while (list.size() <= current.degree + 1) {
                list.add(null);
            }
            list.set(current.degree, current);
        }
        root = null;
        for (int y = 0; y < list.size(); y++) {
            if (list.get(y) != null) {
                // Remove siblings before merging
                list.get(y).right = list.get(y);
                list.get(y).left = list.get(y);
                root = merge(root, list.get(y));
            }
        }
    }

    //Method 8
    private void removeNodeFromList(Node<H> node) {
        // connect the left and right of removed node together
        node.left.right = node.right;
        node.right.left = node.left;
        // remove node completely from list by setting its left and right pointers to itself
        node.right = node;
        node.left = node;
    }

    //Method 9
    private void linkSmallToBig(Node<H> nodeA, Node<H> nodeB) {
        // remove small key node from the list
        removeNodeFromList(nodeA);
        // now merge smaller key node as child of bigger key node
        nodeB.child = merge(nodeA, nodeB.child);
        // accordingly set child's pointer to parent
        nodeA.parent = nodeB;
        nodeA.childCut = false;
    }

    //Method 10
    // Combines two lists  and returns the max node
    public <H extends Comparable<H>> Node<H> merge(
            Node<H> a, Node<H> b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        if (a == null && b == null) {
            return null;
        }
        // connecting the lists a and b
        Node<H> temp = a.right;
        a.right = b.right;
        a.right.left = a;
        b.right = temp;
        b.right.left = b;
        //the greater value is returned
        return a.compareTo(b) > 0 ? a : b;
    }
}
