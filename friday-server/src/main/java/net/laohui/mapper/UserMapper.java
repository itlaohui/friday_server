package net.laohui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.laohui.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 用以检测用户是否存在
     *
     * @return boolean 该用户是否存在
     */
    @Select("SELECT COUNT(`user_id`) as count FROM f_users WHERE user_account_name = #{username}")
    boolean hasUser(@Param("username") String username);

    List<User> selectUserList(@Param("page") int page, @Param("size") int size);

}
