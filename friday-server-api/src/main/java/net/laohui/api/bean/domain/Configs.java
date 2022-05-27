package net.laohui.api.bean.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.laohui.api.bean.ConfigExt;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName(value = "f_configs")
public class Configs implements Serializable {
    @Id
    private Integer id;
    private Integer cid;
    private String name;
    private String title;
    private String create_found;
    private Date create_time;
    private Date update_time;
    private boolean need_login;
    @TableField(exist = false)
    private List<ConfigExt> config_list;
}
