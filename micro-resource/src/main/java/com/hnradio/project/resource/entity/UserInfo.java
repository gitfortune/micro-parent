package com.hnradio.project.resource.entity;


import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Component
@Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id; //ID


    private  String name; //姓名


    private  String jobNumber; //工号


    private  String phone; //手机号

    private int state; //类型


    private Date createTime; //创建时间


    private Timestamp updateTime;



}
