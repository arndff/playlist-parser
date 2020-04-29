package com.github.arndff.playlistparser;;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class PlaylistParser {
    private static final String EXPORT_PATH = "export/";

    private static int totalSongsCount = 0;
    private static int parsedSongsCount = 0;

    public void execute(String path) {
        long startTime = System.nanoTime();

        SortedMap<String, Set<String>> playlist = new TreeMap<>();
        SortedSet<String> others = new TreeSet<>();

        parseData(path, playlist, others);
        writeDataToFiles(path, playlist, others);

        long endTime = System.nanoTime();

        long elapsedTime = endTime - startTime;
        double elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
        System.out.printf("Execution time: %.2fs%n%n", elapsedTimeInSecond);
    }

    public void printStats() {
        int unparsedSongsCount = totalSongsCount - parsedSongsCount;

        System.out.println("Total songs: " + totalSongsCount);
        System.out.println("Parsed songs: " + parsedSongsCount);
        System.out.println("Unparsed songs: " + unparsedSongsCount);
    }

    private void parseData(String path, SortedMap<String, Set<String>> playlist, SortedSet<String> others) {
        String line;
        String[] data;
        String artist;
        String track;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            line = bufferedReader.readLine();
            ++totalSongsCount;

            while (line != null) {
                data = line.split("\\s*-\\s*");

                if (data.length > 1) {
                    artist = data[0].toLowerCase();
                    track = data[1];

                    if (playlist.containsKey(artist)) {
                        Set<String> currentSet = playlist.get(artist);
                        currentSet.add(track);
                        playlist.put(artist, currentSet);
                    } else {
                        Set<String> tracks = new HashSet<>();
                        tracks.add(track);
                        playlist.put(artist, tracks);
                    }
                } else {
                    others.add(line);
                }

                line = bufferedReader.readLine();
                ++totalSongsCount;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File hasn't been found...");
        } catch (IOException e) {
            System.out.println("A problem occurred while opening the main file...");
        }
    }

    private void createExportDir() {
        File dir = new File(EXPORT_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    private String[] setFileNames(String path) {
        int fileExtLen = 4; // .txt
        File playlistFile = new File(path);
        String playlistName = playlistFile.getName().substring(0, playlistFile.getName().length() - fileExtLen);

        String[] result = new String[2];
        result[0] = EXPORT_PATH + playlistName + "_parsedSongs.txt";
        result[1] = EXPORT_PATH + playlistName + "_unparsedSongs.txt";

        return result;
    }

    private File createFile(String fileName) {
        File file = null;

        try {
            file = new File(fileName);
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("A problem occurred when trying to create");
        }

        return file;
    }

    private void writeDataToFiles(String path, SortedMap<String, Set<String>> playlist, SortedSet<String> others) {
        createExportDir();

        String[] fileNames = setFileNames(path);
        String parsedFileName = fileNames[0];
        String unparsedFileName = fileNames[1];

        File parsedFile = createFile(parsedFileName);
        File unparsedFile = createFile(unparsedFileName);

        try (BufferedWriter parsed = new BufferedWriter(new FileWriter(parsedFile));
             BufferedWriter unparsed = new BufferedWriter(new FileWriter(unparsedFile))) {

            for (Map.Entry<String, Set<String>> entry : playlist.entrySet()) {
                parsed.write("Artist: " + entry.getKey() + "\n");
                for (String currentTrack : entry.getValue()) {
                    parsed.write(currentTrack + "\n");
                    ++parsedSongsCount;
                }

                parsed.write("\n");
            }

            for (String currentTrack : others) {
                unparsed.write(currentTrack);
                unparsed.write("\n\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}