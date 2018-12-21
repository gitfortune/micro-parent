/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.company.project.resource.service;

import com.company.project.resource.ResourceServer;
import com.company.project.resource.dto.UserInfoDTO;
import com.company.project.resource.dto.UserInfoQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@ActiveProfiles("local")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ResourceServer.class})
public class UserInfoServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceTest.class);

    @Autowired
    private UserInfoService userInfoService;

    @Test
    public void createUserInfo() {
        UserInfoDTO userInfo = new UserInfoDTO();
        userInfo.setName("李四");
        userInfo.setPhone("18611106984");
        userInfo.setJobNumber("2");
        userInfo.setState(1);
        userInfo.setCreateTime(new Date());
        userInfo.setUpdateTime(new Timestamp(new Date().getTime()));
        userInfo = userInfoService.createUserInfo(userInfo);
        logger.info("save userinfo id : {}" , userInfo.getId());
    }

    @Test
    public void removeUserInfoById() {
        userInfoService.removeUserInfoById(3L);
    }

    @Test
    public void modifyUserInfo() {
        UserInfoDTO userInfo = userInfoService.getUserInfoById(1L);
        userInfo.setState(0);
        userInfoService.modifyUserInfo(userInfo);
    }

    @Test
    public void disableUserInfo() {
        userInfoService.disableUserInfo(1L);
    }

    @Test
    public void enableUserInfo() {
        userInfoService.enableUserInfo(1L);
    }

    @Test
    public void getUserInfoById() {
        UserInfoDTO userInfo = userInfoService.getUserInfoById(1L);
        logger.info("userinfo content : {}" , userInfo);
    }

    @Test
    public void queryUserInfoByName() {
        List<UserInfoDTO> userInfoDTOList = userInfoService.queryUserInfoByName("李四");
        for(UserInfoDTO u : userInfoDTOList){
            logger.info("userinfo content : {}" , u);
        }

    }

    @Test
    public void queryUserInfoByIdIn() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        ids.add(3L);
        List<UserInfoDTO> userInfoDTOList = userInfoService.queryUserInfoByIdIn(ids);
        for(UserInfoDTO u : userInfoDTOList){
            logger.info("userinfo content : {}" , u);
        }
    }

    @Test
    public void pageUserInfoByConditions() {
        UserInfoQuery query = new UserInfoQuery();
        query.setPhone("18611106984");
        query.setName("张");
        Page<UserInfoDTO> userInfoDTOPage = userInfoService.pageUserInfoByConditions(query,1,2);
        logger.info("page userinfo content : {}" , userInfoDTOPage);
        for(UserInfoDTO u : userInfoDTOPage.getContent()){
            logger.info("userinfo content : {}" , u);
        }
    }

    @Test
    public void statisticsPhoneGroup(){
        List<Map> statistics = userInfoService.statisticsPhoneGroup();
        for(Map u : statistics){
            logger.info("userinfo content : {}" , u);
        }
    }
}