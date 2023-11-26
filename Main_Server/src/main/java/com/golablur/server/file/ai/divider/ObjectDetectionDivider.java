package com.golablur.server.file.ai.divider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.golablur.server.file.ai.service.ObjectService;
import com.golablur.server.file.ai.service.SendToAPIService;
import com.golablur.server.file.overall.domain.FileEntity;
import com.golablur.server.file.overall.domain.FileObjectDTO;
import com.golablur.server.file.overall.domain.ObjectEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ObjectDetectionDivider {

    // service
    @Autowired
    private SendToAPIService send;
    @Autowired
    private ObjectService objectService;


    // 객체 탐지 후 반환

    // 하나의 파일의 객체 탐지 후 반환
    public FileObjectDTO getObjects(FileEntity fileEntity) {
        // 객체 탐지
        FileObjectDTO fileObjectDTO =
                FileObjectDTO.builder()
                        .file(fileEntity)
                        .objectList(
                                send.detectObjects(fileEntity)
                        ).build();
        // DB에 저장
        log.info("getObjects: "+ fileObjectDTO);
        objectService.storeObjects(fileObjectDTO.getObjectList());
        return fileObjectDTO;
    }

    // 여러 파일의 객체 탐지 후 각 파일 리스트로 반환
    public List<FileObjectDTO> getObjectsList(List<FileEntity> fileList) {
        // 객체 탐지 및 DB 저장 반복
        List<FileObjectDTO> fileObjectList = new ArrayList<>();
        for(FileEntity fileEntity : fileList){
            fileObjectList.add(getObjects(fileEntity));
            }
        return fileObjectList;
    }

}
