/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hnradio.project.resource.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
@ApiModel(value = "UserInfoDTO", description = "用户信息")
public class UserInfoDTO {

    @ApiModelProperty("用户id")
    private  Long id;

    @ApiModelProperty("用户姓名")
    private  String name;

    @ApiModelProperty("工号")
    private  String jobNumber;

    @ApiModelProperty("手机号")
    private  String phone;

    @ApiModelProperty("状态")
    private int state;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Timestamp updateTime;

}
