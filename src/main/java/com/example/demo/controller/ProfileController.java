package com.example.demo.controller;


import com.example.demo.entity.Singer;
import com.example.demo.entity.User;
import com.example.demo.entity.result.ResultEntity;
import com.example.demo.service.SingerService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;
    @Autowired
    private SingerService singerService;

    @RequestMapping(value = "/profile/like_song", method = RequestMethod.GET)
    public String mainPage(HttpServletRequest request,Map<String, Object> map) {
        ResultEntity e;
        User user = (User) request.getSession(false).getAttribute("visted");
        e = userService.getFans(user);
        ArrayList<User> fans = (ArrayList<User>)e.getObject();
        e= userService.getFriends(user);
        ArrayList<User> friends = (ArrayList<User>)e.getObject();
        ArrayList<Singer> singers = singerService.getSingerUserLike(user.getUserid());
        map.put("FansNum",fans.size());
        map.put("FriendsNum",friends.size()+singers.size());
        return "profile/like_song";
    }
}
