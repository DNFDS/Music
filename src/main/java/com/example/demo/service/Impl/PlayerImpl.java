package com.example.demo.service.Impl;

import com.example.demo.dao.AlbumMapper;
import com.example.demo.dao.SingerMapper;
import com.example.demo.dao.SongListMapper;
import com.example.demo.dao.SongMapper;
import com.example.demo.entity.Album;
import com.example.demo.entity.Singer;
import com.example.demo.entity.Song;
import com.example.demo.entity.SongList;
import com.example.demo.service.PlayerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    public Song getSongByID(Map<String, Object> Map)
    {
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
}
