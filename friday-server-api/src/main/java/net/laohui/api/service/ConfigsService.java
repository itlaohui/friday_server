package net.laohui.api.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.laohui.api.bean.ConfigExt;
import net.laohui.api.bean.domain.Configs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;


@Service
public interface ConfigsService {
    Configs getConfig(String name);
    Page<Configs> getConfigList(@Param("keywords") String keywords, @Param("page") int page, @Param("size") int size);
    Page<ConfigExt> getConfigExtList(@Param("keywords") String keywords, @Param("page") int page, @Param("size") int size);

}
