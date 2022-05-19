package net.laohui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.laohui.mapper.UserMapper;
import net.laohui.pojo.User;
import net.laohui.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User checkUser(String userName, String passWord) {
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
    public boolean updateUser(Integer userId, User data) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = new User();
        user.setUser_id(userId);
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
