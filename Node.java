/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Vagisha Tyagi
 */
public class Node<H extends Comparable<H>>
        implements Comparable<Node<H>> {
    public H key;
    public String tagName;
    public int degree;
    public Node<H> parent;
    public Node<H> child;
    public Node<H> left;
    public Node<H> right;
    public boolean childCut;
    public boolean isMax;

    public Node() {
        key = null;
    }

    public Node(H key, String tag) {
        this.key = key;
        this.tagName = tag;
        right = this;
        left = this;
    }

    public H getKey() {
        return key;
    }

    public String getTag() {
        return tagName;
    }

    public int compareTo(Node<H> other) {
        return this.key.compareTo(other.key);
    }
}
