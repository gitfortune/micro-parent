package com.company.project.resource.controller;

import com.company.project.common.domain.RestResponse;
import com.company.project.resource.enmu.ResultEnmu;
import com.company.project.resource.dto.ProcessFileDTO;
import com.company.project.resource.exception.ConvertException;
import com.company.project.resource.service.ConvertService;
import com.company.project.resource.utils.CheckFileTypeUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "音视频转码服务，图片处理", tags = "ConvertApi", description="音视频转码服务，图片处理")
@Slf4j
@RequestMapping("/convert")
public class ConvertController {

    @Autowired
    private ConvertService convertService;

    @RequestMapping("/convert_file")
    public RestResponse convertFile(ProcessFileDTO processFileDTO){

        String filePath = processFileDTO.getFilePath();

        int fileType = CheckFileTypeUtil.checkType(CheckFileTypeUtil.getFileHeader(filePath));

        //如果是音、视频，图片，进入处理程序
        if(fileType != CheckFileTypeUtil.UNKNOWN && fileType != CheckFileTypeUtil.EXPECT){

            convertService.process(fileType,processFileDTO);

        }else if(fileType == CheckFileTypeUtil.EXPECT){
            //MP3，MP4格式，不需要转码，如果大于50M存入指定路径
            log.info("上传到xxx路径下");

        }else {
            //无法处理的未知类型
            throw new ConvertException(ResultEnmu.UNIDENTIFIED);
        }

        return RestResponse.success();
    }

}




