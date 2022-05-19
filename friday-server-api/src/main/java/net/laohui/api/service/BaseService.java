package net.laohui.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

public interface BaseService<T> {
    // 插入数据
    Integer insert(T data);
    // 插入数据列表
    Integer insertList(List<T> rows);

    // 删除指定数据
    Integer delete(QueryWrapper<T> queryWrapper);

    // 更新一条数据
    Integer updateOne(QueryWrapper<T> queryWrapper, T data);
    // 更新数据列表
    Integer updateList(QueryWrapper<T> queryWrapper, List<T> data);

    // 查询数据列表
    List<T> selectList(QueryWrapper<T> queryWrapper);
    // 查询一条数据
    T selectOne(QueryWrapper<T> queryWrapper);

}
