package com.golablur.server.file.ai.service;

import com.golablur.server.file.overall.domain.*;
import com.golablur.server.file.overall.mapper.FileMapper;
import com.golablur.server.file.overall.mapper.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ObjectService {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    FileMapper fileMapper;



    public String storeObjects(List<ObjectEntity> objects) {
        log.info("storeObjects : "+ objects);
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        for(Object obj : objects) {
            ObjectEntity object= mapper.convertValue(obj, ObjectEntity.class);
            if(objectMapper.storeObject(object) == 0){
                log.error("AIService.storeObject failed");
                return null;
            }
        }
        log.info("AIService.storeObject successful");
        return "200";
    }

    public FileObjectDTO getFileObjectDTO(ProcessingFileObjectDTO fileObject) {
        List<ObjectEntity> objectList = new ArrayList<>();
        log.info("getFileObjectDTO fileObject : " + fileObject );
        for (String id : fileObject.getObject_IDList()) {
            log.info(id);
            ObjectEntity object = objectMapper.getObjectByObjectID(id);
            if(object == null){
                log.error("ObjectService getAIFunctionDTO failed");
            }
            objectList.add(object);
        }
        log.info("objectList");
        log.info(objectList.toString());
        return FileObjectDTO.builder()
                .file(fileMapper.getFileDataByFile_ID(fileObject.getFile_ID()))
                .objectList(objectList)
                .build();
    }

    // FileObjectDTO => FileEntity, List<ObjectEntity> -> Detection/Processed Object List
    public FileObjectDTO returnFileObjectByFile(FileEntity file) {
        return FileObjectDTO.builder()
                .file(file)
                .objectList(objectMapper.getDetectionObjectListByFile(file))
                .build();
    }

    public DeepFakeFileEntityDTO getDeepFakeFileEntity(DeepFakeDTO deepFakeDTO) {
        return DeepFakeFileEntityDTO.builder()
                .source_file(fileMapper.getFileDataByFile_ID(deepFakeDTO.getSource_file_ID()))
                .target_file(objectMapper.getDeepFakeFileBySourceFile_ID(deepFakeDTO.getSource_file_ID()))
                .build();
    }


    public int storeObject(ObjectEntity objectEntity) {
        return objectMapper.storeObject(objectEntity);
    }

    public int getPersonCntByFileID(String fileId) {
        return objectMapper.getPersonObjectByFile(fileId).size();
    }


    public List<ObjectEntity> getObjectsByName(FileEntity fileEntity, List<String> objectNameList) {
        List<ObjectEntity> objects = new ArrayList<>();
        for(String objectName : objectNameList){
            log.info("objectName: " + objectName);
            List<ObjectEntity> objectList = objectMapper.getObjectByObjectName(fileEntity.getFile_ID(), objectName);
            log.info("objectList size: " + objectList.size());
            objects.addAll(objectList);
        }
        log.info("objects: " + objects.size());
        return objects;
    }

    public FileObjectDTO getFileObjectDTOByObjectName(ProcessingFileObjectDTO fileObject) {
        log.info("getFileObjectDTOByObjectName");
        List<ObjectEntity> objectEntityList = new ArrayList<>();
        for(String objectName : fileObject.getObject_IDList()){
            log.info("objectName: " + objectName);
            List<ObjectEntity> object = objectMapper.getObjectByObjectName(fileObject.getFile_ID(), objectName);
            log.info("object: " + object.toString());
            objectEntityList.add(object.get(0));
        }
        log.info("objectEntityList : " + objectEntityList.toString());
        return FileObjectDTO.builder()
                .file(fileMapper.getFileDataByFile_ID(fileObject.getFile_ID()))
                .objectList(objectEntityList)
                .build();
    }

}
