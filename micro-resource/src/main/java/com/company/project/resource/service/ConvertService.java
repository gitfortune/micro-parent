package com.company.project.resource.service;

import com.company.project.resource.config.ConvertProperties;
import com.company.project.resource.dto.ProcessFileDTO;
import com.company.project.resource.enmu.ResultEnmu;
import com.company.project.resource.entity.MediaInfo;
import com.company.project.resource.exception.ConvertException;
import com.company.project.resource.utils.CheckFileTypeUtil;
import com.company.project.resource.utils.FileUtil;
import com.company.project.resource.utils.XuggleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 音视频转码服务，图片处理
 */
@Service
@Slf4j
public class ConvertService {

    @Autowired
    ConvertProperties properties;

    public String process(int fileType,ProcessFileDTO processFileDTO) {

        String filePath = processFileDTO.getFilePath();
        String tempPath = null;
        String descPath = null;
        //先判断文件格式
        if(fileType == CheckFileTypeUtil.VIDEO){

            tempPath = this.videoTranscoding(processFileDTO);
            //转码完成后，获取文件信息
            MediaInfo mediaInfo = XuggleUtil.getMediaInfo(tempPath);
            log.info(mediaInfo.getFileSize()+"M");

            String fileName = tempPath.substring(tempPath.lastIndexOf("/")+1);
            //如果转码后文件小于指定大小，存到原文件同级目录下
            if(mediaInfo.getFileSize() <= properties.getSeparateSize()){
                descPath = filePath.substring(0, filePath.lastIndexOf("/")+1) + fileName;
            }else{
                //如果大于指定大小，上传到WOWZA。
                File file = new File(properties.getContent());
                if(!file.exists()){
                    file.mkdir();
                }
                descPath = properties.getContent()+fileName;
            }
        }else if (fileType == CheckFileTypeUtil.AUDIO){
            tempPath = this.aideoTranscoding(processFileDTO);
            String fileName = tempPath.substring(tempPath.lastIndexOf("/")+1);
            descPath = filePath.substring(0, filePath.lastIndexOf("/")+1) + fileName;
        }else if(fileType == CheckFileTypeUtil.PICTURE){
            //图片处理

        }
        log.info("最终路径：{}",descPath);
        FileUtil.move(tempPath,descPath);
        return descPath;
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


    /**
     * 视频转码
     * @param processFileDTO
     * @return
     */
    public String videoTranscoding(ProcessFileDTO processFileDTO){
        File dire = new File(properties.getVideoTemp());
        if(!dire.exists()){
            dire.mkdir();
        }
        String inputPath = processFileDTO.getFilePath();
        File file = new File(inputPath);
        String name = file.getName();
        String subName = name.substring(0,name.lastIndexOf("."));  //截取文件名字
        String outputPath = properties.getVideoTemp()+subName+".mp4";
        List<String> commend = new ArrayList<>();
        commend.add(properties.getFfmpeg());
        commend.add("-i");
        commend.add(inputPath);
        commend.add("-y");  //如果有重名，覆盖掉
        commend.add("-loglevel");
        commend.add("quiet");
//        commend.add("-threads"); // 开启两个线程，实测速度变慢，弃用
//        commend.add("2");
//        commend.add("-c:v");
//        commend.add("libx264");
//        commend.add("-s");
//        commend.add("960x540");
        commend.add(outputPath);
        try {

            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process process = builder.start();
            process.waitFor();  //执行完才能继续下一步，输出路径
        } catch (IOException | InterruptedException e) {
            log.error("ffmpeg视频转码出错了：{}，转码失败视频：{}",e.getMessage(),inputPath);
            throw new ConvertException(ResultEnmu.FFMPEG_FAIL);
        }
        log.info("转码完成！！！！！！！！");
        return outputPath;
    }

    /**
     * 音频转码
     * @param processFileDTO
     * @return
     */
    public String aideoTranscoding(ProcessFileDTO processFileDTO) {
        File dire = new File(properties.getAideoTemp());
        if(!dire.exists()){
            dire.mkdir();
        }
        String inputPath = processFileDTO.getFilePath();
        File file = new File(inputPath);
        String name = file.getName();
        String subName = name.substring(0,name.lastIndexOf("."));  //截取文件名字
        String outputPath = properties.getAideoTemp()+subName+".mp3";
        List<String> commend = new ArrayList<>();
        commend.add(properties.getFfmpeg());
        commend.add("-i");
        commend.add(inputPath);
        commend.add("-y");  //如果有重名，覆盖掉
        commend.add("-loglevel");
        commend.add("quiet");
//        commend.add("-c:v");
//        commend.add("libx264");
        commend.add(outputPath);
        try {

            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process process = builder.start();
            process.waitFor();  //执行完才能继续下一步，输出路径
        } catch (IOException | InterruptedException e) {
            log.error("ffmpeg音频转码出错了：{}，转码失败音频：{}",e.getMessage(),inputPath);
            throw new ConvertException(ResultEnmu.FFMPEG_FAIL);
        }
        log.info("音频转码完成！！！！！！！！");
        return outputPath;
    }
}
