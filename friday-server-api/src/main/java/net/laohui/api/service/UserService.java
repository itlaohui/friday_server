package net.laohui.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.laohui.api.bean.User;

import java.util.List;

public interface UserService {
    // 账号校验
    User checkUser(String userName, String passWord);
    // 根据用户名查询用户
    User getUserByUserName(String userName);
    // 根据用户ID查询用户
    User getUserByUserId(Integer userId);
    // 获取用户列表
    List<User> getUserList();
    // 获取用户列表(分页)
    List<User> getUserList(Integer page, Integer size);
    // 根据条件获取用户列表
    List<User> getUserListByWhere(QueryWrapper<User> queryWrapper);
    // 获取用户数量
    Integer countUser();
    // 获取用户数量
    Object getUserCount();
    // 根据条件获取用户数量
    Integer countUserByWhere(QueryWrapper<User> queryWrapper);
    // 添加用户
    boolean addUser(User data);
    // 更新用户(根据用户ID)
    boolean updateUserByUserId(Integer userId, User data);
    // 更新用户(根据用户名)
    boolean updateUserByUserName(String userName, User data);
    // 删除用户
    boolean deleteUser(Integer userId, String userName);
    // 删除用户
    boolean deleteUser(User user);
}
