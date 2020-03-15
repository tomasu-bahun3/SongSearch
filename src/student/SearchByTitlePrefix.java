/** *************************************************************************
 * Revision History (newest first)
 ***************************************************************************
 * 2020 Thomas Bahun/Taylor Owen - ran test cases.
 * 2020 Thomas Bahun/Taylor Owen/Cameron Gibson - wrote search().
 * 2020 Tom Bahun and Cameron Gibson - wrote SearchByTitlePrefix.class
 ************************************************************************* */
package student;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * SearchByTitlePrefix searches the artists in the song database for artists
 * that begin with the input String.
 *
 * @author Tom Bahun
 */
public class SearchByTitlePrefix {

    //an array of song objects
    private Song[] songs;
    //initial RAL
    private RaggedArrayList<Song> ral;
    //the RAL that will hold the subList or ral
    private RaggedArrayList<Song> sub;
    //counts the comparisons
    private static int compCount;
    //counts matches
    private static int matchCount;


    /*constructor that takes in a SongCollection ane uses the data from it to
     * to build a RaggedArrayList of songs ordered by title.
     * @author Tom Bahun
     * @param sc a SongCollection object
     */
    public SearchByTitlePrefix(SongCollection sc) {
        //dump the song collection into the songs array.
        songs = sc.getAllSongs();
        //create the comparator written in Song.CmpTitle.
        Song.CmpTitle comp = new Song.CmpTitle();
        //initialize the RaggedArrayList using the title comparator (comp)
        ral = new RaggedArrayList<>(comp);
        //iterate through the songs array and add each song to the ral.
        for (Song song : songs) {
            //add's song to ral.
            ral.add(song);
        }
        //declare the subList ral. also using the comparator.
        sub = new RaggedArrayList<>(comp);
        //initialize comparison counter.
        compCount = comp.getCmpCnt();
    }

    /**
     * find all songs matching title prefix uses binary search should operate in
     * time log n + k (# matches) converts titlePrefix to lowercase and creates
     * a Song object with title prefix as the title in order to have a Song to
     * compare. These Song objects are sorted by title.
     *
     * @author Thomas Bahun, Cameron Gibson, Taylor Owen
     * @param titlePrefix all or part of the title.
     * @return result an array of songs by titles with substrings that match the
     * prefix
     *
     */
    public Song[] search(String titlePrefix) {    
	// reset Match Count
	matchCount = 0;
        // covert the title of the song to lowerCase
        titlePrefix = titlePrefix.toLowerCase();
        // declare the firstMatch dummy Song for subList
        Song firstMatch = new Song("", titlePrefix, "");
        // declare a Song by using the titlePrefix
        Song prefix = new Song("", titlePrefix, "");
        // create an iterator to step through ral
        Iterator itr = ral.iterator();
        // step through ral, and look for the first matching title prefix
        while (itr.hasNext()) {
            // if the titlePrefix is a match make a Song object with that object
            Song match = (Song) itr.next();
            // increment the comp counter                   _||_
            compCount++;//                                  \  /
            // check if the title contains the prefix        \/   
            if (match.getTitle().toLowerCase().startsWith(prefix.getTitle().toLowerCase())) {
                // increment the match counter
                matchCount++;
                // if a match is found, declare this song as the firstMatch
                if (matchCount == 1) {
                    firstMatch = match;
		}
            }
        }
        // convert the prefix to a char array
        char[] prefixArray = titlePrefix.toCharArray();
        // increment the last letter of the search term
        prefixArray[prefixArray.length - 1]++;
        // return the char arry to a String
        String lastMatchPrefix = new String(prefixArray);
        // create dummy Song with lastMatchPrefix as title
        Song lastMatch = new Song("", lastMatchPrefix, "");
        // create a subList of the ral containing only matching prefixed titles
        sub = ral.subList(firstMatch, lastMatch);
        // declare a new array of the same size as sub
        Song[] result = new Song[sub.size];
        // convert sub into a song array using the toArray method
        result = sub.toArray(result);
        // return the resulting array
        return result;
    }

    /**
     * testing method for this unit
     * @author Tom Bahun
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }
        SongCollection sc = new SongCollection(args[0]);
        SearchByTitlePrefix sbtp = new SearchByTitlePrefix(sc);

        if (args.length > 1) {
            for (int i = 1; i < args.length; i++) {
                sbtp.compCount = 0;
                System.out.println("Searching for: " + args[i]);
                Song[] btr = sbtp.search(args[i]);
                System.out.println("Total Comparisons: " + compCount);
                System.out.println("Total Matches: " + matchCount);
                Stream.of(sbtp.search(args[i])).limit(10).forEach(System.out::println);
                System.out.println("");
            }
        }
    }
}
