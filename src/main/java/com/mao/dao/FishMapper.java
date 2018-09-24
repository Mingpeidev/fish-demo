package com.mao.dao;

import com.mao.proj.Fish;

public interface FishMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Fish record);

    int insertSelective(Fish record);

    Fish selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Fish record);

    int updateByPrimaryKey(Fish record);
}