package com.golablur.server.file.loader.service.getFileData;

import com.golablur.server.file.overall.mapper.FileMapper;
import com.golablur.server.file.overall.domain.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class getFileDataService {

    @Autowired
    FileMapper mapper;

    public FileEntity getOneFileData(String file_id) {
        return mapper.getFileDataByFile_ID(file_id);
    }

    public List<FileEntity> getFileListData(String group_id) {
        return mapper.getFileDataByGroup_ID(group_id);
    }

}
