# Intro

What motivated me to create this simple project was that I wanted to extract the content from a YouTube playlist, which contains 3500+ songs.

# Preparation
You have to export playlist's content to a text file (.txt) -- it can be done from that site for free:
- https://www.tunemymusic.com/YouTube-to-File.php

# Running the project
Then you should pass that text file as an argument to the Main class. The result will be saved to ```/playlist-parser/export```.

# Output
You'll see 2 files in the aforementioned directory:
```
1) <playlistName>_parsedSongs.txt
2) <playlistName>_unparsedSongs.txt
```

## Output details
Lines in the second file don't have ```-``` in their titles.

Lines in the first file match those videos which contain ```-``` in their titles. 
They're sorted and grouped by artist. Example:
```
Artist: Black Pumas
Colors
Dirty, Dirty
```

# Bugs
However, there will be some bugs but that's because YouTube doesn't have a standard for naming music videos, for example: 
```<Artist> - <TrackID>```.
Therefore it's hard to parse the whole content of a playlist properly.
  
A bug can occur if:
- the video is named like that: ```<TrackID> - <Artist>```
- the Artist/TrackID contains ```-```
