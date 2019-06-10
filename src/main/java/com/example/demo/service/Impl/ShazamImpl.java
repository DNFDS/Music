package com.example.demo.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.example.demo.dao.HashMapper;
import com.example.demo.service.ShazamService;
import com.example.demo.service.Shazam.hash.Hash;

import org.springframework.stereotype.Service;

@Service
public class ShazamImpl implements ShazamService{
    @Resource
    private HashMapper hMapper;

    @Override
    public String insertHash(int hashid,String songid,int offset){
        Map<String,Object> map = new HashMap<>();
        map.put("hashid",hashid);
        map.put("songid",songid);
        map.put("offset",offset);
        hMapper.insertHash(map);
        return (String)map.get("succ");
    }

    @Override
    public void buildIndex(){
        hMapper.buildIndex();
    }

    @Override
    public String getSongName(String songid){
        Map<String,Object> map = new HashMap<>();
        map.put("songid",songid);
        hMapper.getSongName(map);
        return (String)map.get("songname");
    }

    @Override
    public ArrayList<Hash> selectHash(int hashid){
        Map<String,Object> map = new HashMap<>();
        map.put("hashid",hashid);
        hMapper.selectHash(map);
        return (ArrayList<Hash>)map.get("hashs");
    }

    @Override
    public int insertSong(String songName){
        Map<String,Object> map = new HashMap<>();
        map.put("songname",songName);
        hMapper.insertSong(map);
        return Integer.parseInt(map.get("songid").toString());
    }
}