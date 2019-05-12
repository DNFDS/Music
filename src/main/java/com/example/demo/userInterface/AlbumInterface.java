package com.example.demo.userInterface;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.service.AlbumService;

@CrossOrigin
@RestController
public class AlbumInterface {

    @Autowired
    AlbumService albumService;

    @RequestMapping(value ="/api/getAlbum",method = RequestMethod.GET)
    public Object getAlbum(@Param("albumid") String albumid){
        return albumService.getAlbumByAlbumId(albumid);
    }

}
