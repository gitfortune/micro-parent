package com.company.project.resource.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ProcessFileDTO", description = "包含音、视频，图片转换时用到的参数")
public class ProcessFileDTO {

    @ApiModelProperty("音、视频，图片参数，文件原路径")
    private String filePath;

    @ApiModelProperty("水印图片参数，水印图片路径")
    private String waterMarkPath;

    @ApiModelProperty("音、视频，图片参数，缩放比例，默认不缩放")
    private float scale;

    @ApiModelProperty("音、视频参数，码率，默认使用原码率")
    private String rate;

    @ApiModelProperty("图片参数，裁剪位置的横坐标")
    private int x;

    @ApiModelProperty("图片参数，裁剪位置的纵坐标")
    private int y;

    @ApiModelProperty("图片参数，裁剪的宽度")
    private int cutWidth;

    @ApiModelProperty("图片参数，裁剪的高度")
    private int cutHeight;

    @ApiModelProperty("水印图片参数，水印位置，1：左上，2：左下，3：右上，4，右下，5：居中")
    private int position;

    @ApiModelProperty("水印图片参数，透明度，默认不透明")
    private float transparency;

}
