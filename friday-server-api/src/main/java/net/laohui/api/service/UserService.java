package net.laohui.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.laohui.api.bean.User;

import java.util.List;

public interface UserService {
    // 账号校验
    public User checkUser(String userName, String passWord);
    // 根据用户名查询用户
    public User getUserByUserName(String userName);
    // 根据用户ID查询用户
    public User getUserByUserId(Integer userId);
    // 获取用户列表
    public List<User> getUserList();
    // 根据条件获取用户列表
    public List<User> getUserListByWhere(QueryWrapper<User> queryWrapper);
    // 获取用户数量
    public Integer countUser();
    // 根据条件获取用户数量
    public Integer countUserByWhere(QueryWrapper<User> queryWrapper);
    // 添加用户
    public boolean addUser(User data);
    // 更新用户
    public boolean updateUser(Integer userId,User data);
    // 删除用户
    public boolean deleteUser(Integer userId, String userName);

    List<User> getUserList(Integer page, Integer size);

    Object getUserCount();

    boolean deleteUser(User user);
}
