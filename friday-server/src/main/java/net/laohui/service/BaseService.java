package net.laohui.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

public interface BaseService<T> {
    Integer insert(T data);
    Integer insertList(List<T> rows);

    Integer delete(QueryWrapper<T> queryWrapper);

    Integer updateOne(QueryWrapper<T> queryWrapper, T data);
    Integer updateList(QueryWrapper<T> queryWrapper, List<T> data);

    List<T> selectList(QueryWrapper<T> queryWrapper);
    T selectOne(QueryWrapper<T> queryWrapper);


}
