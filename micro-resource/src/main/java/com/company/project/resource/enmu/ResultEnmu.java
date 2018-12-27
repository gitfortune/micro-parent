package com.company.project.resource.enmu;

import lombok.Getter;

@Getter
public enum ResultEnmu {

    LOGIN_FAIL(1,"登陆失败，用戶名或密碼不正确"),

    UNIDENTIFIED(2,"无法识别文件格式"),

    FFMPEG_FAIL(3,"ffmpeg执行转码出现异常"),

    THUMBNAILATOR_FAIL(4,"Thumbnailator出现异常"),

    FILE_NOT_FOUND(5,"找不到该文件"),

    XUGGLE_FAIL(6,"Xuggle打开媒体文件失败"),

    FILE_MOVE_FAIL(7,"移动文件到指定目录失败"),

    THUMBNAILS_FAIL(8,"图片处理失败"),

    MQ_SEND_MSG_FAIL(9,"MQ发送消息失败"),

    ;




    private int code;

    private String msg;

    ResultEnmu(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
