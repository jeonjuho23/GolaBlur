package com.golablur.server.file.overall.service;

import com.golablur.server.file.loader.divider.LoaderDivider;
import com.golablur.server.file.overall.domain.FileID_FileEntityDTO;
import com.golablur.server.file.overall.mapper.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReplaceService {

    @Autowired
    private FileMapper mapper;
    @Autowired
    private LoaderDivider loaderDivider;

    public String replaceResult(FileID_FileEntityDTO fileIDFileEntityDTO) {
        // 이미 편집이 되었는지 확인
        if(mapper.getFileDataByFile_ID(fileIDFileEntityDTO.getFile_ID()) != null){
            // 이미 삭제를 진행한 후 편집이 되었을 경우
            log.info("after delete - update processed file data");
            mapper.replaceFileEntity(fileIDFileEntityDTO);
        }
        else {
            // 첫 편집
            log.info("save custom image - insert processed file data");
            loaderDivider.saveCustomImage(fileIDFileEntityDTO.getFileEntity());
        }
        return "200";
    }

}
