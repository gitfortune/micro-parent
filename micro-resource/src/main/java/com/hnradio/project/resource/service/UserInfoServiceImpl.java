

package com.hnradio.project.resource.service;

import com.hnradio.project.common.domain.BusinessException;
import com.hnradio.project.common.domain.ErrorCode;
import com.hnradio.project.resource.dto.UserInfoDTO;
import com.hnradio.project.resource.dto.UserInfoQuery;
import com.hnradio.project.resource.entity.UserInfo;
import com.hnradio.project.resource.respository.UserInfoRepository;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    private UserInfoRepository userInfoRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public UserInfoDTO createUserInfo(UserInfoDTO userInfo) {
       if(StringUtils.isBlank(userInfo.getName())){
            throw new BusinessException(ErrorCode.E_130101);
        }
        UserInfo entity  =  convertUserInfoDTOToEntity(userInfo);
        userInfoRepository.save(entity);
        return convertUserInfoEntityToDTO(entity);
    }

    @Override
    public void removeUserInfoById(Long id) {
        userInfoRepository.deleteById(id);
    }

    @Override
    public void modifyUserInfo(UserInfoDTO userInfo) {
        UserInfo entity  =  convertUserInfoDTOToEntity(userInfo);
        userInfoRepository.save(entity);
    }

    @Override
    public void disableUserInfo(Long id) {
        userInfoRepository.disableUserInfo(id);
    }

    @Override
    public void enableUserInfo(Long id) {
        userInfoRepository.enableUserInfo(id);
    }


    @Override
    public UserInfoDTO getUserInfoById(Long id) {
        return convertUserInfoEntityToDTO(userInfoRepository.getOne(id));
    }

    @Override
    public List<UserInfoDTO> queryUserInfoByName(String name) {
        return convertUserInfoEntityToDTOs(userInfoRepository.findByName(name));
    }

    @Override
    public List<UserInfoDTO> queryUserInfoByIdIn(List<Long> ids) {
        return convertUserInfoEntityToDTOs(userInfoRepository.findAllByIdIn(ids));
    }

    @Override
    public Page<UserInfoDTO> pageUserInfoByConditions(UserInfoQuery userInfoQuery, Integer pageNumber, Integer pageSize) {

        Sort sort = new Sort(Sort.Direction.DESC,"createTime"); //创建时间降序排序
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Specification<UserInfo> spec = new Specification<UserInfo>() {
            @Override
            public Predicate toPredicate(Root<UserInfo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (StringUtils.isNotBlank(userInfoQuery.getName())) {
                    list.add(criteriaBuilder.like(root.get("name").as(String.class), userInfoQuery.getName() + "%"));
                }
                if (StringUtils.isNotBlank(userInfoQuery.getJobNumber())) {
                    list.add(criteriaBuilder.equal(root.get("jobNumber").as(String.class), userInfoQuery.getJobNumber()));
                }
                if (StringUtils.isNotBlank(userInfoQuery.getPhone())) {
                    list.add(criteriaBuilder.equal(root.get("phone").as(String.class), userInfoQuery.getPhone()));
                }
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        };

        Page<UserInfo> entitys = userInfoRepository.findAll(spec, pageable);
        Page<UserInfoDTO> dtos = new PageImpl<>(convertUserInfoEntityToDTOs(entitys.getContent()), pageable, entitys.getTotalElements() );
        return  dtos;
    }

    @Override
    public List<Map> statisticsPhoneGroup() {

        String sql = "select phone, count(1) count from user_info where state = 1  group by phone ";
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> statistics = query.getResultList();
        return statistics;
    }


    ////以下是dto entity 互转方法

    private UserInfoDTO convertUserInfoEntityToDTO(UserInfo entity){
        UserInfoDTO dto = new UserInfoDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }


    private List<UserInfoDTO> convertUserInfoEntityToDTOs(List<UserInfo> entitys){
        List<UserInfoDTO> dtos = new ArrayList<>();
        for(UserInfo e : entitys){
            dtos.add(convertUserInfoEntityToDTO(e));
        }
        return dtos;
    }

    private UserInfo convertUserInfoDTOToEntity(UserInfoDTO dto){
        UserInfo entity = new UserInfo();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    private List<UserInfo> convertUserInfoDTOToEntity(List<UserInfoDTO> dtos){
        List<UserInfo> entitys = new ArrayList<>();
        for(UserInfoDTO d : dtos){
            entitys.add(convertUserInfoDTOToEntity(d));
        }
        return  entitys;
    }



}
