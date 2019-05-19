package com.example.demo.userInterface;

import com.example.demo.entity.Album;
import com.example.demo.entity.Singer;
import com.example.demo.entity.Song;
import com.example.demo.entity.User;
import com.example.demo.entity.result.ResultEntity;
import com.example.demo.service.AdminService;
import com.example.demo.service.SingerService;
import com.example.demo.service.UserService;
import com.example.demo.util.AutoShowUtil;

import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

@CrossOrigin(allowCredentials="true")
@RestController
public class AdminInterface {

    @Autowired
    private AdminService adminService;
    @Autowired
    private SingerService singerService;
    @Autowired
    private AutoShowUtil showUtil;

    @PostMapping(value = "/api/adminLogin")
    public String adminLogin(@Param("id") String id,@Param("pwd") String pwd){
        return adminService.isAdminExist(id,pwd);
    }

    @GetMapping(value = "/api/maxSongid")
    public String maxSongid(){
        return adminService.getMaxSongid();
    }

    @GetMapping(value = "/api/maxAlbumid")
    public String maxAlbumid(){
        return adminService.getMaxAlbumid();
    }

    @GetMapping(value = "/api/maxSingerid")
    public String maxSingerid(){
        return adminService.getMaxSingerid();
    }

    @GetMapping(value = "/api/getSingeridByName")
    public String getSingeridByName(@Param("name") String name){
        Singer s=adminService.getSingerByName(name);
        return s.getSingerid();
    }

    @GetMapping(value = "/api/getAlbumidBySAName")
    public String getAlbumidBySAName(@Param("sname") String sname,@Param("aname") String aname){
        return adminService.getAlbumidBySAName(sname,aname);
    }

    @PostMapping(value = "/api/addSong")
    public String addSong(
    @Param("id") String id,
    @Param("name") String name,
    @Param("albumid") String albumid,
    @Param("length") String length,
    @Param("language") String language,
    @Param("school") String school,
    @Param("company") String company,
    @Param("age") String age,
    @Param("adminid") String adminid
    ){
        Song s=new Song();
        s.setAdminid(adminid);
        s.setAlbumid(albumid);
        s.setCompany(company);
        s.setLanguage(language);
        s.setLength(length);
        s.setSongage(age);
        s.setSongid(id);
        s.setSongschool(school);
        s.setSongname(name);
        return (String)adminService.addSong(s);
    }

    @PostMapping(value = "/api/addAlbum")
    public String addAlbum(
    @Param("id") String id,
    @Param("name") String name,
    @Param("language") String language,
    @Param("school") String school,
    @Param("company") String company,
    @Param("age") String age,
    @Param("adminid") String adminid,
    @Param("singerid") String singerid
    ){
        Album a=new Album();
        a.setAdminid(adminid);
        a.setAlbumid(id);
        a.setCompany(company);
        a.setLanguage(language);
        a.setAlbumage(age);
        a.setSingerid(singerid);
        a.setAlbumname(name);
        return (String)adminService.addAlbum(a);
    }

    @PostMapping(value="/api/addSinger")
    public String addSinger(
    @Param("id") String id,
    @Param("name") String name,
    @Param("sex") String sex,
    @Param("region") String region,
    @Param("intro") String intro,
    @Param("adminid") String adminid
    ){
        Singer s = new Singer();
        s.setSingerid(id);
        s.setSingername(name);
        s.setSingersex(sex);
        s.setRegion(region);
        s.setIntroduction(intro);
        s.setAdminid(adminid);
        return (String)adminService.addSinger(s);
    }

    @GetMapping(value="/api/searchDeleteSong")
    public Song searchDeleteSong(@Param("singername") String singername,@Param("albumname") String albumname,
    @Param("songname") String songname){
        return (Song)adminService.getDeleteSong(singername,albumname,songname);
    }

    @GetMapping(value="/api/searchDeleteAlbum")
    public Album searchDeleteAlbum(@Param("singername") String singername,@Param("albumname") String albumname){
        return (Album)adminService.getDeleteAlbum(singername,albumname);
    }

    @GetMapping(value="/api/searchDeleteSinger")
    public Singer searchDeleteSinger(@Param("singername") String singername){
        return (Singer)adminService.getDeleteSinger(singername);
    }

    @DeleteMapping(value="/api/deleteSong")
    public Object deleteSong(@Param("songid") String songid){
        return adminService.deleteSong(songid);
    }

    @DeleteMapping(value="/api/deleteAlbum")
    public Object deleteAlbum(@Param("albumid") String albumid){
        return adminService.deleteAlbum(albumid);
    }

    @DeleteMapping(value="/api/deleteSinger")
    public Object deleteSinger(@Param("singerid") String singerid){
        return adminService.deleteSinger(singerid);
    }
}
