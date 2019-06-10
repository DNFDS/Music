package com.example.demo.service.Shazam.search;

/**
 * SongScore 
 */
public class SongScore {
    public int id;
    public int score;

    @Override
    public String toString() {
        return String.format("score=%d for [%d](%s)", score, id);
    }
}
