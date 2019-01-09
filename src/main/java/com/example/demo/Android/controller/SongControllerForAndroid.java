package com.example.demo.Android.controller;

import com.example.demo.entity.Album;
import com.example.demo.entity.Singer;
import com.example.demo.entity.Song;
import com.example.demo.entity.SongList;
import com.example.demo.entity.result.ResultEntity;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SongControllerForAndroid {
    @Autowired
    private UserService userService;
    @Autowired
    private SongListService songListService;
    @Autowired
    private SongService songService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private KeepService keepService;

    @RequestMapping(value = "/Android/showSong", method = RequestMethod.POST)
    public Map showSong(@RequestParam("songid") String songid){
        Map<String,Object > map = new HashMap<>();
        Song song = songService.getSongById(songid);
        Album album = albumService.getAlbumByAlbumId(song.getAlbumid());
        ResultEntity e  = songService.getSingersInSong(song.getSongid());
        ArrayList<Singer> singers = (ArrayList<Singer>)e.getObject();
        Singer singer = singers.get(0);
        map.put("song",song);
        map.put("album",album.getAlbumname());
        map.put("singer",singer.getSingername());
        return map;
    }


    @RequestMapping(value = "/Android/keepSong", method = RequestMethod.POST)
    public Map keepSong(@RequestParam("songid") String songid,@RequestParam("songlistid") String songlistid){
        Map<String,Object > map = new HashMap<>();
        String result = keepService.KeepSong(songid,songlistid);
        if(result.equals("0")){
            map.put("msg","歌曲已存在");
        }
        else
            map.put("msg","已添加到歌单");
        return map;
    }

    @RequestMapping(value = "/Android/favoriteSong",method = RequestMethod.POST)
    public Map favoriteSong(@RequestParam("songid")String songid,@RequestParam("userid")String userid){
        Map<String,Object > map = new HashMap<>();
        SongList favorite = userService.getFavoritelist(userid);
        if(favorite == null){
            map.put("msg","用户默认歌单不存在");
            return map;
        }
        String result = keepService.KeepSong(songid,favorite.getSonglistid());
        if(result.equals("0")){
            keepService.unKeepSong(songid,favorite.getSonglistid());
            map.put("msg","取消喜欢成功");
            return map;
        }
        map.put("msg","喜欢成功");
        return map;
    }

    @RequestMapping(value = "/Android/isFavorite",method = RequestMethod.POST)
    public Map isFavorite(@RequestParam("songid")String songid,@RequestParam("userid")String userid){
        Map<String,Object > map = new HashMap<>();
        SongList favorite = userService.getFavoritelist(userid);
        ResultEntity e = songListService.getSongsInSongList(favorite);
        ArrayList<Song>songs = (ArrayList<Song>)e.getObject();
        for(Song song:songs){
            if(song.getSongid().equals(songid)){
                map.put("succ",true);
                return map;
            }
        }
        map.put("succ",false);
        return map;
    }
}
