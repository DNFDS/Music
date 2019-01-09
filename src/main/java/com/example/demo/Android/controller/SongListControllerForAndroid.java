package com.example.demo.Android.controller;

import com.example.demo.entity.*;
import com.example.demo.entity.result.ResultEntity;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SongListControllerForAndroid {

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

    @RequestMapping(value = "/Android/getAllMySongList", method = RequestMethod.POST)
    public Map getAllMySongList(@RequestParam("id")String id){
        ResultEntity e =userService.getUserById(id);
        User user = (User) e.getObject();
        ArrayList<SongList> Csonglist = userService.getCreatedSongList(id);
        ArrayList<SongList> Ssonglist = userService.getKeepedSongList(id);
        ArrayList<Integer> Cnum = new ArrayList<>();
        ArrayList<Integer> Snum = new ArrayList<>();
        for(SongList list : Csonglist){
            ArrayList<Song> l = (ArrayList<Song>)songListService.getSongsInSongList(list).getObject();
            Cnum.add(l.size());
        }
        for(SongList list : Ssonglist){
            ArrayList<Song> l = (ArrayList<Song>)songListService.getSongsInSongList(list).getObject();
            Snum.add(l.size());
        }
        Map<String,Object>map = new HashMap<>() ;
        map.put("Csonglist",Csonglist);
        map.put("Ssonglist",Ssonglist);
        map.put("Cnum",Cnum);
        map.put("Snum",Snum);
        map.put("user",user);
        return map;
    }

    @RequestMapping(value = "/Android/createSongList", method = RequestMethod.POST)
    public Map createSongList(@RequestParam("id")String id,@RequestParam("name")String name){
        String msg = songListService.createNewSongList(name,"","0",id);
        Map<String,Object>map = new HashMap<>() ;
        map.put("msg",msg);
        return map;
    }

    @RequestMapping(value = "/Android/deleteSongList", method = RequestMethod.POST)
    public Map deleteSongList(@RequestParam("songlistid") String songlistid){
        songListService.deleteSongList(songlistid);
        Map<String,Object>map = new HashMap<>() ;
        map.put("msg","删除成功了嗷");
        return map;
    }

    @RequestMapping(value = "/Android/deleteSongLists", method = RequestMethod.POST)
    public Map deleteSongList(@RequestParam("songlistid1")String songlistid1,@RequestParam("songlistid2")String songlistid2,@RequestParam("songlistid3")String songlistid3){
        if(!songlistid1.equals("")){
            songListService.deleteSongList(songlistid1);
        }
        if(!songlistid2.equals("")){
            songListService.deleteSongList(songlistid2);
        }
        if(!songlistid3.equals("")){
            songListService.deleteSongList(songlistid3);
        }
        Map<String,Object>map = new HashMap<>() ;
        map.put("msg","删除成功了嗷");
        return map;
    }

    @RequestMapping(value = "/Android/deleteSongInList", method = RequestMethod.POST)
    public Map deleteSongInList(@RequestParam("songlistid")String songlistid,@RequestParam("songid")String songid){
        songListService.deleteSongInList(songid,songlistid);
        Map<String,Object>map = new HashMap<>() ;
        map.put("msg","删除成功了嗷");
        return map;
    }

    @RequestMapping(value = "/Android/changeSongListName", method = RequestMethod.POST)
    public Map changeSongListName(@RequestParam("name")String name,@RequestParam("songlistid") String songlistid){
        String succ = songListService.changeSongListName(name,songlistid);
        Map<String,Object>map = new HashMap<>();
        if(succ.equals("1"))
            map.put("msg","修改名字成功了嗷");
        else
            map.put("msg","修改失败了嗷");
        return map;
    }

    @RequestMapping(value = "/Android/showSongList", method = RequestMethod.POST)
    public Map showSongList(@RequestParam("songlistid") String songlistid,@RequestParam("uid") String uid){
        Map<String,Object> map = new HashMap<>();
        ArrayList<Singer> singers = new ArrayList<>();
        ArrayList<String> albums = new ArrayList<>();
        ArrayList<SongList> songLists = userService.getCreatedSongList(uid);
        SongList a = new SongList();
        a.setSonglistid(songlistid);
        ResultEntity e = songListService.getSongsInSongList(a);
        ArrayList<Song> songs = (ArrayList<Song>)e.getObject();
        for(Song song:songs){
            e = songService.getSingersInSong(song.getSongid());
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

    @RequestMapping(value = "/Android/keepSongList", method = RequestMethod.POST)
    public Map keepSongList(@RequestParam("userid") String userid,@RequestParam("songlistid") String songlistid){
        Map<String,Object > map = new HashMap<>();
        String result = keepService.KeepSongList(songlistid,userid);
        if(result.equals("0")){
            keepService.unKeepSongList(songlistid,userid);
            map.put("msg","歌单已取消收藏");
        }
        else
            map.put("msg","收藏成功");
        return map;
    }

    @RequestMapping(value = "/Android/isKeeped",method = RequestMethod.POST)
    public Map isKeeped(@RequestParam("songlistid")String songlistid,@RequestParam("userid")String userid){
        Map<String,Object > map = new HashMap<>();
        ArrayList<SongList> songLists = userService.getKeepedSongList(userid);
        for(SongList songList:songLists){
            if(songList.getSonglistid().equals(songlistid)){
                map.put("succ",true);
                return map;
            }
        }
        map.put("succ",false);
        return map;
    }

    @RequestMapping(value = "/Android/songListDetail",method = RequestMethod.POST)
    public Map songListDetail(@RequestParam("songlistid")String songlistid,@RequestParam("userid")String userid){
        Map<String,Object > map = new HashMap<>();
        SongList songList = songListService.getSongListById(songlistid);
        map.put("songlist",songList);
        return map;
    }
}
