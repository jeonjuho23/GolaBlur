package com.golablur.server.file.ai.divider;

import com.golablur.server.file.ai.service.ObjectService;
import com.golablur.server.file.ai.service.SendToAPIService;
import com.golablur.server.file.loader.service.storeFileData.StoreFileDataService;
import com.golablur.server.file.overall.domain.*;
import com.golablur.server.file.overall.mapper.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MosaicDivider {

    @Autowired
    private ObjectService objectService;
    @Autowired
    private StoreFileDataService storeFileDataService;
    @Autowired
    private SendToAPIService send;
    @Autowired
    private FileMapper fileMapper;

    //

    public FileEntity mosaicOneImage(ProcessingFileObjectDTO fileObject) {
        // DB에 접근하여 AIFUnctionDTO 를 채운다.
        FileObjectDTO fileObjectDTO = objectService.getFileObjectDTO(fileObject);
        // AIFUnctionDTO 를 AI API 로 전송하고 처리된 파일을 반환 받는다.
        FileEntity processedFile = send.processMosaicOneImage(fileObjectDTO);
        if(processedFile == null){
            log.error("mosaicOneImage process failed");
            return null;
        }
        // 처리된 파일을 DB에 저장한다.
        if(storeFileDataService.storeFile(processedFile).equals("500")){
            log.error("mosaicOneImage storeFile failed");
            return null;
        }
        storeFileDataService.updateProcessedFileData(processedFile.getOriginal_File_ID());
        log.info("MosaicOneImage successful");
        return processedFile;
    }


    public FileEntity mosaicOneVideo(ProcessingFileObjectDTO fileObject) {
        // DB에 접근하여 AIFUnctionDTO 를 채운다.
        FileObjectDTO fileObjectDTO = objectService.getFileObjectDTOByObjectName(fileObject);
        // AIFUnctionDTO 를 AI API 로 전송하고 처리된 파일을 반환 받는다.
        FileEntity processedFile = send.processMosaicOneVideo(fileObjectDTO);
        if(processedFile == null){
            log.error("mosaicOneVideo process failed");
            return null;
        }
        // 처리된 파일을 DB에 저장한다.
        if(storeFileDataService.storeFile(processedFile).equals("500")){
            log.error("mosaicOneVideo storeFile failed");
            return null;
        }
        storeFileDataService.updateProcessedFileData(processedFile.getOriginal_File_ID());
        log.info("MosaicOneVideo successful");
        return processedFile;
    }

    public List<FileEntity> mosaicALotImages(GroupID_ObjectNameListDTO groupIDObjectIDListDTO) {

        int cnt = 0;
        // 이미지별 객체에 대한 리스트
        List<FileObjectDTO> processList = new ArrayList<>();

        // 그룹 이미지 가져오기
        List<FileEntity> groupFileEntity = fileMapper.getFileDataByGroup_ID(groupIDObjectIDListDTO.getGroupID());

        // 그룹 내 파일별로 객체 이름이 같은 객체들의 entity 가져와서 구성하기
        for(FileEntity fileEntity : groupFileEntity){
            List<ObjectEntity> objectEntityList =
                    objectService.getObjectsByName(fileEntity, groupIDObjectIDListDTO.getObjectNameList());

            processList.add(FileObjectDTO.builder().file(fileEntity).objectList(objectEntityList).build());
        }
        log.info("processList: " + processList.toString());
        // 하나의 이미지 처리를 반복
        List<FileEntity> processedList = send.mosaicGroupImage(processList);

        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        for (Object object : processedList) {
            FileEntity processedFile = mapper.convertValue(object, FileEntity.class);
            // 처리된 파일을 DB에 저장한다.
            if (storeFileDataService.storeFile(processedFile).equals("500")) {
                log.error("mosaic image storeFile failed");
                return null;
            }
            storeFileDataService.updateProcessedFileData(processedFile.getOriginal_File_ID());
        }

        log.info("MosaicALotImages successful");
        log.info(processedList.toString());
        return processedList;
    }

}
