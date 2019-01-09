package com.example.demo.userInterface;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AlbumInterface {

    @RequestMapping(value = "/profile/showAlbum", method = RequestMethod.GET)
    public ModelAndView showAlbum(HttpServletRequest request, HttpServletResponse response){
        String flag = request.getParameter("flag");
        if(flag.equals("1"))
            return new ModelAndView("/temp/mylike/album_details");
        if(flag.equals("2"))
            return new ModelAndView("/temp/mylike/album_form_details");
        return new ModelAndView("/temp/mylike/album_list_details");
    }
}
