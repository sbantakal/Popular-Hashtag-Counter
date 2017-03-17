/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Vagisha Tyagi
 */
public class hashtagcounter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BufferedReader reader;
        BufferedWriter writer;
        String line;
        FibonacciMaxHeap<Integer> heap = new FibonacciMaxHeap<Integer>();
        HashMap<String, Node<Integer>> nodeHashMap = new HashMap<String, Node<Integer>>();
        Pattern stop = Pattern.compile("^stop$");
        Pattern hashf = Pattern.compile("^#([^ ]+) ([0-9]+)");
        Pattern query = Pattern.compile("^([0-9]+)");
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(args[0])));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output_file.txt")));
            while ((line = reader.readLine()) != null) {
                // First check for stop if it matches the stop pattern
                Matcher s = stop.matcher(line);
                if (s.matches()) {
                    break;
                } //check if it matches hash pattern
                else {
                    s = hashf.matcher(line);
                }

                if (s.matches()) {
                    // Increment count or add new entry to hashmap
                    int number = Integer.valueOf(s.group(2));
                    String tag = s.group(1);
                    //cheeck if the tag already exists 
                    if (nodeHashMap.containsKey(tag)) {
                        // take the corresponding value of tag
                        //  increment value with the count to already existing node
                        heap.increaseKey(nodeHashMap.get(tag), (nodeHashMap.get(tag).getKey() + number));

                    } else {
                        //if tag doesn't already exist in hashmap, insert new node with tag into heap
                        Node<Integer> newNode = heap.insert(number, tag);

                        //also insert into Hashmap with tag and new node
                        nodeHashMap.put(tag, newNode);
                    }

                } else {
                    s = query.matcher(line);
                    if (s.matches()) {
                        // Do the query
                        int n = Integer.valueOf(s.group(1));
                        // Keep track of all nodes removed with extractMax so we can reinsert new nodes of the same
                        // values after this query
                        Node<Integer>[] nodes = new Node[n];
                        for (int i = 0; i < n; ++i) {
                            nodes[i] = heap.extractMax();
                            writer.write(nodes[i].getTag());
                            if (i < n - 1) {
                                writer.write(",");
                            }
                        }
                        writer.write("\n");

                        // Reinsert the extracted into hashmap
                        for (int i = 0; i < n; ++i) {
                            nodeHashMap.replace(nodes[i].getTag(), heap.insert((Integer) nodes[i].getKey(), nodes[i].getTag()));
                        }
                    }
                }
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("IOException encountered\n\n" + e.toString());
        }
    }

}
