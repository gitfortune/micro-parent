package com.company.project.resource.service;

import com.company.project.resource.dto.ProcessFileDTO;
import com.company.project.resource.utils.CheckFileTypeUtil;
import com.company.project.resource.utils.FfmpegUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 音视频转码服务，图片处理
 */
@Service
@Slf4j
public class ConvertService {

    public boolean process(int fileType,ProcessFileDTO processFileDTO) {
        boolean result = false;
        //先判断文件格式
        if(fileType == CheckFileTypeUtil.VIDEO){

            result = FfmpegUtil.videoTranscoding(processFileDTO);
        }else if (fileType == CheckFileTypeUtil.AUDIO){

            //result = FfmpegUtil.aideoTranscoding(processFileDTO);
        }else if(fileType == CheckFileTypeUtil.PICTURE){
            //图片处理

        }

        return result;
    }


    /**
     * 图片处理
     * @param filePath
     * @param waterMarkPath
     * @param processFileDTO
     * @return
     */
    public String picProcess(String filePath, String waterMarkPath, ProcessFileDTO processFileDTO){

        return "";
    }


}
