package com.example.demo.Android.controller;

import com.example.demo.entity.Album;
import com.example.demo.entity.Singer;
import com.example.demo.entity.Song;
import com.example.demo.entity.SongList;
import com.example.demo.entity.result.ResultEntity;
import com.example.demo.service.AlbumService;
import com.example.demo.service.SongService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RecommandControllerAndroid {
    @Autowired
    private UserService userService;
    @Autowired
    private SongService songService;
    @Autowired
    private AlbumService albumService;

    @RequestMapping(value = "/Android/showRecommend", method = RequestMethod.POST)
    public Map showRecommend(@RequestParam("userid") String userid){
        Map<String,Object> map = new HashMap<>();
        ArrayList<Song> songs = songService.getCommandSong(userid);
        ArrayList<SongList> songLists = userService.getCreatedSongList("100501");
        if(songs.size()>3){
            map.put("songs",songs.subList(0,3));
        }
        else
            map.put("songs",songs);
        for(SongList i:songLists){
            if(i.getSonglistname().equals("我喜欢")){
                songLists.remove(i);
                break;
            }
        }
        map.put("songlists",songLists);
        return map;
    }

    @RequestMapping(value = "/Android/showRecommendSong", method = RequestMethod.POST)
    public Map showRecommendSong(@RequestParam("userid") String userid){
        Map<String,Object> map = new HashMap<>();
        ArrayList<Singer> singers = new ArrayList<>();
        ArrayList<String> albums = new ArrayList<>();
        ArrayList<SongList> songLists = userService.getCreatedSongList(userid);
        ArrayList<Song> songs = songService.getCommandSong(userid);
        for(Song song:songs){
            ResultEntity e = songService.getSingersInSong(song.getSongid());
            ArrayList<Singer> singer = (ArrayList<Singer>)e.getObject();
            Album album = albumService.getAlbumByAlbumId(song.getAlbumid());
            albums.add(album.getAlbumname());
            if(singer.size()==0){
                Singer singer1 = new Singer();
                singer1.setSingername("无");
                singers.add(singer1);
            }
            else
                singers.add(singer.get(0));
        }
        map.put("songLists",songLists);
        map.put("albums",albums);
        map.put("songs",songs);
        map.put("singers",singers);
        return map;
    }
}
