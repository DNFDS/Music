package com.example.demo.service;

import com.example.demo.entity.Album;
import com.example.demo.entity.Singer;
import com.example.demo.entity.Song;

import java.util.ArrayList;

public interface AdminService {

	String isAdminExist(String id, String pwd);

    String getMaxSongid();
    
    String getMaxAlbumid();

	String getMaxSingerid();

	Singer getSingerByName(String name);

	String getAlbumidBySAName(String singerName, String albumName);

    String addSong(Song s);
    
    String addAlbum(Album a);

    String addSinger(Singer s);

	Song getDeleteSong(String singername, String albumname, String songname);

	Album getDeleteAlbum(String singername, String albumname);

	Singer getDeleteSinger(String singername);

	Object deleteSong(String songid);

	Object deleteAlbum(String albumid);

	Object deleteSinger(String singerid);

	
}

