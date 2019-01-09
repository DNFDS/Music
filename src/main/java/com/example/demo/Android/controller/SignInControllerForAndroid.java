package com.example.demo.Android.controller;

import com.example.demo.entity.*;
import com.example.demo.entity.result.ResultEntity;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SignInControllerForAndroid {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/Android/login", method = RequestMethod.POST)
    public Map login(@RequestParam("userid") String userid,@RequestParam("pwd") String pwd){
        User user = new User();
        user.setUserid(userid);
        user.setUserpassword(pwd);
        Map<String,Object> map = new HashMap<>();
        ResultEntity result = userService.SignIn(user);
        if(result.getSuccess()){
            user = (User) result.getObject();
            map.put("user",user);
        }
        map.put("msg",result.getErrorMsg());
        map.put("succ",result.getSuccess());
        return map;
    }



}
