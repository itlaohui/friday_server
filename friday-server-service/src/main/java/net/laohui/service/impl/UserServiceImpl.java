package net.laohui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.log4j.Log4j2;
import net.laohui.api.bean.User;
import net.laohui.api.service.UserService;
import net.laohui.dao.UserMapper;
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

    @Override
    public User getUserByUserName(String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = new User();
        user.setUser_account_name(userName);
        queryWrapper.setEntity(user);
        return userMapper.selectOne(queryWrapper);
    }

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
    public boolean addUser(User data) {
        return userMapper.insert(data) > 0;
    }

    @Override
    public boolean updateUserByUserId(Integer userId, User data) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = new User();
        user.setUser_id(userId);
        queryWrapper.setEntity(user);
        return userMapper.update(data, queryWrapper) > 0;
    }

    @Override
    public boolean updateUserByUserName(String userName, User data) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = new User();
        user.setUser_account_name(userName);
        queryWrapper.setEntity(user);
        return userMapper.update(data, queryWrapper) > 0;
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

    public User getUserById(Integer id) {
        User user = new User();
        user.setUser_id(1);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(user);
        return userMapper.selectOne(queryWrapper);
    }

    public User getUserByAccountName(String accountName) {
        return new User();
    }

    public User getUserByEmail(String Email) {
        return new User();
    }

    public Integer updateUserById(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getUser_id());
        return userMapper.update(user,queryWrapper);
    }

    public Integer insertUser(User user) {
        return userMapper.insert(user);
    }

    public boolean hasUser(String username) {
        return userMapper.hasUser(username);
    }

    public boolean deleteUser(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(user);
        int delete = userMapper.delete(queryWrapper);
        return delete > 0;
    }

    public Integer getUserCount() {
        return userMapper.selectCount(null);
    }
}
