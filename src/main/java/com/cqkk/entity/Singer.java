package com.cqkk.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: lxmAndkk
 * @description: 歌手实体类
 * @author: luo kk
 * @create: 2021-06-10 20:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("singer")
@ApiModel("歌手表")
public class Singer implements Serializable {

    /*版本号*/
    private static final long SerialVersionUID = 1L;

    @TableId(value = "SINGER_ID", type = IdType.AUTO)
    @JSONField(name = "SINGER_ID")
    @ApiModelProperty("歌手ID")
    private Integer singerId;

    @TableField("SINGER_NM")
    @JSONField(name = "SINGER_NM")
    @ApiModelProperty("歌手名称")
    private String singerNm;

    @TableField("SINGER_SEX")
    @JSONField(name = "ASINGER_SEXGE")
    @ApiModelProperty("歌手性别")
    private String singerSex;

    @TableField("AGE")
    @JSONField(name = "AGE")
    @ApiModelProperty("歌手年龄")
    private Integer age;

    @TableField("SINGER_PATH")
    @JSONField(name = "SINGER_PATH")
    @ApiModelProperty("歌手图片")
    private String singerPath;

}