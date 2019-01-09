package com.example.demo.Android.controller;

import com.example.demo.util.AutoShowUtil;
import com.example.demo.entity.*;
import com.example.demo.entity.result.ResultEntity;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SearchControllerForAndroid {
    @Autowired
    private UserService userService;
    @Autowired
    private SongListService songListService;
    @Autowired
    private SongService songService;
    @Autowired
    private AutoShowUtil showUtil;
    @Autowired
    private AlbumService albumService;

    @RequestMapping(value = "/Android/searchSong", method = RequestMethod.POST)
    public Map searchSong(@RequestParam("name") String name, @RequestParam("uid") String uid){
        Map<String,Object> map = new HashMap<>();
        ArrayList<Singer> singers = new ArrayList<>();
        ArrayList<String> albums = new ArrayList<>();
        ArrayList<SongList> songLists = userService.getCreatedSongList(uid);
        ArrayList<Song> songs = songService.getSongByNamePart(name);
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

    @RequestMapping(value = "/Android/searchSongList", method = RequestMethod.POST)
    public Map searchSongList(@RequestParam("name") String name){
        ArrayList<SongList> songlists = songListService.getSongListByNamePart(name);
        Map<String,Object> map = new HashMap<>();
        ArrayList<Integer>songnum = new ArrayList<>();
        ArrayList<String>users = new ArrayList<>();
        for(SongList list : songlists){
            ArrayList<Song> l = (ArrayList<Song>)songListService.getSongsInSongList(list).getObject();
            songnum.add(l.size());
            User user = (User) userService.getUserById(list.getUserid()).getObject();
            users.add(user.getUsername());
        }
        map.put("songlist",songlists);
        map.put("songnum",songnum);
        map.put("users",users);
        return map;
    }

    @RequestMapping(value = "/Android/searchUser", method = RequestMethod.POST)
    public Map searchUser(@RequestParam("name") String name,@RequestParam("userid") String userid){
        ArrayList<User> users = userService.getUserByNamePart(name);
        Map<String,Object > map = showUtil.showUser(users,userid);
        return map;
    }

}
