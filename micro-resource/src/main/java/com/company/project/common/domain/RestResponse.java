/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.company.project.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@JsonInclude(Include.NON_NULL)
@ApiModel(value = "RestResponse<T>", description = "响应通用参数包装")
@Data
public class RestResponse<T> {

	@ApiModelProperty("响应错误编码,0为正常")
	private int code;
	
	@ApiModelProperty("响应错误信息")
	private String msg;
	
	@ApiModelProperty("响应内容")
	private T result;

	public static <T> RestResponse<T> success() {
		return new RestResponse<>();
	}

	public static <T> RestResponse<T> success(T result) {
		RestResponse<T> response = new RestResponse<>();
		response.setCode(0);
		response.setMsg("成功");
		response.setResult(result);
		return response;
	}

	public static<T> RestResponse<T> error(int code,String msg){
		RestResponse<T> response = new RestResponse<>();
		response.setCode(code);
		response.setMsg(msg);
		return response;
	}

	public RestResponse() {
		this(0, "");
	}

	public RestResponse(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "RestResponse [code=" + code + ", msg=" + msg + ", result="
				+ result + "]";
	}

}
