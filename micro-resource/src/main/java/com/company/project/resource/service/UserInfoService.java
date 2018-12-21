

package com.company.project.resource.service;

import com.company.project.resource.dto.UserInfoDTO;
import com.company.project.resource.dto.UserInfoQuery;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface UserInfoService {

    /**
     * 新增 信息
     * @param userInfo
     * @return
     */
    UserInfoDTO createUserInfo(UserInfoDTO userInfo);

    /**
     * 删除信息
     * @param id
     */
    void removeUserInfoById(Long id);

    /**
     * 修改信息
     * @param userInfo
     */
    void modifyUserInfo(UserInfoDTO userInfo);

    /**
     * 禁用记录
     * @param id
     */
    void disableUserInfo(Long id);

    /**
     * 启用记录
     * @param id
     */
    void enableUserInfo(Long id);

    /**
     * 获取单个信息
     * @param id
     * @return
     */
    UserInfoDTO getUserInfoById(Long id);

    /**
     * 查询信息
     * @param name
     * @return
     */
    List<UserInfoDTO> queryUserInfoByName(String name);

    /**
     * id列表查询信息
     * @param ids
     * @return
     */
    List<UserInfoDTO> queryUserInfoByIdIn(List<Long> ids);

    /**
     * 分页查询信息
     * @param query
     * @param pageNumber
     * @param pageSize
     * @return
     */
    Page<UserInfoDTO> pageUserInfoByConditions(UserInfoQuery query, Integer pageNumber, Integer pageSize);

    /**
     * 轻统计
     */

    List<Map> statisticsPhoneGroup();




}
