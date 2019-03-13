

package com.hnradio.project.resource.controller;

import com.hnradio.project.common.domain.RestResponse;
import com.hnradio.project.resource.dto.UserInfoDTO;
import com.hnradio.project.resource.dto.UserInfoQuery;
import com.hnradio.project.resource.service.UserInfoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/resource")
@Api(value = "用户模块demo", tags = "UserInfoApi", description="用户操作demo")
public class UserInfoController {

	private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);

	@Autowired
	private UserInfoService userInfoService;
	
	@ApiOperation("创建用户")
	@ApiImplicitParam(name = "userInfo", value = "用户", required = true, dataType = "UserInfoDTO", paramType="body")
	@PostMapping(value = "/user")
	public RestResponse<String> createUserInfo(@RequestBody UserInfoDTO userInfo){
		userInfoService.createUserInfo(userInfo);
		return RestResponse.success();
	}
	
	@ApiOperation("删除应用")
	@ApiImplicitParam(name = "id", value = "用户标识", required = true, dataType = "Long", paramType="path")
	@DeleteMapping(value = "/user/{id}")
	public RestResponse<String> removeUserInfoById(@PathVariable("id") Long id){
		userInfoService.removeUserInfoById(id);
		return RestResponse.success();
	}

	@ApiOperation("分页条件检索应用")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "queryParams", value = "应用查询参数", required = false, dataType = "UserInfoQuery", paramType="body"),
		@ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType="query"),
		@ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType="query")
	})
	@PostMapping(value = "/user/page")
	public RestResponse<Page<UserInfoDTO>> pageApplicationByConditions(@RequestBody UserInfoQuery queryParams, @RequestParam Integer pageNo, @RequestParam Integer pageSize){
		Page<UserInfoDTO> page = userInfoService.pageUserInfoByConditions(queryParams,pageNo,pageSize);
		return RestResponse.success(page);
	}
	

	

}
