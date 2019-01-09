package com.example.demo.Android.controller;

import com.example.demo.Android.entity.AndroidUser;
import com.example.demo.util.AutoShowUtil;
import com.example.demo.entity.User;
import com.example.demo.entity.result.ResultEntity;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FriendControllerForAndroid {

    @Autowired
    private UserService userService;
    @Autowired
    private AutoShowUtil showUtil;

    @RequestMapping(value = "/Android/showFollowedUser", method = RequestMethod.POST)
    public Map showFollowedUser(@RequestParam("userid") String userid){
        User user = new User();
        user.setUserid(userid);
        ResultEntity e = userService.getFriends(user);
        ArrayList<User> friends = (ArrayList<User>)e.getObject();
        ArrayList<AndroidUser> users = new ArrayList<>();
        for(User friend:friends){
            AndroidUser a = new AndroidUser();
            a.setUserid(friend.getUserid());
            a.setUsername(friend.getUsername());
            a.setUserimage(friend.getUserimage());
            a.setIsvip(friend.getIsvip());
            users.add(a);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("FollowedUser",users);
        return map;
    }

    @RequestMapping(value = "/Android/followUser", method = RequestMethod.POST)
    public Map followUser(@RequestParam("userid") String userid,@RequestParam("otherid") String otherid){
        return showUtil.changeFollow(userid,otherid);
    }
}
