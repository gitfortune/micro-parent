package com.hnradio.project.resource.respository;

import com.hnradio.project.resource.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserInfoRepository extends JpaSpecificationExecutor<UserInfo>,JpaRepository<UserInfo,Long> {


    @Query(value = "update user_info u set u.state = 0 where u.id=?1 ", nativeQuery = true)
    @Modifying
    void disableUserInfo(Long id);


    @Query(value = "update UserInfo u set u.state = 1 where u.id=:id ")
    @Modifying
    void enableUserInfo(@Param("id") Long id);


    /**
     *  方法命名自动匹配
     * @param name
     * @return
     */
    List<UserInfo> findByName(String name);


    List<UserInfo> findAllByIdIn(List<Long> ids);





}
