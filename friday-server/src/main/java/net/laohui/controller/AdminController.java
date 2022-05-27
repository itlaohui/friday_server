package net.laohui.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.log4j.Log4j2;
import net.laohui.api.bean.ConfigExt;
import net.laohui.enumerate.UserStatusEnum;
import net.laohui.exception.OperationException;
import net.laohui.api.bean.domain.Configs;
import net.laohui.api.bean.domain.User;
import net.laohui.api.service.ConfigsService;
import net.laohui.api.service.UserService;
import net.laohui.util.ResponseResult;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Log4j2
@RequiresAuthentication
@RestController
@RequestMapping(value = "admin")
public class AdminController {

    @Reference(version = "1.0.0", timeout = 60000)
    UserService userService;
    @Reference(version = "1.0.0", timeout = 60000)
    ConfigsService configsService;
    HttpServletRequest request;
    HttpServletResponse response;


    @Autowired
    public void setVariables(HttpServletRequest request,
                             HttpServletResponse response) {

        this.request = request;
        this.response = response;
    }

    /**
     * 添加用户
     *
     * @return
     */
    @RequestMapping(value = "addUser", method = {RequestMethod.POST})
    public ResponseResult<User> addUser() {
        String user_account_name = request.getParameter("user_account_name");
        String user_password = request.getParameter("user_account_password");
        String user_nickname = request.getParameter("user_nickname");
        String user_sex = request.getParameter("user_sex");
        String user_phone_number = request.getParameter("user_phone_number");
        String user_email = request.getParameter("user_email");
        String user_status = request.getParameter("user_status");
        User user = new User();
        user.setUser_account_name(user_account_name);
        user.setUser_account_password(user_password);
        user.setUser_nickname(user_nickname);
        user.setUser_sex(isBlank(user_sex) ? 2 : Integer.parseInt(user_sex));
        user.setUser_email(user_email);
        user.setUser_phone_number(user_phone_number);
        try {
            UserStatusEnum.valueOf(user_status);
            user.setUser_status(user_status);
        } catch (Exception e) {
            log.error("user_status is not valid");
        }
        log.info("user:{}", user_account_name);
        if (userService.addUser(user) != null) {
            return ResponseResult.success("添加成功", user);
        }
        return ResponseResult.error(400, "添加失败");
    }

    /**
     * 获取用户列表
     *
     * @return
     */
    @RequestMapping(value = "getUserList", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseResult<JSONObject> getUserList() {
        Integer page = isBlank(request.getParameter("page")) ? 1 : Integer.parseInt(request.getParameter("page"));
        Integer size = isBlank(request.getParameter("size")) ? 20 : Integer.parseInt(request.getParameter(
                "size"));
        size.equals(0);
        System.out.println(page + " " + size);
        List<User> userList = userService.getUserList(page, size);
        JSONObject response = new JSONObject();
        response.put("data", userList);
        response.put("count", userService.getUserCount());
        return ResponseResult.success(response);
    }

    /**
     * 批量删除用户
     *
     * @return
     */
    @PostMapping(value = "batchDeleteUsers")
    public ResponseResult<String> batchDeleteUsers() {
        String userIds = request.getParameter("data");
        JSONArray objects = JSONObject.parseArray(userIds);
        AtomicInteger count = new AtomicInteger();
        objects.forEach(o -> {
            JSONObject parse = (JSONObject) JSONObject.parse(o.toString());
            User user = new User();
            user.setUser_id(parse.getInteger("id"));
            user.setUser_account_name(parse.getString("user_account_name"));
            if (userService.deleteUser(user)) {
                count.getAndIncrement();
            }
        });
        return ResponseResult.success(count.intValue() + "条数据删除成功", null);
    }

    /**
     * 删除用户
     *
     * @return
     */
    @GetMapping(value = "deleteUser")
    public ResponseResult<String> deleteUser() {
        String user_id = request.getParameter("user_id");
        User user = new User();
        user.setUser_id(Integer.parseInt(user_id));
        if (userService.deleteUser(user)) {
            return ResponseResult.success("删除成功", null);
        }
        return ResponseResult.error(400, "删除失败");
    }

    /**
     * 更新用户信息
     *
     * @return
     */
    @PostMapping(value = "updateUser")
    public ResponseResult<User> updateUser() {
        String user_id = request.getParameter("user_id");
        String user_account_name = request.getParameter("user_account_name");
        String user_password = request.getParameter("user_password");
        String user_nickname = request.getParameter("user_nickname");
        String user_email = request.getParameter("user_email");
        String user_phone_number = request.getParameter("user_phone_number");
        String user_status = request.getParameter("user_status");
        String user_sex = request.getParameter("user_sex");
        String user_role = request.getHeader("user_role");
        User user = new User();
        user.setUser_id(Integer.parseInt(user_id));
        user.setUser_account_name(user_account_name);
        user.setUser_account_password(user_password);
        user.setUser_nickname(user_nickname);
        user.setUser_email(user_email);
        user.setUser_phone_number(user_phone_number);
        user.setUser_status(user_status);
        user.setUser_role(user_role);
        try {
            Integer sex = Integer.parseInt(user_sex);
            user.setUser_sex(sex);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            if (userService.updateUserByUserId(user.getUser_id(), user) == null) {
                return ResponseResult.error(400, "更新失败");
            }
        } catch (DuplicateKeyException e) {
            if (e.getCause().toString().contains("SQLIntegrityConstraintViolationException")) {
                throw new OperationException("用户名已存在");
            }
        } catch (Exception e) {
            return ResponseResult.error(400, String.format("修改失败！%s", e.getMessage()));
        }
        return ResponseResult.success("修改成功", user);
    }

    @GetMapping(value = "getConfigList")
    public ResponseResult<Object> getConfigList(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "keywords", required = false) String keywords)
    {
        if (page == null) {
            page = 1;
        }
        if (size == null) {
            size = 20;
        }
        if (keywords == null || keywords.equals("")) {
            keywords = "";
        }
        Page<Configs> configList = configsService.getConfigList(keywords, page, size);
        JSONObject response = new JSONObject();
        response.put("count", configList.getSize());
        response.put("data", configList.getRecords());
        return ResponseResult.success("查询成功", response);
    }

    @GetMapping(value = "getConfigExtList")
    public ResponseResult<Object> getConfigExtList(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "keywords", required = false) String keywords
    ) {
        if (page == null) {
            page = 1;
        }
        if (size == null) {
            size = 20;
        }
        if (keywords == null || keywords.equals("")) {
            keywords = "";
        }
        Page<ConfigExt> configList = configsService.getConfigExtList(keywords, page, size);
        JSONObject response = new JSONObject();
        response.put("count", configList.getSize());
        response.put("data", configList.getRecords());
        return ResponseResult.success("查询成功", response);
    }
}
