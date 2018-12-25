package com.company.project.resource.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MediaInfo", description = "包含音、视频，图片实体对象的参数")
public class MediaInfo {

    @ApiModelProperty("文件大小，单位：M")
    private float fileSize;

    @ApiModelProperty("视频分辨率参数：高度")
    private int height;

    @ApiModelProperty("视频分辨率参数：宽度")
    private int width;
}
