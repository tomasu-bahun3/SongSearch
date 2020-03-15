/** *************************************************************************
 * Revision History (newest first)
 ***************************************************************************
 * 1/28/2020 - Tom Bahun and Arsene Ininahazwe worked on part 2
 * 01/20/20 Tiffany Hoeung & Thomas Bahun completed empty classes
 * 2016 Anne Applin formatting and JavaDoc added
 * 2015 Starting code by Prof. Boothe
 ************************************************************************* */
package student;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * A class that parses the data file and builds an array of Songs objects.
 *
 * @author bboothe
 */
public class SongCollection {

    /**
     * an array of Song objects
     */
    private Song[] songs;

    /**
     * Note: in any other language, reading input inside a class is simply not
     * done!! No I/O inside classes because you would normally provide
     * precompiled classes and I/O is OS and Machine dependent and therefore not
     * portable. Java runs on a virtual machine that IS portable. So this is
     * permissable because we are programming in Java.
     * @author Tom Bahun
     * @param filename The path and filename to the datafile that we are using.
     */
    public SongCollection(String filename) {
        Scanner inputFile = null;
        ArrayList<Song> songList = new ArrayList();
        try {
            inputFile = new Scanner(new FileReader(filename));
            while (inputFile.hasNext()) { // read in one song
                Song song = null;
                song = readSong(inputFile);
                songList.add(song);
                // testing: System.out.println(song);
                // testing: System.out.println(song.getLyrics() + "\n");
            }
            Collections.sort(songList);
            
            songs = new Song[songList.size()];
            songs = songList.toArray(new Song[songs.length]);
        } catch (FileNotFoundException ex) {
            System.err.println("The file " + filename + " does not exist");
            System.exit(1);
        } catch (InputMismatchException ex) {
            System.err.println("Attempt to read the wrong data type.");
            System.exit(1);
        }

        // use a try catch block
        // read in the song file and build the songs array
        // you must use a StringBuilder to read in the lyrics!
        // sort the songs array
    }

    /**
     * readSong method reads and stores the content from the file
     * @author Tom Bahun
     * @param s the scanner
     * @return the song
     */
    private Song readSong(Scanner s) {
        StringBuilder str = new StringBuilder(); // for lyrics
        String eol = System.lineSeparator();

        //Reads the artist and takes only the artist name.
        String artist = s.nextLine();
        artist = artist.substring(artist.indexOf('\"')+1,
                artist.lastIndexOf('\"'));

        //Reads the title and takes only the title name.
        String title = s.nextLine();
        title = title.substring(title.indexOf('\"')+1,
                title.lastIndexOf('\"'));

        //Reads the first lyric and takes only the lyrics.
        String nextLyric = s.nextLine();
        str.append(nextLyric.substring(nextLyric.indexOf('\"')+1));
        nextLyric = s.nextLine();

        //Continues reading the lyrics until reaching "
        while (!nextLyric.startsWith("\"")) {
            str.append(eol + nextLyric);
            nextLyric = s.nextLine();
        }

        // Build lyrics
        String lyrics = str.toString();

        // Create new Song and return
        return new Song(artist, title, lyrics);
    }

    /**
     * this is used as the data source for building other data structures
     * @author Tom Bahun
     * @return the songs array
     */
    public Song[] getAllSongs() {
        return songs;
    }

    /**
     * Returns a formatted string with the total number of songs, as well as the
     * first ten songs with their artist and title.
     * @author Tom Bahun
     * @return The first ten songs with their artist and title.
     */
    public String getFirstSongs() {
        StringBuilder str = new StringBuilder();
        int numSongs = songs.length;
        String eol = System.lineSeparator();
        str.append("Total songs = " + numSongs + ", first songs:");

        if (numSongs > 10) { // If the songlist contains 10 or more songs
            for (int i = 0; i < 10; i++) {
                str.append(eol + songs[i].getArtist() + 
                        "  \"" + songs[i].getTitle() + "\"");
            }
        } else { // less than 10 songs, only searches the array size
            for (int i = 0; i < numSongs; i++) {
                str.append(eol + songs[i].getArtist() + 
                        "  \"" + songs[i].getTitle() + "\"");
            }
        }
        return str.toString();
    }

    /**
     * unit testing method Start by setting shortSongs.txt as the argument in
     * the Project Properties.
     * @author Tom Bahun
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);

        // show song count 
        sc.getFirstSongs();
        
        //print first 10 songs(1 per line)
        Stream.of(sc.getAllSongs()).limit(10).forEach(System.out::println);
    }
}
