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
public class NewsControllerForAndroid {
    @Autowired
    private UserService userService;
    @Autowired
    private SongListService songListService;
    @Autowired
    private SongService songService;
    @Autowired
    private NewsService newsService;

    @RequestMapping(value = "/Android/showNews", method = RequestMethod.POST)
    public Map showNews(@RequestParam("userid") String userid){
        ArrayList<News> news = newsService.getAllNews(userid);
        ArrayList<Boolean>isLike = new ArrayList<>();
        ArrayList<Integer>likeNum = new ArrayList<>();
        ArrayList<Integer>forwardNum = new ArrayList<>();
        ArrayList<Integer>commentNum = new ArrayList<>();
        ArrayList<String>name = new ArrayList<>();
        ArrayList<String>creater = new ArrayList<>();
        ArrayList<String>muser = new ArrayList<>();
        ArrayList<String>forward = new ArrayList<>();
        Map<String,Object > map = new HashMap<>();
        for(News news1:news){
            ResultEntity e = userService.getUserById(news1.getCreaterid());
            User user = (User) e.getObject();
            muser.add(user.getUsername());
            if(news1.getForwarder()!=null){
                e = userService.getUserById(news1.getForwarder());
                user = (User) e.getObject();
                forward.add(user.getUsername());
            }
            isLike.add(newsService.isUserLikeNews(userid,news1.getId()));
            likeNum.add(newsService.getNewsLike(news1.getId()).size());
            forwardNum.add(newsService.getNewsForward(news1.getId()).size());
            commentNum.add(newsService.getNewsComment(news1.getId()).size());
            switch (news1.getType()){
                case "歌单":
                    SongList contain = songListService.getSongListById(news1.getContentid());
                    name.add(contain.getSonglistname());
                    e = userService.getUserById(contain.getUserid());
                    user = (User) e.getObject();
                    creater.add(user.getUsername());
                    break;
                case "单曲":
                    Song song = songService.getSongById(news1.getContentid());
                    name.add(song.getSongname());
                    e  = songService.getSingersInSong(song.getSongid());
                    ArrayList<Singer> singers = (ArrayList<Singer>)e.getObject();
                    creater.add(singers.get(0).getSingername());
                    break;
                case "专辑":
                    break;
            }
        }
        map.put("forward",forward);
        map.put("muser",muser);
        map.put("name",name);
        map.put("creator",creater);
        map.put("news",news);
        map.put("isLike",isLike);
        map.put("likeNum",likeNum);
        map.put("forwardNum",forwardNum);
        map.put("commentNum",commentNum);
        return map;
    }

    @RequestMapping(value = "/Android/likeNews", method = RequestMethod.POST)
    public Map likeNews(@RequestParam("userid") String userid,@RequestParam("newsid") String newsid){
        Map<String,Object > map = new HashMap<>();
        if(newsService.isUserLikeNews(userid,newsid)){
            newsService.dislikeNews(userid,newsid);
            map.put("flag","2");
        }
        else {
            newsService.likeNews(userid,newsid);
            map.put("flag","1");
        }
        return map;
    }

    @RequestMapping(value = "/Android/createNewsComment", method = RequestMethod.POST)
    public Map createNewsComment(@RequestParam("userid") String userid,@RequestParam("newsid") String newsid,@RequestParam("text") String text){
        Map<String,Object > map = new HashMap<>();
        ResultEntity e = userService.getUserById(userid);
        User user = (User) e.getObject();
        map.put("username",user.getUsername());
        map.put("cid",newsService.commentNews(userid,newsid,text));
        map.put("msg","评论成功嗷");
        return map;
    }

    @RequestMapping(value = "/Android/answerComment", method = RequestMethod.POST)
    public Map answerComment(@RequestParam("userid") String userid,@RequestParam("commentid") String commentid,@RequestParam("text") String text){
        Map<String,Object > map = new HashMap<>();
        ResultEntity e = userService.getUserById(userid);
        User user = (User) e.getObject();
        map.put("username",user.getUsername());
        map.put("cid",newsService.answerComment(userid,commentid,text));
        map.put("msg","评论成功嗷");
        return map;
    }

    @RequestMapping(value = "/Android/createNews", method = RequestMethod.POST)
    public Map createNews(@RequestParam("userid") String userid,@RequestParam("type") String type,@RequestParam("contentid") String contentid,@RequestParam("text") String text){
        Map<String,Object > map = new HashMap<>();
        ResultEntity e = userService.getUserById(userid);
        User user = (User) e.getObject();
        map.put("username",user.getUsername());
        map.put("nid",newsService.createNews(userid,type,contentid,text));
        map.put("msg","创建动态成功");
        return map;
    }

    @RequestMapping(value = "/Android/deleteNews", method = RequestMethod.POST)
    public Map deleteNews(@RequestParam("nid") String nid){
        Map<String,Object > map = new HashMap<>();
        newsService.deleteNews(nid);
        map.put("msg","动态删除成功");
        return map;
    }

    @RequestMapping(value = "/Android/deleteComment", method = RequestMethod.POST)
    public Map deleteComment(@RequestParam("cid") String cid){
        Map<String,Object > map = new HashMap<>();
        newsService.deleteNewsComment(cid);
        map.put("msg","评论删除成功");
        return map;
    }

    @RequestMapping(value = "/Android/showNewsDetail", method = RequestMethod.POST)
    public Map showNewsDetail(@RequestParam("nid") String nid,@RequestParam("uid") String uid){
        Map<String,Object > map = new HashMap<>();
        ArrayList<NewsComment>comments = newsService.getNewsComment(nid);
        ArrayList<User> likeUser = newsService.getNewsLike(nid);
        ArrayList<User> forwardUser = newsService.getNewsForward(nid);
        ArrayList<String> commentUser = new ArrayList<>();
        ArrayList<String> parenter = new ArrayList<>();
        ArrayList<Boolean> isLike = new ArrayList<>();
        ArrayList<Integer>likeNum = new ArrayList<>();
        for(NewsComment comment:comments){
            if(comment.getParenter()!=null){
                ResultEntity e = userService.getUserById(comment.getParenter());
                User user = (User) e.getObject();
                parenter.add(user.getUsername());
            }
            ResultEntity e = userService.getUserById(comment.getCommenterid());
            User user = (User) e.getObject();
            commentUser.add(user.getUsername());
            likeNum.add(newsService.getCommentLikeNum(comment.getCommentid()));
            isLike.add(newsService.isUserLikeComment(uid,comment.getCommentid()));
        }
        ArrayList<String>like = new ArrayList<>();
        ArrayList<String>forward = new ArrayList<>();
        for (User user:likeUser){
            like.add(user.getUsername());
        }
        for(User user:forwardUser){
            forward.add(user.getUsername());
        }
        map.put("parenter",parenter);
        map.put("likeNum",likeNum);
        map.put("comments",comments);
        map.put("commentUser",commentUser);
        map.put("like",like);
        map.put("forward",forward);
        map.put("isLike",isLike);
        return map;
    }

    @RequestMapping(value = "/Android/likeComment", method = RequestMethod.POST)
    public Map likeComment(@RequestParam("cid") String cid,@RequestParam("uid") String uid){
        Map<String,Object > map = new HashMap<>();
        if(newsService.isUserLikeComment(uid,cid)){
            newsService.dislikeComment(uid,cid);
            map.put("flag","2");
        }
        else {
            newsService.likeComment(uid,cid);
            map.put("flag","1");
        }
        return map;
    }

    @RequestMapping(value = "/Android/forwardNews", method = RequestMethod.POST)
    public Map forwardNews(@RequestParam("nid") String nid,@RequestParam("uid") String uid,@RequestParam("text") String text){
        Map<String,Object > map = new HashMap<>();
        newsService.forwardNews(uid,nid,text);
        map.put("msg","转发成功了嗷");
        return map;
    }
}
