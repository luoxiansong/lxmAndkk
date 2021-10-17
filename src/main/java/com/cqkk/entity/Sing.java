package com.cqkk.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: lxmAndkk
 * @description: 歌曲信息 sing
 * @author: luo kk
 * @create: 2021-06-10 20:31
 */
@TableName("SING")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("sing")
public class Sing implements Serializable {

    private static final Long SerialVersionUID = 5L;

    @TableField("SING_ID")
    @JSONField(name = "SING_ID")
    @ApiModelProperty("歌曲ID")
    private Integer singId;

    @TableField("SING_NM")
    @JSONField(name = "SING_NM")
    @ApiModelProperty("歌曲名称")
    private String singNm;

    @TableField("SINGER_ID")
    @JSONField(name = "SINGER_ID")
    @ApiModelProperty("歌手ID")
    private Integer singerId;

    @TableField("ISSUE_DATE")
    @JSONField(name = "ISSUE_DATE")
    @ApiModelProperty("发行年月")
    private String issueDate;

    @TableField("SING_DSC")
    @JSONField(name = "SING_DSC")
    @ApiModelProperty("歌曲描述")
    private String singDsc;

    @TableField("SING_PATH")
    @JSONField(name = "SING_PATH")
    @ApiModelProperty("歌曲存放路径")
    private String singPath;

    @TableField("SING_IMG_PATH")
    @JSONField(name = "SING_IMG_PATH")
    @ApiModelProperty("歌曲图片存放路径")
    private String singImgPath;
}