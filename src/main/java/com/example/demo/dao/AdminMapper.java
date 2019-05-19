package com.example.demo.dao;

import java.util.Map;

import com.example.demo.entity.Admin;

public interface AdminMapper {

    void isAdminExist(Map<String,Object> Map);

    void getMaxSongid(Map<String,Object> Map);

    void getMaxAlbumid(Map<String,Object> Map);

    void getMaxSingerid(Map<String, Object> map);
    
    void addSong(Map<String, Object> map);

    void addAlbum(Map<String, Object> map);

    void addSinger(Map<String, Object> map);

    void getAlbumidBySAName(Map<String, Object> map);

    void getSongBySASName(Map<String, Object> map);

    void getAlbumBySAName(Map<String, Object> map);

    void deleteSong(Map<String, Object> map);

    void deleteAlbum(Map<String, Object> map);

    void deleteSinger(Map<String, Object> map);
}