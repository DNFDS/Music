package com.example.demo.userInterface;

import com.example.demo.entity.SongList;
import com.example.demo.entity.User;
import com.example.demo.entity.result.ResultEntity;
import com.example.demo.service.SongService;
import com.example.demo.util.AutoShowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

@RestController
public class BuyInterface {
    @Autowired
    private SongService songService;
    @Autowired
    private AutoShowUtil showUtil;

    @RequestMapping(value = "/profile/showMyBought", method = RequestMethod.GET)
    public ModelAndView showMyBught(HttpServletRequest request, HttpServletResponse response){
        User user = (User) request.getSession(false).getAttribute("visted");
        ResultEntity e = songService.getBoughtSongByUserId(user.getUserid());
        ArrayList<SongList> boughtSongList = (ArrayList<SongList>)e.getObject();
        Map<String,Object> map = showUtil.showSongList(boughtSongList);
        return new ModelAndView("temp/mybought_main",map);
    }

}
