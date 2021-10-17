package com.cqkk.service;

import com.cqkk.entity.Sing;

import java.util.List;

public interface SingService {

    /*根据歌手ID查询歌手演唱的歌曲*/
    List<Sing> getSingListBySingerId(Integer SingerId);
}
