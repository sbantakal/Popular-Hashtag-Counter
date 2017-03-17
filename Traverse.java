/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Vagisha Tyagi
 */
//this will be used in combineSameDegree() method by collecting nodes in list in 
//the constructor as during pairwise join nodes will not remain same.
public class Traverse<H extends Comparable<H>> implements Iterator<Node<H>> {

    private Queue<Node<H>> elements = new LinkedList<Node<H>>();

    public Traverse(Node<H> start) {
        if (start == null) {
            return;
        }
        Node<H> current = start;
        do {
            //add current node to queue and proceed to its right, iterate till start node appears
            elements.add(current);
            current = current.right;
        } while (start != current);
    }

    public boolean hasNext() {
        //iterator functions used
        return elements.peek() != null;
    }

    public Node<H> next() {
        //iterator functions used
        return elements.poll();
    }

}
