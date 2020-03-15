/** *************************************************************************
 * Revision History (newest first)
 ***************************************************************************
 * 02/18/2020 - Tom Bahun and Ethan Ray started methods
 * 02/16/2020 - Tom Bahun and Ethan Ray started part 5
 * 02/13/20 - Tom Bahun and Jonathan Koenig finished add() method
 * 02/12/20 - Tom Bahun and Jonathan Koenig continued working on method
 * 02/11/20 - Tom Bahun and Jonathan Koenig worked on add() method
 * 02/08/20 - Tom Bahun added comments, documentation, and reviewed for errors
 * 02/07/20 - Tiffany Hoeung - modified findFront to check the last element
 * 02/06/20 - Mary Isabelle Wisell - added findEnd method
 * 02/02/20 - Tiffany Hoeung started the findFront method
 * 2016 Anne Applin formatting and JavaDoc added
 * 2015 Starting code by Prof. Boothe
 ************************************************************************* */
package student;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**
 * The RaggedArrayList is a 2 level data structure that is an array of arrays.
 *
 * It keeps the items in sorted order according to the comparator.<br />
 * Duplicates are allowed.<br />
 * New items are added after any equivalent items.<br />
 *
 * NOTE: normally fields, internal nested classes and non API methods should all
 * be private, however they have been made public so that the tester code can
 * set them
 * @author Tom Bahun
 * @param <E> A generic data type so that this structure can be built with any
 * data type (Object)  *
 */
public class RaggedArrayList<E> implements Iterable<E> {

    // must be even so when split get two equal pieces
    private static final int MINIMUM_SIZE = 4;
    /**
     * The total number of elements in the entire RaggedArrayList
     */
    public int size;
    /**
     * really is an array of L2Array, but compiler won't let me cast to that
     */
    public Object[] l1Array;
    /**
     * The number of elements in the l1Array that are used.
     */
    public int l1NumUsed;
    /**
     * a Comparator object so we can use compare for Song
     */
    private Comparator<E> comp;

    /**
     * create an empty list always have at least 1 second level array even if
     * empty, makes code easier (DONE - do not change)
     *
     * @param c a comparator object
     */
    public RaggedArrayList(Comparator<E> c) {
        size = 0;
        // you can't create an array of a generic type
        l1Array = new Object[MINIMUM_SIZE];
        // first 2nd level array
        l1Array[0] = new L2Array(MINIMUM_SIZE);
        l1NumUsed = 1;
        comp = c;
    }

    /**
     * ***********************************************************
     * nested class for 2nd level arrays (DONE - do not change)
     */
    public class L2Array {

        /**
         * the array of items
         */
        public E[] items;
        /**
         * number of items in this L2Array with values
         */
        public int numUsed;

        /**
         * Constructor for the L2Array
         *
         * @param capacity the initial length of the array
         */
        public L2Array(int capacity) {
            // you can't create an array of a generic type
            items = (E[]) new Object[capacity];
            numUsed = 0;
        }
    }// end of nested class L2Array

    /**
     * ***********************************************************
     */
    /**
     * total size (number of entries) in the entire data structure (DONE - do
     * not change)
     *
     * @return total size of the data structure
     */
    public int size() {
        return size;
    }

    /**
     * null out all references so garbage collector can grab them but keep
     * otherwise empty l1Array and 1st L2Array (DONE - Do not change)
     */
    public void clear() {
        size = 0;
        // clear all but first l2 array
        Arrays.fill(l1Array, 1, l1Array.length, null);
        l1NumUsed = 1;
        L2Array l2Array = (L2Array) l1Array[0];
        // clear out l2array
        Arrays.fill(l2Array.items, 0, l2Array.numUsed, null);
        l2Array.numUsed = 0;
    }

    /**
     * *********************************************************
     * nested class for a list position used only internally 2 parts: level 1
     * index and level 2 index
     */
    public class ListLoc {

        /**
         * Level 1 index
         */
        public int level1Index;

        /**
         * Level 2 index
         */
        public int level2Index;

        /**
         * Parameterized constructor
         *
         * @param level1Index input value for property
         * @param level2Index input value for property
         */
        public ListLoc(int level1Index, int level2Index) {
            this.level1Index = level1Index;
            this.level2Index = level2Index;
        }

        /**
         * test if two ListLoc's are to the same location (done -- do not
         * change)
         * @author Tom Bahun
         * @param otherObj
         * @return true/false:
         * whether the ListLoc's are in the same location or not
         */
        public boolean equals(Object otherObj) {
            // not really needed since it will be ListLoc
            if (getClass() != otherObj.getClass()) {
                return false;
            }
            ListLoc other = (ListLoc) otherObj;

            return level1Index == other.level1Index
                    && level2Index == other.level2Index;
        }

        /**
         * move ListLoc to next entry when it moves past the very last entry it
         * will be 1 index past the last value in the used level 2 array can be
         * used internally to scan through the array for sublist also can be
         * used to implement the iterator
         * 
         * @author tombahun & ethanray
         */
        public void moveToNext() {           
            L2Array l2Array = (L2Array) l1Array[this.level1Index];
            if(l2Array.items[this.level2Index+1] == null) {
              this.level1Index++;
              this.level2Index = 0; // reset to 0, as we're in new array
            } else {
              this.level2Index++;
            } 
        }
    }

    /**
     * find 1st matching entry
     * @author Tom Bahun
     * @param item we are searching for a place to put.
     * @return ListLoc of 1st matching item or of 1st item greater than the item
     * if no match this might be an unused slot at the end of a level 2 array
     */
    public ListLoc findFront(E item) {
        // our current spots in the ragged array
        int level1index = 0;
        int level2index = 0;
        // Our first array to look through
        L2Array l2Array = (L2Array) l1Array[level1index];
        // test to see if array is null
        if(l2Array.items[level2index] == null){
            return new ListLoc(level1index, level2index);
        }
        
        // while we are within bounds, next array is not null, and the item
        // is greater than the first item of the l2array, increase index
        while(l1Array != null && level1index < l1Array.length &&
                l2Array != null &&
                // Changed the comparison so that it checks the last
                //  element
                // comp.compare(item, l2Array.items[level2index]) > 0) {
                comp.compare(item, l2Array.items[l2Array.numUsed-1]) > 0){
            // increase index, create new l2Array
            level1index++;
            l2Array = (L2Array) l1Array[level1index];

        } // we leave when the item is less than the first item of the l2array
        
        // Modified this conditional when we modified the previous
        //  while loop.
        // If we're at the end of the array, stepback one to stay within bounds.
        if(level1index > l1NumUsed - 1){
            level1index--;
        }
        l2Array = (L2Array) l1Array[level1index];
        
        // At this point, we have found our array. search through it until the
        //  index of the item or greater than is found.
        while(level2index < l2Array.numUsed
                && comp.compare(item, l2Array.items[level2index]) > 0) {
            level2index++;
        }
        
        return new ListLoc(level1index, level2index);
    }

    /**
     * find location after the last matching entry or if no match, it finds the
     * index of the next larger item this is the position to add a new entry
     * this might be an unused slot at the end of a level 2 array
     * @author Tom Bahun
     * @param item
     * @return the location where this item should go
     */
    public ListLoc findEnd(E item) {
        //  current spots in the ragged array
        int level1index = 0;
        int level2index = 0;
        // Our first array to look through
        L2Array l2Array = (L2Array) l1Array[level1index];
        // test to see if array is null
        if(l2Array.items[level2index] == null){
            return new ListLoc(level1index, level2index);
        }
        // while we are within bounds, next array is not null, and the item
        //  is greater than or equal to the first item of the l2array, increase
        //  index
        while(l1Array != null && level1index < l1Array.length &&
                l2Array != null &&
                comp.compare(item, l2Array.items[level2index]) >= 0) {
            // increase index, create new l2Array
            level1index++;
            l2Array = (L2Array) l1Array[level1index];
        } // we leave when the item is less than the first item of the l2array
        
        // if index isn't 0, step back one
        if(level1index != 0){
            level1index--;
        }
        l2Array = (L2Array) l1Array[level1index];
        
        // At this point, we have found our array. search through it until the
        //  index of the item or greater than is found.
        while(l1Array != null && level2index < l2Array.numUsed
                && comp.compare(item, l2Array.items[level2index]) > 0) {
            level2index++;
        }
        
        
        while(level2index < l2Array.numUsed
                && comp.compare(item, l2Array.items[level2index]) == 0){
            // find the item after the matching items
            level2index += 1;
        }
        
        return new ListLoc(level1index, level2index);
    }

    /**
     * add object after any other matching values findEnd will give the
     * insertion position
     *
     * @param item the item to be added
     * @return returns true whether the item was added successfully or not
     * @author Thomas Bahun, Jonathan Koenig
     */
    public boolean add(E item) {
        // find the insertion point
        ListLoc tempLoc = findEnd(item);
        // create the lvl2Array from the contents of lvl1Array at the index
        L2Array l2Array = (L2Array) l1Array[tempLoc.level1Index];
        // push up and add at current listLoc
        if (l2Array.items[tempLoc.level2Index] != null) {
            for (int i = l2Array.numUsed - 1; 
                    i > tempLoc.level2Index - 1; i--) {
                // move stuff up
                l2Array.items[i + 1] = l2Array.items[i];
            }
            // add item to index
            l2Array.items[tempLoc.level2Index] = item;
        } else {
            // add item to that index
            Array.set(l2Array.items, tempLoc.level2Index, item);
        }
        // increment numUsed after adding item.
        l2Array.numUsed++;
        // update size property
        size++;
        // if current l2Array is full
        if (l2Array.numUsed == l2Array.items.length) {
            // double l2Array if smaller than lvl1
            if (l2Array.items.length < l1Array.length) {
                l2Array.items = Arrays.copyOf(l2Array.items,
                        l2Array.items.length * 2);
                // split l2Array
            } else {
                // doubles l1Array if Arry will be filled by for loop below
                if (l1Array[l1Array.length - 1] == null && 
                        l1Array[l1Array.length - 2] != null) {
                    l1Array = Arrays.copyOfRange(l1Array, 0, 
                            l1Array.length * 2);
                }
                // pushes l1Array indexes down 1, needs to be here so
                //  new l2Array has a space to be inserted into.
                for (int i = l1NumUsed; i > tempLoc.level1Index - 1; i--) {
                    l1Array[i + 1] = l1Array[i];
                }
                //increment number of items in l1Array
                l1NumUsed++;
                //create empty l2Array
                L2Array nextL2Array = new L2Array(1);
                // take last half of objects in l2Array and adds nulls 
                //  onto end, length+length/2 is necessary.
                nextL2Array.items = Arrays.copyOfRange(l2Array.items, 
                        l2Array.numUsed / 2,
                        l2Array.items.length + l2Array.items.length / 2);
                // sets nextL2Arrays numUsed to the correct amount
                nextL2Array.numUsed = l2Array.numUsed / 2;
                // take first half of objects in l2Array 
                //  and replaces exsisting l2Array
                l2Array.items = Arrays.copyOfRange(l2Array.items, 0, 
                        l2Array.numUsed / 2);
                // sets l2Array numUsed to the correct amount
                l2Array.numUsed = l2Array.numUsed / 2;
                // doubles l2Array indexes to add 4 nulls
                l2Array.items = Arrays.copyOfRange(l2Array.items, 0, 
                        l2Array.numUsed * 2);
                // inserts l2Array into l1Array
                l1Array[tempLoc.level1Index + 1] = nextL2Array;
            }
        }
        return true;
    }

    /**
     * check if list contains a match
     *
     * @param item the item to search for
     * @return true or false whether contained in the ragged array
     * 
     * @author tombahun & ethanray
     */
    public boolean contains(E item) {
      ListLoc tempLoc = findFront(item);
      L2Array l2Array = (L2Array) l1Array[tempLoc.level1Index];
      if(l2Array.items[tempLoc.level2Index].equals(item)) {
        return true;
      } else {
        return false;
      }
    }

    /**
     * copy the contents of the RaggedArrayList into the given array
     *
     * @param a - an array of the actual type and of the correct size
     * @return the filled in array
     * 
     * @author tombahun & ethanray
     */
    public E[] toArray(E[] a) {
        // TO DO in part 5
        /*if(a.length == size) { // should this be size() method?
            for(int l1 = 0; l1 <= l1NumUsed; l1++) {
                L2Array l2Array = (L2Array) l1Array[l1];
                for(int l2 = 0; l2 <= l2Array.numUsed; l2++) {
                    a[l2] = l2Array.items[l2];
                }
            }
        } else {
            System.out.println("Incorrect size for given array.");
        }*/
        if(a.length == size) {
            Itr itr = new Itr();
            int index = 0;
            while(itr.hasNext()) {
                a[index] = itr.next();
                index++;
            }
        } else {
            System.out.println("Incorrect size for given array.");
        }
        return a;
    }

    /**
     * returns a new independent RaggedArrayList whose elements range from
     * fromElemnt, inclusive, to toElement, exclusive the original list is
     * unaffected findStart and findEnd will be useful
     *
     * @param fromElement
     * @param toElement
     * @return the sublist
     * 
     * @author tombahun & ethanray
     */
    public RaggedArrayList<E> subList(E fromElement, E toElement) {
        RaggedArrayList<E> result = new RaggedArrayList<E>(comp);
        ListLoc front = new ListLoc(findFront(fromElement).level1Index, 
                findFront(fromElement).level2Index);
        ListLoc end = new ListLoc(findFront(toElement).level1Index, 
                findFront(toElement).level2Index); // no o's at all
        while(!front.equals(end)) {
            // l2 array
            L2Array l2Array = (L2Array) l1Array[front.level1Index];
            // add items to new RaggedArray
            result.add(l2Array.items[front.level2Index]);
            // move to next
            front.moveToNext();
        }
        return result;
    }

    /**
     * returns an iterator for this list this method just creates an instance of
     * the inner Itr() class (DONE)
     *
     * @return
     */
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * Iterator is just a list loc it starts at (0,0) and finishes with index2 1
     * past the last item in the last block
     */
    private class Itr implements Iterator<E> {

        private ListLoc loc;

        /*
         * create iterator at start of list
         * (DONE)
         */
        Itr() {
            loc = new ListLoc(0, 0);
        }

        /**
         * check if more items
         * 
         * @author tombahun & ethanray
         */
        public boolean hasNext() {
            return (L2Array) l1Array[loc.level1Index] != null;
        }

        /**
         * return item and move to next throws NoSuchElementException if off end
         * of list
         * 
         * @author tombahun & ethanray
         */
        public E next() {
            L2Array l2Array = (L2Array) l1Array[loc.level1Index];            
            if(hasNext()) {
                E item = l2Array.items[loc.level2Index];
              	loc.moveToNext();	
                return item;
            }
            else {
                throw new IndexOutOfBoundsException();
            } 
        }

        /**
         * Remove is not implemented. Just use this code. (DONE)
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
