package com.example.demo.service;

import java.util.ArrayList;

import com.example.demo.service.Shazam.hash.Hash;

public interface ShazamService {
    String insertHash(int hashid,String songid,int offset);
    void buildIndex();
    String getSongName(String songid);
    ArrayList<Hash> selectHash(int hashid);
    int insertSong(String songName);
}