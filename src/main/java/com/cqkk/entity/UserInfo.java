package com.cqkk.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("USER")
@ApiModel("用户实体类")
@Data
@NoArgsConstructor
public class UserInfo {

    /*用户ID*/
    @ApiModelProperty("用户ID，自动生成，不能重复")
    @TableField("ID")
    private Integer id;

    /*用户名*/
    @ApiModelProperty("用户名")
    @TableField("USERNAME")
    private String username;

    /*密码*/ 
    @ApiModelProperty("密码")
    @TableField("PASSWORD")
    private String password;

    /*角色*/
    @ApiModelProperty("角色")
    @TableField("ROLE")
    private String role;

    public UserInfo(Integer id) {
        this.id = id;
    }

    public UserInfo(Integer id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
