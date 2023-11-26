package com.golablur.server.user.service;

import com.golablur.server.file.overall.service.delete.FileDeleteService;
import com.golablur.server.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDeleteService {

    @Autowired
    private FileDeleteService fileDeleteService;
    @Autowired
    private UserMapper userMapper;


    public int userDelete(String user_id) {
        fileDeleteService.deleteFilesByUserID(user_id);
        return userMapper.deleteUser(user_id);
    }

}
