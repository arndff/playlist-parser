package com.github.arndff.playlistparser;;

public class Main {
    public static void main(String[] args) {
        PlaylistParser playlistParser = new PlaylistParser();

        if (args.length != 1) {
            System.err.println("You should pass exactly one argument:");
            System.err.println("- playlist's txt file.");
            System.err.println("Terminating...");
            System.exit(1);
        }

        playlistParser.execute(args[0]);
        playlistParser.printStats();
    }
}