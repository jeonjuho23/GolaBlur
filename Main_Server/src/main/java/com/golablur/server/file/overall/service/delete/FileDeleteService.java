package com.golablur.server.file.overall.service.delete;

import com.golablur.server.file.overall.domain.FileEntity;
import com.golablur.server.file.overall.mapper.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FileDeleteService {

    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private ObjectDeleteService objectDeleteService;

    public int deleteFilesByUserID(String user_id) {
        List fileList = fileMapper.getFileDataByUser_ID(user_id);
        for(Object obj : fileList) {
            FileEntity file = (FileEntity) obj;
            objectDeleteService.deleteObjectsByFileID(file.getFile_ID());
        }
        return fileMapper.deleteFileByUser_ID(user_id);
    }

    public String deleteAFileByFileID(String file_id){
        int s = objectDeleteService.deleteObjectsByFileID(file_id);
        s = fileMapper.deleteFileByFile_ID(file_id);
        if(s == 0) {
            log.error("File delete failed");
            return "500";
        }
        return "200";
    }

    public String deleteFileGroupByFileID(String group_id){
        List fileList = fileMapper.getFileDataByGroup_ID(group_id);
        int s = 0;
        for(Object obj : fileList) {
            FileEntity file = (FileEntity) obj;
            s = objectDeleteService.deleteObjectsByFileID(file.getFile_ID());
        }
        s = fileMapper.deleteFileGroup(group_id);
        if(s == 0) {
            log.error("File delete failed");
            return "500";
        }
        return "200";
    }

}
