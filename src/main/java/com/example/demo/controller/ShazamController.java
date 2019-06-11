package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.ShazamService;
import com.example.demo.service.Shazam.hash.CombineHash;
import com.example.demo.service.Shazam.hash.FFT;
import com.example.demo.service.Shazam.hash.Hash;
import com.example.demo.service.Shazam.pcm.*;
import com.example.demo.service.Shazam.search.Grader;
import com.example.demo.service.Shazam.search.SongScore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@RestController
@CrossOrigin
public class ShazamController {

    @Autowired
    private ShazamService ORMapping;

    public void MusicArchiver(){

    }

    /**
     * 上传音频文件
    */
    @PostMapping("/upload")
    public int upload(@RequestParam("file") MultipartFile file) {
        
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }
        // 格式转换
        try{
            File song = ShazamController.multipartToFile(file, file.getOriginalFilename());
            if (song != null) {
                long song_start = System.currentTimeMillis();
                System.out.println("Generating fingerprint for " + song.getName() + " ...");
                
                // register a song and get its id.
                String id = String.valueOf(ORMapping.insertSong(song.getName()));
                System.out.printf("Get id %d\n", id);
                
                // generate hashes.
                ArrayList<Hash> hashes = CombineHash.generateFingerprint(song, id, 1);

                for (Hash hash: hashes){
                    ORMapping.insertHash(hash.getHashID(), hash.song_id, hash.offset);
                }
                             
                // clear mem space.
                hashes = null;
                System.gc();
                
                long song_end = System.currentTimeMillis();
                System.out.printf("Finish generating fingerprints, time elapsed : %.2f!\n==============\n", (song_end-song_start)/1000.0);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return -1;
        }
        
        return 1;
    }

    public static File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
        multipart.transferTo(convFile);
        return convFile;
    }

    /**
     * 验证音频文件
    */
    @PostMapping("/searchSong")
    public int searchSong(@RequestParam("file") MultipartFile file) {
        
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }
        // 格式转换
        try{
            File song = ShazamController.multipartToFile(file, file.getOriginalFilename());
            if (song != null) {
                long start = System.currentTimeMillis();

                System.out.println("Extracting fingerprints ...");

                ArrayList<Hash> hashes = CombineHash.generateFingerprint(song, "", 1);

                System.out.println("Finish extracting fingerprints, start grading");

                // call the grading module
                ArrayList<SongScore> scores = Grader.grade(hashes);

                for (int i=0; i<5 && i<scores.size(); ++i) {
                    System.out.println(scores.get(i));
                }

                long end = System.currentTimeMillis();

                System.out.printf("Time elapsed %.2f sec.\n", (end-start)/1000.0);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return -1;
        }
        
        return 1;

    }

    @GetMapping("/testupload")
    public String index() {
        return "upload";
    }
}