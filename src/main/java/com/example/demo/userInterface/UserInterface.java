package com.example.demo.userInterface;

import com.example.demo.entity.User;
import com.example.demo.entity.result.ResultEntity;
import com.example.demo.service.SingerService;
import com.example.demo.service.UserService;
import com.example.demo.util.AutoShowUtil;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

@CrossOrigin
@RestController
public class UserInterface {

    @Autowired
    private UserService userService;
    @Autowired
    private SingerService singerService;
    @Autowired
    private AutoShowUtil showUtil;

    @GetMapping(value = "/api/getUser")
    public Object getUser(@Param("id") String id) {
        return userService.getUserById(id).getObject();
    }

    @GetMapping(value = "/api/getFans")
    public Object getFans(@Param("id") String id) {
        User user = new User();
        user.setUserid(id);
        ResultEntity e = userService.getFans(user);
        return e.getObject();
    }

    @GetMapping(value = "/api/getFriends")
    public Object getFollows(@Param("id") String id) {
        User user = new User();
        user.setUserid(id);
        ResultEntity e = userService.getFriends(user);
        return e.getObject();
    }

    @GetMapping(value = "/api/getFollowSinger")
    public Object getFollowSinger(@Param("id") String id) {
        User user = new User();
        user.setUserid(id);
        return singerService.getSingerUserLike(user.getUserid());
    }

    @GetMapping(value = "/api/getSongList")
    public Object getSongList(@Param("id") String id) {
        User user = new User();
        user.setUserid(id);
        return userService.getSongLists(user).getObject();
    }
    
    @RequestMapping(value ="/api/Login",method = RequestMethod.POST)
    public ResultEntity Login(@RequestParam("id") String id, @RequestParam("pwd")String pwd, HttpServletRequest request){
        User user = new User();
        user.setUserid(id);
        user.setUserpassword(pwd);
        ResultEntity result = userService.SignIn(user);
        if(result.getSuccess()){
            request.getSession().setAttribute("user",result.getObject());
            request.getSession().setAttribute("visted",result.getObject());
        }
        return result;
    }

    @RequestMapping(value = "/profile/showFans", method = RequestMethod.GET)
    public ModelAndView showFans(HttpServletRequest request, HttpServletResponse response){
        User user = (User) request.getSession(false).getAttribute("visted");
        User my =(User) request.getSession(false).getAttribute("user");
        Map<String,Object> map ;
        ResultEntity e = userService.getFans(user);
        ArrayList<User> Follows = (ArrayList<User>)e.getObject();
        //"Follows"关注的用户 "FollowNum"关注的人数 "isFollow"是否关注
        map = showUtil.showFollow(my.getUserid(),Follows);
        return new ModelAndView("temp/fans_main",map);
    }

    @RequestMapping(value = "/profile/showFollowUser", method = RequestMethod.GET)
    public ModelAndView showFollowUser(HttpServletRequest request, HttpServletResponse response){
        User user = (User) request.getSession(false).getAttribute("visted");
        User my =(User) request.getSession(false).getAttribute("user");
        String flag = request.getParameter("flag");
        ResultEntity e = userService.getFriends(user);
        ArrayList<User> Follows = (ArrayList<User>)e.getObject();
        //"Follows"关注的用户 "FollowNum"关注的人数 "isFollow"是否关注
        Map<String,Object> map = showUtil.showFollow(my.getUserid(),Follows);
        if(flag.equals("2"))
            return new ModelAndView("temp/follows/follow_user",map);
        return new ModelAndView("temp/follow_main",map);
    }

    @ResponseBody
    @RequestMapping(value = "/profile/changeFollow", method = RequestMethod.GET)
    public Map<String, String> changeFollow(HttpServletRequest request){
        User my =(User) request.getSession(false).getAttribute("user");
        String id = request.getParameter("id");
        return showUtil.changeFollow(my.getUserid(),id);
    }
}
