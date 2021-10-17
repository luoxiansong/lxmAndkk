package com.cqkk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("log")
public class MyLog {

    @TableId(value = "ID", type = IdType.AUTO)
    @ApiModelProperty("序号")
    private Integer id;

    @TableField(value = "URL_NAME")
    @ApiModelProperty("接口名称")
    private String urlName;

    @TableField(value = "URL")
    @ApiModelProperty("接口地址")
    private String url;

    @TableField(value = "ip")
    @ApiModelProperty("访问人IP")
    private String ip;

    @TableField(value = "USER_NAME")
    @ApiModelProperty("访问用户姓名")
    private String userName;

    @TableField(value = "ADD_DATE")
    @ApiModelProperty("访问时间")
    private Date addDate;

    @TableField(value = "STATUS")
    @ApiModelProperty("状态")
    private String status;

}
