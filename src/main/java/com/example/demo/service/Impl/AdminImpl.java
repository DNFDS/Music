package com.example.demo.service.Impl;

import com.example.demo.dao.AdminMapper;
import com.example.demo.dao.AlbumMapper;
import com.example.demo.dao.SingerMapper;
import com.example.demo.dao.SongMapper;
import com.example.demo.entity.Album;
import com.example.demo.entity.Singer;
import com.example.demo.entity.Song;
import com.example.demo.entity.comments;
import com.example.demo.service.AdminService;
import com.example.demo.service.AlbumService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminImpl implements AdminService {

    @Resource
    AdminMapper adminMapper;

    @Resource
    SingerMapper singerMapper;

    @Override
    public String isAdminExist(String id, String pwd){
        Map<String,Object> map=new HashMap<>();
        map.put("id",id);
        map.put("pwd",pwd);
        adminMapper.isAdminExist(map);
        return (String)map.get("succ");
    }

    @Override
    public String getMaxSongid(){
        Map<String,Object> map=new HashMap<>();
        adminMapper.getMaxSongid(map);
        return (String)map.get("songid");
    }

    @Override
    public String getMaxAlbumid(){
        Map<String,Object> map=new HashMap<>();
        adminMapper.getMaxAlbumid(map);
        return (String)map.get("albumid");
    }

    @Override
    public String getMaxSingerid(){
        Map<String,Object> map=new HashMap<>();
        adminMapper.getMaxSingerid(map);
        return (String)map.get("singerid");
    }

    @Override
    public Singer getSingerByName(String name){
        Map<String,Object> map=new HashMap<>();
        map.put("singername",name);
        singerMapper.getSingerByName(map);
        return ((ArrayList<Singer>)map.get("singers")).get(0);
    }

    @Override
    public String getAlbumidBySAName(String singerName, String albumName){
        Map<String,Object> map=new HashMap<>();
        map.put("singername",singerName);
        map.put("albumname",albumName);
        adminMapper.getAlbumidBySAName(map);
        return (String)map.get("albumid");
    }
    /*
    call ADMIN_PKG.add_album(#{albumid,mode=IN,jdbcType=VARCHAR},#{albumname,mode=IN,jdbcType=VARCHAR},
    #{albumage,mode=IN,jdbcType=VARCHAR},#{language,mode=IN,jdbcType=VARCHAR},
    #{company,mode=IN,jdbcType=VARCHAR},#{adminid,mode=IN,jdbcType=VARCHAR},
    #{singerid,mode=IN,jdbcType=VARCHAR},#{succ,mode=OUT,jdbcType=VARCHAR})\
    call ADMIN_PKG.add_singer(#{singerid,mode=IN,jdbcType=VARCHAR},#{singername,mode=IN,jdbcType=VARCHAR},
    #{region,mode=IN,jdbcType=VARCHAR},#{sex,mode=IN,jdbcType=VARCHAR},#{intro,mode=IN,jdbcType=VARCHAR},
    #{adminid,mode=IN,jdbcType=VARCHAR},#{succ,mode=OUT,jdbcType=VARCHAR})*/
    @Override
    public String addSong(Song s){
        Map<String,Object> map=new HashMap<>();
        map.put("songid",s.getSongid());
        map.put("songname",s.getSongname());
        map.put("length",s.getLength());
        map.put("language",s.getLanguage());
        map.put("age",s.getSongage());
        map.put("school",s.getSongschool());
        map.put("company",s.getCompany());
        map.put("adminid",s.getAdminid());
        map.put("albumid", s.getAlbumid());
        map.put("free",s.getFree());
        adminMapper.addSong(map);
        return (String)map.get("succ");
    }

    @Override
    public String addAlbum(Album a){
        Map<String,Object> map=new HashMap<>();
        map.put("albumid",a.getAlbumid());
        map.put("albumname",a.getAlbumname());
        map.put("language",a.getLanguage());
        map.put("albumage",a.getAlbumage());
        map.put("company",a.getCompany());
        map.put("adminid",a.getAdminid());
        map.put("singerid", a.getSingerid());
        map.put("free",a.getFree());
        adminMapper.addAlbum(map);
        return (String)map.get("succ");
    }

    @Override
    public String addSinger(Singer s){
        Map<String,Object> map = new HashMap<>();
        map.put("singerid",s.getSingerid());
        map.put("singername",s.getSingername());
        map.put("region",s.getRegion());
        map.put("sex",s.getSingersex());
        map.put("intro",s.getIntroduction());
        map.put("adminid",s.getAdminid());
        adminMapper.addSinger(map);
        return (String)map.get("succ");
    }

    @Override
    public Song getDeleteSong(String singername, String albumname, String songname){
        Map<String,Object> map = new HashMap<>();
        map.put("albumname",albumname);
        map.put("singername",singername);
        map.put("songname",songname);
        adminMapper.getSongBySASName(map);
        return ((ArrayList<Song>)map.get("song")).isEmpty()?null:((ArrayList<Song>)map.get("song")).get(0);
    }

    @Override
    public Album getDeleteAlbum(String singername, String albumname){
        Map<String,Object> map = new HashMap<>();
        map.put("albumname",albumname);
        map.put("singername",singername);
        adminMapper.getAlbumBySAName(map);
        return ((ArrayList<Album>)map.get("album")).isEmpty()?null:((ArrayList<Album>)map.get("album")).get(0);
    }

    @Override
    public Singer getDeleteSinger(String singername){
        Map<String,Object> map = new HashMap<>();
        map.put("singername",singername);
        singerMapper.getSingerByName(map);
        return ((ArrayList<Singer>)map.get("singers")).isEmpty()?null:((ArrayList<Singer>)map.get("singers")).get(0);
    }

    @Override
	public Object deleteSong(String songid){
        Map<String,Object> map = new HashMap<>();
        map.put("songid",songid);
        adminMapper.deleteSong(map);
        return map.get("succ");
    }

    @Override
	public Object deleteAlbum(String albumid){
        Map<String,Object> map = new HashMap<>();
        map.put("albumid",albumid);
        adminMapper.deleteAlbum(map);
        return map.get("succ");
    }

    @Override
	public Object deleteSinger(String singerid){
        Map<String,Object> map = new HashMap<>();
        map.put("singerid",singerid);
        adminMapper.deleteSinger(map);
        return map.get("succ");
    }

    @Override
    public String banUser(String adminid){
        Map<String,Object> map = new HashMap<>();
        map.put("adminid",adminid);
        adminMapper.banUser(map);
        String s="";
        ArrayList<String> banids=(ArrayList<String>)map.get("banids");
        if(banids.size()==0){
            return s;
        }
        else{
            for (int i=0;i<((ArrayList<String>)map.get("banids")).size();i++){
                map.put("userid",((ArrayList<String>)map.get("banids")).get(i));
                s+=((ArrayList<String>)map.get("banids")).get(i);
                adminMapper.banSingleUser(map);
                if(((String)map.get("succ")).equals("1")){
                    s+="封禁成功 ";
                }
                else{
                    s+="封禁失败 ";
                }
                map.remove("succ");
                map.remove("userid");
            }
        }
        return s;
    }

    @Override
    public ArrayList<comments> getNewComments(int pgnum){
        Map<String,Object> map = new HashMap<>();
        map.put("pgnum",pgnum);
        adminMapper.viewAllNewComments(map);
        return (ArrayList<comments>)map.get("comments");
    }

    @Override
    public int getNewCommentsTotal(){
        Map<String,Object> map = new HashMap<>();
        adminMapper.getNewCommentsTotal(map);
        return (int)map.get("cnt");
    }
    
    @Override
    public Object passComment(String uid, String sid, String ctime){
        Map<String,Object> map = new HashMap<>();
        map.put("userid", uid);
        map.put("songid",sid);
        map.put("commenttime",ctime);
        adminMapper.passComment(map);
        return map.get("succ");
    }

    @Override
	public Object failComment(String uid, String sid, String ctime){
        Map<String,Object> map = new HashMap<>();
        map.put("userid", uid);
        map.put("songid",sid);
        map.put("commenttime",ctime);
        adminMapper.failComment(map);
        return map.get("succ");
    }

    @Override
    public int getBanTotal(){
        Map<String,Object> map = new HashMap<>();
        adminMapper.getBanTotal(map);
        return (int)map.get("cnt");
    }

    @Override
    public Object getBan(int pgnum){
        Map<String,Object> map = new HashMap<>();
        map.put("pgnum",pgnum);
        adminMapper.getBan(map);
        return map.get("bans");
    }

    @Override
    public Object unBan(String uid){
        Map<String,Object> map = new HashMap<>();
        map.put("userid",uid);
        adminMapper.unBan(map);
        return map.get("succ");
    }

    @Override
    public String[] getBasicInfo(String aid){
        Map<String,Object> map = new HashMap<>();
        map.put("adminid",aid);
        adminMapper.getBasicInfo(map);
        return ((String)map.get("info")).split("/",7);
    }

}
