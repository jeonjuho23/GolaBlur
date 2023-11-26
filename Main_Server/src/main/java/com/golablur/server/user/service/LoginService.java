package com.golablur.server.user.service;

import com.golablur.server.user.domain.DuringWorkDTO;
import com.golablur.server.user.domain.LoginDTO;
import com.golablur.server.user.domain.UserEntity;
import com.golablur.server.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginService {

    @Autowired
    private UserMapper userMapper;

    // 일반 유저 로그인
    public UserEntity normalLogin(LoginDTO loginDTO) {
        UserEntity userEntity = userMapper.login(loginDTO.getUser_ID(), loginDTO.getUser_PW());
        if(userEntity == null) {
            log.error("login: 로그인에 실패했습니다.");
            return null;
        }
        log.info("ID: " + userEntity.getUser_ID()+ "   PW: " + userEntity.getUser_PW() + "    Name: " + userEntity.getUser_Name());
        return userEntity;

    }

    // 작업 도중 로그인
    public String duringWork(DuringWorkDTO duringWorkDTO) {
        log.info("userID:" + duringWorkDTO.getUser_ID() + " Token: " + duringWorkDTO.getSessionToken());
        if(userMapper.updateFileData(duringWorkDTO.getUser_ID(), duringWorkDTO.getSessionToken())==0) {
            log.error("social duringWork: 파일 데이터의 업데이트를 실패했습니다.");
            return "500";
        }
        userMapper.updateObjectData(duringWorkDTO.getUser_ID(), duringWorkDTO.getSessionToken());
        return "200";

    }

    // 작업 도중 로그인 ( 일반 유저 )
    public UserEntity normalLoginDuringWork(DuringWorkDTO duringWorkDTO) {
        log.info("userID:" + duringWorkDTO.getUser_ID() + " Token: " + duringWorkDTO.getSessionToken());
        UserEntity userEntity = userMapper.login(duringWorkDTO.getUser_ID(), duringWorkDTO.getUser_PW());
        if(userEntity == null) {
            log.error("login: 로그인에 실패했습니다.");
            return null;
        }
        if(userMapper.updateFileData(duringWorkDTO.getUser_ID(), duringWorkDTO.getSessionToken()) == 0) {
            log.error("normal duringWork: 파일 데이터의 업데이트를 실패했습니다.");
            return null;
        }
        userMapper.updateObjectData(duringWorkDTO.getUser_ID(), duringWorkDTO.getSessionToken());
        return userEntity;
    }
}
