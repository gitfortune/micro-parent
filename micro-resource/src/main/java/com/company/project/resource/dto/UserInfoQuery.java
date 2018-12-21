/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.company.project.resource.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserInfoQuery", description = "用户信息查询参数")
public class UserInfoQuery {

    @ApiModelProperty("用户姓名")
    private  String name;

    @ApiModelProperty("工号")
    private  String jobNumber;

    @ApiModelProperty("手机号")
    private  String phone;

    @ApiModelProperty("状态")
    private int state;


}
