/** 
 * written by Bob Boothe
 * DO NOT MODIFY
 */
package student;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;
/**
 * RALtester3.java
 * written by Bob Boothe
 * DO NOT MODIFY
 * testing code for finished Ragged Array List
 * There is a default test case of a-g.
 * You can also specify arguments on the command line that will be
 * processed as a sequence of characters to insert into the list.
 * 
 * DO NOT MODIFY I WILL BE USING THIS FOR MY TESTING
 * @author bboothe
 */
public class RALtester3 {
    /**
     * 
     * @param args
     * @throws FileNotFoundException 
     */

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("testing routine for RaggedArrayList");
        String order = "qwertyuiopasdfghjklzxcvbnmaeiou";  // default test

        // insert them character by character into the list
        System.out.println("insertion order: " + order);
        Comparator<String> comp = new StringCmp();
        // reset the counter inside the comparator
        ((CmpCnt) comp).resetCmpCnt();
        RaggedArrayList<String> ralist = new RaggedArrayList<String>(comp);
        for (int i = 0; i < order.length(); i++) {
            String s = order.substring(i, i + 1);
            ralist.add(s);
        }
        System.out.println(
                "The number of comparison to build the RaggedArrayList = "
                + ((CmpCnt) comp).getCmpCnt());

        System.out.println("TEST: after adds - data structure dump");
        dump(ralist);
        stats(ralist, comp);

        System.out.println("TEST: contains(\"c\") ->" + ralist.contains("c"));
        System.out.println("TEST: contains(\"7\") ->" + ralist.contains("7"));

        System.out.println("TEST: toArray");
        String[] a = new String[ralist.size()];
        ralist.toArray(a);
        for (int i = 0; i < a.length; i++) {
            System.out.print("[" + a[i] + "]");
        }
        System.out.println();

        System.out.println("TEST: iterator");
        Iterator<String> itr = ralist.iterator();
        while (itr.hasNext()) {
            System.out.print("[" + itr.next() + "]");
        }
        System.out.println();

        System.out.println("TEST: sublist(e,o)");
        RaggedArrayList<String> sublist = ralist.subList("e", "o");
        dump(sublist);
    }

    /**
     * string comparator with cmpCnt for testing
     */
    public static class StringCmp extends CmpCnt 
                                  implements Comparator<String> {

        public int compare(String s1, String s2) {
            cmpCnt++;
            return s1.compareTo(s2);
        }
    }

    /**
     * print out an organized display of the list intended for testing
     * purposes on small examples it looks nice for the test case where 
     * the objects are characters
     *
     * DO NOT MODIFY I WILL BE USING THIS FOR MY TESTING
     */
    public static void dump(RaggedArrayList<String> ralist) {
        for (int i1 = 0; i1 < ralist.l1Array.length; i1++) {
            RaggedArrayList<String>.L2Array l2array
                    = (RaggedArrayList<String>.L2Array) (ralist.l1Array[i1]);
            System.out.print("[" + i1 + "] -> ");
            if (l2array == null) {
                System.out.println("null");
            } else {
                // can't seem to cast to array of strings
                Object[] items = (Object[]) (l2array.items);
                int len = items.length;
                for (int i2 = 0; i2 < len; i2++) {
                    String item = (String) (items[i2]);
                    if (item == null) {
                        System.out.print("[ ]");
                    } else {
                        System.out.print("[" + item + "]");
                    }
                }
                //System.out.printf("  (%d of %d) used\n\n", 
                //	l2array.numUsed,l2array.items.length );
                
            }
            System.out.println();
        }
    }

    /**
     * calculate and display statistics
     *
     * It use a comparator that implements the given CmpCnt interface. It then
     * runs through the list searching for every item and calculating search
     * statistics.
     *
     * DO NOT MODIFY I WILL BE USING THIS FOR MY TESTING
     */
    public static void stats(RaggedArrayList<String> ralist,
            Comparator<String> comp) {
        System.out.println("STATS:");
        int size = ralist.size();
        System.out.println("list size N = " + size);

        // level 1 array
        int l1NumUsed = ralist.l1NumUsed;
        System.out.println("level 1 array " + l1NumUsed + " of "
                + ralist.l1Array.length + " used.");

        // level 2 arrays
        int minL2size = Integer.MAX_VALUE, maxL2size = 0;
        for (int i1 = 0; i1 < ralist.l1NumUsed; i1++) {
            RaggedArrayList<String>.L2Array l2array
                    = (RaggedArrayList<String>.L2Array) (ralist.l1Array[i1]);
            minL2size = Math.min(minL2size, l2array.numUsed);
            maxL2size = Math.max(maxL2size, l2array.numUsed);
        }
        System.out.printf("level 2 array sizes: min = %d used, avg = %.1f "
                + "used, max = %d used\n",
                minL2size, (double) size / l1NumUsed, maxL2size);

        // search stats, search for every item
        int totalCmps = 0, minCmps = Integer.MAX_VALUE, maxCmps = 0;
        Iterator<String> itr = ralist.iterator();
        while (itr.hasNext()) {
            String obj = itr.next();
            ((CmpCnt) comp).resetCmpCnt();
            if (!ralist.contains(obj)) {
                System.err.println("Did not expect an unsuccesful search in stats");
            }
            int cnt = ((CmpCnt) comp).getCmpCnt();
            totalCmps += cnt;
            if (cnt > maxCmps) {
                maxCmps = cnt;
            }
            if (cnt < minCmps) {
                minCmps = cnt;
            }
        }
        System.out.printf("Successful search: min cmps = %d, avg cmps = %.1f, "
                + "max cmps %d\n",
                minCmps, (double) totalCmps / size, maxCmps);
    }

}
