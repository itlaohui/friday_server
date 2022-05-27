package net.laohui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.log4j.Log4j2;
import net.laohui.api.bean.domain.User;
import net.laohui.api.service.UserService;
import net.laohui.dao.UserMapper;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@com.alibaba.dubbo.config.annotation.Service(version = "1.0.0", timeout = 60000)
@Transactional
public class UserServiceImpl implements UserService {

     UserMapper userMapper;

    UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User checkUser(String userName, String passWord) {
        log.info("------------------------> checkUser: " + userName + " " + passWord);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = new User();
        user.setUser_account_name(userName);
        user.setUser_account_password(passWord);
        queryWrapper.setEntity(user);
        return userMapper.selectOne(queryWrapper);
    }

    @Cacheable(cacheNames = "user", key = "#userName")
    @Override
    public User getUserByUserName(String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = new User();
        user.setUser_account_name(userName);
        queryWrapper.setEntity(user);
        return userMapper.selectOne(queryWrapper);
    }

    @Cacheable(cacheNames = "user", key = "#userId")
    @Override
    public User getUserByUserId(Integer userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = new User();
        user.setUser_id(userId);
        queryWrapper.setEntity(user);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public List<User> getUserList() {
        return userMapper.selectList(null);
    }

    @Override
    public List<User> getUserListByWhere(QueryWrapper<User> queryWrapper) {
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public Integer countUser() {
        return userMapper.selectCount(null);
    }

    @Override
    public Integer countUserByWhere(QueryWrapper<User> queryWrapper) {
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    public User addUser(User data) {
        return userMapper.insert(data) > 0 ? data : null;
    }

    @CachePut(cacheNames = "user", key = "#userId")
    @Override
    public User updateUserByUserId(Integer userId, User data) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = new User();
        user.setUser_id(userId);
        queryWrapper.setEntity(user);
        if (userMapper.update(data, queryWrapper) > 0) {
            return userMapper.selectOne(queryWrapper);
        } else {
            return null;
        }
    }

    @Override
    public User updateUserByUserName(String userName, User data) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = new User();
        user.setUser_account_name(userName);
        queryWrapper.setEntity(user);
        return userMapper.update(data, queryWrapper) > 0 ? data : null;
    }


    @Override
    public boolean deleteUser(Integer userId, String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = new User();
        user.setUser_id(userId);
        user.setUser_account_name(userName);
        queryWrapper.setEntity(user);
        return userMapper.delete(queryWrapper) > 0;
    }

    public List<User> getUserList(Integer page, Integer size) {
        log.info("------------------------> getUserList: " + page + " " + size);
        return userMapper.selectUserList(page, size);
    }

    @Override
    public boolean deleteUser(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(user);
        return userMapper.delete(queryWrapper) > 0;
    }

    @Override
    public Integer getUserCount() {
        return userMapper.selectCount(null);
    }
}
