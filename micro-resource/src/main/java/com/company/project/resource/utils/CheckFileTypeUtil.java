package com.company.project.resource.utils;

import com.company.project.resource.enmu.ResultEnmu;
import com.company.project.resource.exception.ConvertException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 检查文件类型
 */
@Slf4j
public class CheckFileTypeUtil {

    /**无法处理的未知类型*/
    public static final int UNKNOWN = 0;
    /**预期需要的格式：MP4，MP3*/
    public static final int EXPECT = 1;
    /**视频*/
    public static final int VIDEO = 2;
    /**音频*/
    public static final int AUDIO = 3;
    /**图片*/
    public static final int PICTURE = 4;
    /**使用checkType（）方法无法区分avi和wav，先标记为容易混淆的，另作处理 */
    public static final int CONFUSING = 5;

    /**
     * 根据文件路径获取文件头信息
     *
     * @param filePath 文件路径
     * @return 文件头信息
     */
    public static String getFileHeader(String filePath) {
        File file = new File(filePath);
        FileInputStream is = null;
        String value = null;

        if (!file.exists()) {
            log.error("音视频转码：找不到该文件：{}",filePath);
            throw new ConvertException(ResultEnmu.FILE_NOT_FOUND);
        }

        try {
            is = new FileInputStream(filePath);

            byte[] b = new byte[4];
            /*
             * int read() 从此输入流中读取一个数据字节。 int read(byte[] b) 从此输入流中将最多 b.length
             * 个字节的数据读入一个 byte 数组中。 int read(byte[] b, int off, int len)
             * 从此输入流中将最多 len 个字节的数据读入一个 byte 数组中。
             */
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
            log.error("获取文件头信息失败：{}", e);
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("关闭FileInputStream失败：{}", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     * 将要读取文件头信息的文件的byte数组转换成string类型表示
     *
     * @param src 要读取文件头信息的文件的byte数组
     * @return 文件头信息
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        log.info("检测到文件头：" + builder.toString());
        return builder.toString();
    }

    public static int checkType(String str) {
        switch (str) {
            case "FFD8FF":          //jpg
            case "FFD8FFE0":      //jpeg
            case "89504E47":    //png
            case "47494638":    //gif
            case "49492A00":    //tif
            case "424D":        //bmp
                return PICTURE;
            case "57415645":    //wav
            case "4D414320":    //ape
            case "664C6143":    //flac
                return AUDIO;
            case "52494646":    //wav,avi
                return CONFUSING;
            case "FFF15080":    //aac
                return AUDIO;
            case "4D546864":    //mid
                return AUDIO;
            case "3026B2751":
                return AUDIO;
            case "49443303":    //mp3
                return EXPECT;
            case "2E524D46":    //rm，rmvb
                return VIDEO;
            case "000001BA":    //mpeg
            case "000001B3":    //mpeg
                return VIDEO;
            case "6D6F6F76":    //mov
                return VIDEO;
            case "3026B275":    //wma,wmv(asf?) 30 26 B2 75 8E 66 CF 11 A6 D9 00 AA 00 62 CE 6C
                return VIDEO;
            case "00000018":    //mp4
                return EXPECT;
            case "1A45DFA3":    //mkv
                return VIDEO;
            case "464C5601":    //flv
                return VIDEO;
            default:
                return UNKNOWN;
        }
    }

}
