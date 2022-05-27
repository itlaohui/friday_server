package net.laohui.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.laohui.api.bean.ConfigExt;
import net.laohui.api.bean.domain.Configs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConfigsMapper extends BaseMapper<Configs> {
    Configs getConfig(@Param("name") String name);
    List<ConfigExt> getConfigExtList(@Param("keywords") String keywords, @Param("page") int page, @Param("size") int size);
}
