package net.laohui.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.laohui.pojo.User;

import java.util.List;

public class TestUserService implements BaseService<User> {
    @Override
    public Integer insert(User data) {
        return null;
    }

    @Override
    public Integer insertList(List<User> rows) {
        return null;
    }

    @Override
    public Integer delete(QueryWrapper<User> queryWrapper) {
        return null;
    }

    @Override
    public Integer updateOne(QueryWrapper<User> queryWrapper, User data) {
        return null;
    }

    @Override
    public Integer updateList(QueryWrapper<User> queryWrapper, List<User> data) {
        return null;
    }

    @Override
    public List<User> selectList(QueryWrapper<User> queryWrapper) {
        return null;
    }

    @Override
    public User selectOne(QueryWrapper<User> queryWrapper) {
        return null;
    }
}
