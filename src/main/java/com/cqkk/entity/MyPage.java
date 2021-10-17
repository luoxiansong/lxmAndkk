package com.cqkk.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyPage<T>{

    private Integer current;
    private Integer size;
    private Long total;
    public List<T> lists;
}
