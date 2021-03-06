package com.example.demo.service;

import com.example.demo.entity.History;
import com.example.demo.entity.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PlayerService
{
    Song getSongByID(Map<String, Object> Map);
    ArrayList<Integer> getListByAlbumID(Map<String, Object> Map);
    ArrayList<Integer> getListByListID(Map<String, Object> Map);
    Integer addSong(String songID,String path,String name,String image,String length,String albumID,String albumName,String singer,String lrc,String singerID);
    Integer addUserHistory(String userID,String singerID);
    String[] getRecommendSingers(String userID)throws Exception;
}
