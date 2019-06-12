package com.example.demo.service.Shazam.search;

/**
 * SongScore 
 */
public class SongScore {
    public String id;
    public int score;
    public String songName;

    @Override
    public String toString() {
        return String.format("score=%d for (%s)[%s]", score, id, songName);
    }
}
