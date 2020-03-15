/***************************************************************************
 * Revision History (newest first)
 ***************************************************************************
 * 1/28/2020 - Tom Bahun and Arsene Ininahazwe worked on part 2
 * 2016 Anne Applin formatting and JavaDoc added 
 * 2015 Starting code by Prof. Boothe 
 **************************************************************************/

package student;

import java.io.*;
import java.util.*;
/**
 * Search by Artist Prefix searches the artists in the song database 
 * for artists that begin with the input String.
 * @author bboothe
 */

public class SearchByArtistPrefix {
    // keep a direct reference to the song array
    private Song[] songs;  

    /**
     * constructor initializes the property. {Done]
     * @param sc a SongCollection object
     */
    public SearchByArtistPrefix(SongCollection sc) {
        songs = sc.getAllSongs();
    }

    /**
     * find all songs matching artist prefix uses binary search should operate
     * in time log n + k (# matches)
     * converts artistPrefix to lowercase and creates a Song object with 
     * artist prefix as the artist in order to have a Song to compare.
     * walks back to find the first "beginsWith" match, then walks forward
     * adding to the arrayList until it finds the last match.
     *
     * @author Tom Bahun
     * @param artistPrefix all or part of the artist's name
     * @return an array of songs by artists with substrings that match 
     *    the prefix
     */
    public Song[] search(String artistPrefix) {
      long start = System.currentTimeMillis();
      String tempStr = artistPrefix.toLowerCase();
      int forwardCount = 0, backwardCount = 0;
      Song key = new Song(tempStr, "dummy", "dummy");
      Comparator<Song> cmp = new Song.CmpArtist();
      ((CmpCnt)cmp).resetCmpCnt();
      int partLength = artistPrefix.length();
      int i = Arrays.binarySearch(songs, key, cmp);
      System.out.println("CmpCnt (total comparisons): " + 
              ((CmpCnt)cmp).getCmpCnt());
      if (i < 0) {
            i = -i - 1;
            if (i == 0) {
                i+=1;
            }
        }
      ArrayList<Song> list = new ArrayList<>();
      if (i > 0) {// it should be, but it never hurts to be careful
            // find the front - the first partial match.
            while (i >= 0 && 
                    songs[i].getArtist().length() >= partLength &&
                    songs[i].getArtist().substring(0, partLength).
                    compareToIgnoreCase(artistPrefix) == 0) {  
              i--;
              forwardCount++;
            }
            // we WILL go one too far.
            i++;
            // now fill the list by walking forward
            while (i < songs.length && 
                    songs[i].getArtist().length() >= partLength && 
                    songs[i].getArtist().substring(0, partLength).
                    compareToIgnoreCase(artistPrefix) == 0) {
                list.add(songs[i]);
                i++;
                backwardCount++;
            }
            Song[] result = new Song[list.size()];
            result = list.toArray(result);
            System.out.println("Walking Forward Comparisons: " + forwardCount);
            System.out.println("Walking Backward Comparisons: " + backwardCount);
            long end = System.currentTimeMillis();
            System.out.println("My search takes " + (end-start) + "ms");
            return result;
        } else {
            return null;
        }
    }
    
    /**
     * Returns a formatted string with the num matches, as well as the
     * first ten songs (or less) with their artist and title.
     *
     * @author Tom Bahun
     * @param sbar the array containing the matches
     * @return The first ten songs with their artist and title.
     */
    public String getFirstSongs(Song[] sbar) {
        StringBuilder str = new StringBuilder();
        if(sbar != null) {
            int numSongs = sbar.length;
            String eol = System.lineSeparator();
            str.append("Total matches = " + numSongs + ", first songs:");

            if (numSongs > 10) { // If the songlist contains 10 or more songs
                for (int i = 0; i < 10; i++) {
                    str.append(eol + sbar[i].getArtist() + 
                            "  \"" + sbar[i].getTitle() + "\"");
                }
            } else { // less than 10 songs, only searches the array size
                for (int i = 0; i < numSongs; i++) {
                    str.append(eol + sbar[i].getArtist() + 
                            "  \"" + sbar[i].getTitle() + "\"");
                }
            }
            return str.toString();
        } else {
            str.append("No matches");
            return str.toString();
        }
    }

    /**
     * testing method for this unit
     * @author Tom Bahun
     * @param args  command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        SearchByArtistPrefix sbap = new SearchByArtistPrefix(sc);

        if (args.length > 1) {
            System.out.println("searching for: " + args[1]);
            Song[] byArtistResult = sbap.search(args[1]);

            // show first 10 matches
            System.out.println(sbap.getFirstSongs(byArtistResult));
        }
    }
}