package com.example.demo.userInterface;

import com.example.demo.entity.Song;
import com.example.demo.entity.SongList;
import com.example.demo.entity.User;
import com.example.demo.entity.result.ResultEntity;
import com.example.demo.service.*;
import com.example.demo.util.AutoShowUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SongListInterface {
    @Autowired
    private UserService userService;
    @Autowired
    private SongListService songListService;
    @Autowired
    private KeepService keepService;
    @Autowired
    private AutoShowUtil showUtil;


    @GetMapping(value = "/api/getSongListSavedNum")
    public Object getSongListSavedNum(@Param("songlistid") String songlistid){
        return songListService.getSongListSavedNum(songlistid).getObject();
    }

    @GetMapping(value = "/api/getNumOfSongList")
    public Object getNumOfSongList(@Param("songlistid") String songlistid){
        ArrayList<Song> songs = (ArrayList<Song>)songListService.getSongsInSongList(songlistid).getObject();
        return songs.size();
    }

    @RequestMapping(value = "/profile/like_song_songlist_typeList", method = RequestMethod.GET)
    public ModelAndView showKeepedSongList(HttpServletRequest request, HttpServletResponse response){
        User user = (User) request.getSession(false).getAttribute("visted");
        String flag = request.getParameter("flag");
        //如果请求的form页面，就直接返回
        //list页面，要额外添加歌单的收藏数和曲目数
        ArrayList<SongList> keepedSongList = userService.getKeepedSongList(user.getUserid());
        //得到所有歌单的曲目数和收藏数创建人  songnum  savenum username
        Map<String,Object> map = showUtil.showSongList(  keepedSongList);
        if(flag.equals("1"))
            return new ModelAndView("temp/mylike/songlist_details",map);
        if(flag.equals("2"))
            return new ModelAndView("temp/mylike/songlist_form_details",map);
        return new ModelAndView("temp/mylike/songlist_list_details",map);
    }

    @RequestMapping(value = "/profile/showCreatedSongList", method = RequestMethod.GET)
    public ModelAndView showCreatedSongList(HttpServletRequest request, HttpServletResponse response){
        User user = (User) request.getSession(false).getAttribute("visted");
        //如果请求的form页面，就直接返回
        //list页面，要额外添加歌单的收藏数和曲目数
        ArrayList<SongList>CreatedSongList = userService.getCreatedSongList(user.getUserid());
        //得到所有歌单的曲目数和收藏数创建人  songnum  savenum username
        Map<String,Object>map = showUtil.showSongList(CreatedSongList);
        return new ModelAndView("temp/created_main",map);
    }

    @RequestMapping(value = "/KeepSongList",method = RequestMethod.POST)
    public ResultEntity KeepSongList(@Param("songlistid")String songlistid, HttpServletRequest request){
        User user = (User) request.getSession(false).getAttribute("user");
        String result = keepService.KeepSongList(songlistid,user.getUserid());
        boolean succ = true;
        if(result.equals("0")){
            succ = false;
            return new ResultEntity(succ,"歌单已收藏",null);
        }
        return new ResultEntity(succ,"收藏成功",null);
    }

    @RequestMapping(value = "/createNewSonglist",method = RequestMethod.POST)
    public ResultEntity createNewSonglist(@Param("name")String name, @Param("isprivate")String isprivate,@Param("image")String image, HttpServletRequest request) {
        User user = (User) request.getSession(false).getAttribute("user");
        String id = songListService.createNewSongList(name,image,isprivate,user.getUserid());
        return new ResultEntity(true,"您新创建的歌单id为: "+id,null);
    }

    @RequestMapping(value = "/getMySongList", method = RequestMethod.GET)
    public ModelAndView getMySongList(HttpServletRequest request, @RequestParam("songid") String songid){
        User user = (User) request.getSession(false).getAttribute("user");
        ResultEntity e = userService.getSongLists(user);
        Map<String,Object>e_map = (Map<String, Object>) e.getObject();
        ArrayList<SongList> createdsonglist = (ArrayList<SongList>)e_map.get("createdsonglist");
        Map<String,Object>map = new HashMap<>();
        map.put("songlists",createdsonglist);
        map.put("toAdd",songid);
        return new ModelAndView("temp/songListChooser",map);
    }
}
