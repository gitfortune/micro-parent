package com.company.project.resource.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * 文件处理工具类
 */
@Slf4j
public class FileUtil {

    /**
     * 将文件移动到指定目录
     * @param src   源文件路径
     * @param desc 目标文件路径
     */
    public static void move(String src,String desc){
        long startTime2 = System.currentTimeMillis(); //获取开始时间
        Path path = Paths.get(src);
        Path outPath = Paths.get(desc);
        try {
            Files.move(path, outPath, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime2 = System.currentTimeMillis(); //获取结束时间
        System.out.println("Files程序运行时间：" + (endTime2 - startTime2) + "ms"); //输出程序运行时间
    }


    //传输慢
    public static void moveSlow(String src,String desc){
        long startTime = System.currentTimeMillis(); //获取开始时间
        File inputFile = new File(src);
        File outputFile = new File(desc);

        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(inputFile).getChannel();
            outputChannel = new FileOutputStream(outputFile).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis(); //获取结束时间
        System.out.println("channel程序运行时间：" + (endTime - startTime) + "ms"); //输出程序运行时间



    }
}