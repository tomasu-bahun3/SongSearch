/***************************************************************************
 * Revision History (newest first)
 ***************************************************************************
 * 2/28/2020 - Tom Bahun worked on part 6
 * 1/28/2020 - Tom Bahun and Arsene Ininahazwe worked on part 2
 * 01/20/20 - Tiffany Hoeung & Thomas Bahun completed empty methods
 * 2016 Anne Applin formatting and JavaDoc added 
 * 2015 Starting code by Prof. Boothe 
 **************************************************************************/
package student;

import java.util.*;


/**
 * Song class to hold strings for a song's artist, title, and lyrics
 * Do not add any methods, just implement the ones that are here.
 * @author boothe 
 */
public class Song implements Comparable<Song> {
    /**
     * String representing the artist of the song.
     */
     private String artist;
     
     /**
     * String representing the title of the song.
     */
     private String title;
     
     /**
     * String representing the lyrics of the song.
     */
     private String lyrics;
    
    
    /**
     * Parameterized constructor for the Song class
     * @param artist the author of the song
     * @param title the title of the song
     * @param lyrics the lyrics as a string with linefeeds embedded
     */
    public Song(String artist, String title, String lyrics) {
        this.artist = artist;
        this.title = title;
        this.lyrics = lyrics;
    }

    /**
     * Accessor for the Song's artist
     * @return The Song's artist
     */
    public String getArtist() {
    	return this.artist;
    }

    /**
     * Accessor for the Song's lyrics
     * @return The Song's lyrics
     */
    public String getLyrics() {
    	return this.lyrics;
    }

    /**
     * Accessor for the Song's title
     * @return The Song's title
     */
    public String getTitle() {
    	return this.title;
    }

    /**
     * Artist and Title only.
     * @return a formatted string  with artist and title 
     */
    public String toString() {
    	StringBuilder str = new StringBuilder();
        str.append(artist).append(", ").append("\"" + title + "\"");
        return str.toString();
    }

    /**
     * the default comparison of songs
     * primary key: artist, secondary key: title
     * used for sorting and searching the song array
     * if two songs have the same artist and title they are considered the same
     * @param song2
     * @return a negative number, positive number or 0 depending on whether 
     *    this song should be  before, after or is the same.  Used for a
     *    "natural" sorting order.  In this case first by author then by 
     *    title so that the all of an artist's songs are together, 
     *    but in alpha order.  Follow the given example.
     * @author Tom Bahun
     */
    public int compareTo(Song song2) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
      
        // Shouldn't be any null objects, but puts them at the end
        if (song2 == null) {
            return AFTER;
        }
      
        // Compares the artist first; moves on if the comparison is 0 or the same
        int comparison = this.getArtist().compareToIgnoreCase(song2.getArtist());
        if (comparison != EQUAL)
            return comparison;

        // Compares the title next; moves on if the comparison is 0 or the same
        comparison = this.getTitle().compareToIgnoreCase(song2.getTitle()); 
        if (comparison != EQUAL)
            return comparison;

        // If we get to this point, the objects are the same
        return EQUAL;
      
    }
    
    /**
     * Nested class to compare songs by artist
     */
    public static class CmpArtist extends CmpCnt implements Comparator<Song> {
    /**
     * compare method that compares s1 and s2
     * @author Tom Bahun
     * @param s1 song 1
     * @param s2 song 2
     * @return the difference between the two s1 and s2
     */ 
        public int compare(Song s1, Song s2) {
          cmpCnt++;
          return s1.getArtist().compareToIgnoreCase(s2.getArtist());
        }
    }
    
    /**
     * A class for comparing the title names against one another.
     * @author Tom Bahun
     */
    public static class CmpTitle extends CmpCnt implements Comparator<Song> {

        /**
        * This function receives to song objects as parameters, compares the 
        * two objects data field of title lexicographically and returns an 
        * negative integer, positive integer or zero, based on the values
        * compared.
        * @author Tom Bahun
        * @param s1 is the first song object passed into the function
        * @param s2 is the seconds song object passed into the function
        * @return returns a positive or negative integer, based on the 
        * lexicographic values of the two artist strings
        */
        @Override
        public int compare(Song s1, Song s2) {
            // increment the comparison counter cmpCnt
            cmpCnt++;
            // return an int based on lexicographic values of s1 and s2
            return s1.title.compareToIgnoreCase(s2.title);
        }
    }
 
    /**
     * testing method to unit test this class
     * @param args
     */
    public static void main(String[] args) {
        Song s1 = new Song("Professor B",
                "Small Steps",
                "Write your programs in small steps\n"
                + "small steps, small steps\n"
                + "Write your programs in small steps\n"
                + "Test and debug every step of the way.\n");

        Song s2 = new Song("Brian Dill",
                "Ode to Bobby B",
                "Professor Bobby B., can't you see,\n"
                + "sometimes your data structures mystify me,\n"
                + "the biggest algorithm pro since Donald Knuth,\n"
                + "here he is, he's Robert Boothe!\n");

        Song s3 = new Song("Professor B",
                "Debugger Love",
                "I didn't used to like her\n"
                + "I stuck with what I knew\n"
                + "She was waiting there to help me,\n"
                + "but I always thought print would do\n\n"
                + "Debugger love .........\n"
                + "Now I'm so in love with you\n");

        System.out.println("testing getArtist: " + s1.getArtist());
        System.out.println("testing getTitle: " + s1.getTitle());
        System.out.println("testing getLyrics:\n" + s1.getLyrics());

        System.out.println("testing toString:\n");
        System.out.println("Song 1: " + s1);
        System.out.println("Song 2: " + s2);
        System.out.println("Song 3: " + s3);

        System.out.println("testing compareTo:");
        System.out.println("Song1 vs Song2 = " + s1.compareTo(s2));
        System.out.println("Song2 vs Song1 = " + s2.compareTo(s1));
        System.out.println("Song1 vs Song3 = " + s1.compareTo(s3));
        System.out.println("Song3 vs Song1 = " + s3.compareTo(s1));
        System.out.println("Song1 vs Song1 = " + s1.compareTo(s1));
      
      	System.out.println("testing CmpArtist");
      	CmpArtist comp = new CmpArtist();
      	System.out.println("Song1 vs Song2 = " + comp.compare(s1, s2));
        System.out.println("Song2 vs Song1 = " + comp.compare(s2, s1));
        System.out.println("Song1 vs Song3 = " + comp.compare(s1, s3));
        System.out.println("Song3 vs Song1 = " + comp.compare(s3, s1));
        System.out.println("Song1 vs Song1 = " + comp.compare(s1, s1));
    }
}