package com.example.demo.service.Impl;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.service.PlayerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlayerImpl implements PlayerService
{
    @Resource
    private SongMapper songMapper;
    @Resource
    private SongListMapper songListMapper;
    @Resource
    private AlbumMapper albumMapper;
    @Resource
    private SingerMapper singerMapper;
    @Resource
    private HistoryMapper historyMapper;

    @Override
    public Song getSongByID(Map<String, Object> Map)
    {
        System.out.println(Map);
        songMapper.getSongById(Map);
        ArrayList<Song> temp=(ArrayList<Song>) Map.get("songs");
        return temp.get(0);
    }

    @Override
    public ArrayList<Integer> getListByAlbumID(Map<String, Object> Map)
    {
        songMapper.getSongByAlbumId(Map);
        ArrayList<Song> arrayList=(ArrayList<Song>)Map.get("songs");
        ArrayList<Integer> integerArrayList=new ArrayList<>();
        for(Song i:arrayList)
        {
            integerArrayList.add(Integer.parseInt(i.getSongid()));
        }
        return integerArrayList;
    }

    @Override
    public ArrayList<Integer> getListByListID(Map<String, Object> Map)
    {
        songListMapper.getSongListById(Map);
        ArrayList<SongList> arrayList=(ArrayList<SongList>)Map.get("songlist");
        HashMap hashMap=new HashMap();
        hashMap.put("songlistid",arrayList.get(0).getSonglistid());
        songListMapper.getSongsInSongList(hashMap);
        ArrayList<Song>songList=(ArrayList<Song>)hashMap.get("songs");

        ArrayList<Integer> integerArrayList=new ArrayList<>();
        for(Song i:songList)
        {
            integerArrayList.add(Integer.parseInt(i.getSongid()));
        }
        return integerArrayList;
    }

    @Override
    public Integer addSong(String songID,String path, String name, String image, String length, String albumID,String albumName, String singer, String lrc,String singerID) {
        Map<String,Object>tempSongMap=new HashMap<>();
        tempSongMap.put("songid",songID);
        songMapper.getSongById(tempSongMap);
        ArrayList<Song>tempSongList=(ArrayList<Song>) tempSongMap.get("songs");
        if (tempSongList.size()!=0)
            return -1;

        Song song=new Song();
        song.setAlbumid(albumID!=null?albumID:"-1");
        song.setAdminid("1002");
        song.setCompany(name.length()%3==0?"索尼音乐":name.length()%3==1?"腾讯音乐":"网易音乐");
        song.setFree("1");
        song.setLanguage("中文");
        song.setLength(length!=null?length:"-1");
        song.setLyric(lrc);
        song.setPlaytimes(0);
        song.setSavenum(new BigDecimal(0));
        song.setSinger(singer!=null?singer:"-1");
        song.setSongage("00");
        song.setSongimage(image);
        song.setSongname(name);
        song.setSongpath(path);
        song.setSongschool(Integer.parseInt(length)%3==0?"古典":Integer.parseInt(length)%3==1?"流行":"嘻哈");
        song.setSingerID(singerID);
        song.setSongid(songID);
        Integer result=songMapper.addSong(song);
        Integer result1=0;
        Integer result2=0;

        Map<String,Object> tempMapSinger=new HashMap();
        tempMapSinger.put("singerid",singerID);
        singerMapper.getSingerById(tempMapSinger);
        ArrayList<Singer> tempSingerList=(ArrayList<Singer>) tempMapSinger.get("singers");
        if (tempSingerList.size()==0)
        {
            Singer tempSinger=new Singer();
            tempSinger.setSingerid(singerID);
            tempSinger.setAdminid("1002");
            tempSinger.setSingername(singer);
            tempSinger.setIntroduction(" ");
            tempSinger.setRegion(" ");
            tempSinger.setSingersex("女");
            tempSinger.setSingerimage(" ");

            result1=singerMapper.addSinger(tempSinger);
        }

        Map<String,Object> tempMap=new HashMap();
        tempMap.put("albumid",albumID);
        albumMapper.getAlbumById(tempMap);
        ArrayList<Album>albums=(ArrayList<Album>) tempMap.get("albums");
        if (albums.size()==0)
        {
            Album album=new Album();
            album.setAlbumid(albumID);
            album.setAlbumname(albumName);
            album.setAdminid("1002");
            album.setAlbumage("00");
            album.setCompany(song.getCompany());
            album.setFree("1");
            album.setLanguage("中文");
            album.setSingerid(singerID);
            album.setAlbumimage(image);

            result2=albumMapper.addAlbum(album);
        }

        return result+result1+result2;
    }

    @Override
    public Integer addUserHistory(String userID, String singerID)
    {
        Integer result=0;
        History history=historyMapper.getHistoryByUserAndSinger(userID,singerID);
        if (history==null)
            result=historyMapper.addUserHistory(userID,singerID);
        else
            result=historyMapper.updateHistory(userID,singerID);
        return result;
    }

    @Override
    public String[] getRecommendSingers(String userID)throws Exception
    {
        List<History>history=historyMapper.getAllHistory();
        String userList="";
        String singerList="";
        String numList="";
        for(History i:history)
        {
            userList+=(i.getUserID()+",");
            singerList+=(i.getSingerID()+",");
            numList+=(i.getNum().toString()+",");
        }
        String[] recSingers=getRecomSingers(userList,singerList,numList,userID);
        return recSingers;
    }
    public String[] getRecomSingers(String userList,String singerList,String numList,String userID)throws Exception
    {
        //设置命令行传入的参数
        String[] arg = new String[]{"untitled1/venv/bin/python", "untitled1/recommend.py",userList,singerList,numList,userID,"5"};
        Process pr = Runtime.getRuntime().exec(arg);
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line=in.readLine();
        in.close();
        pr.waitFor();
        line=line!=null?line:"";
        return line.split(",");
    }
}
