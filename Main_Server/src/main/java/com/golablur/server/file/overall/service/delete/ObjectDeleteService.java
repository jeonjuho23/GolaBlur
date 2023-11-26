package com.golablur.server.file.overall.service.delete;

import com.golablur.server.file.overall.domain.FileEntity;
import com.golablur.server.file.overall.mapper.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ObjectDeleteService {

    @Autowired
    private ObjectMapper objectMapper;


    public int deleteObjectsByFileID(String file_ID) {
        return objectMapper.deleteObject(file_ID);
    }

}
