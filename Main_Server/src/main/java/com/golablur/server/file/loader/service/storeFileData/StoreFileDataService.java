package com.golablur.server.file.loader.service.storeFileData;

import com.golablur.server.file.overall.mapper.FileMapper;
import com.golablur.server.file.overall.domain.FileEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StoreFileDataService {

    @Autowired
    FileMapper mapper;


    public String storeFile(FileEntity file) {
        log.info("file"+file);
        if(mapper.uploadOriginalFile(file) == 0) {
            log.error("파일 데이터의 DB 저장을 실패했습니다.");
            return "500";
        }
        log.info("파일 업로드 성공");
        return "200";
    }

    public String updateProcessedFileData(String original_id) {
        log.info("original_id : "+original_id);
        if(mapper.updateProcessedFileData(original_id) == 0){
            log.error("Process file data update failed");
            return "500";
        }
        return "200";
    }
}
