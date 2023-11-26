package com.golablur.server.file.ai.divider;

import com.golablur.server.file.ai.service.ObjectService;
import com.golablur.server.file.ai.service.SendToAPIService;
import com.golablur.server.file.loader.service.storeFileData.StoreFileDataService;
import com.golablur.server.file.overall.domain.*;
import com.golablur.server.file.overall.mapper.FileMapper;
import com.golablur.server.file.overall.mapper.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DeepFakeDivider {


    @Autowired
    private ObjectService objectService;
    @Autowired
    private StoreFileDataService storeFileDataService;
    @Autowired
    private SendToAPIService send;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private ObjectMapper objectMapper;

    // 사람 딥페이크 후 반환

//    public FileEntity deepFakeOneImage(ProcessingFileObjectDTO fileObject) {
//        // DB에 접근하여 AIFUnctionDTO 를 채운다.
//        FileObjectDTO fileObjectDTO = objectService.getFileObjectDTO(fileObject);
//        if (fileObjectDTO == null) {
//            log.error("deepFakeOneImage getAIFunctionDTO failed");
//            return null;
//        }
//        // AIFUnctionDTO 를 AI API 로 전송하고 처리된 파일을 반환 받는다.
//        FileEntity processedFile = send.processFakeOneImage(fileObjectDTO);
//        if(processedFile == null){
//            log.error("deepFakeOneImage process failed");
//            return null;
//        }
//        // 처리된 파일을 DB에 저장한다.
//        if(storeFileDataService.storeFile(processedFile).equals("500")){
//            log.error("deepFakeOneImage storeFile failed");
//            return null;
//        }
//        storeFileDataService.updateProcessedFileData(processedFile.getOriginal_File_ID());
//        log.info("deepFakeOneImage successful");
//        return processedFile;
//    }
//
//
//    public FileEntity deepFakeOneVideo(ProcessingFileObjectDTO fileObject) {
//        // DB에 접근하여 AIFUnctionDTO 를 채운다.
//        FileObjectDTO fileObjectDTO = objectService.getFileObjectDTO(fileObject);
//        if (fileObjectDTO == null) {
//            log.error("deepFakeOneVideo getAIFunctionDTO failed");
//            return null;
//        }
//        // AIFUnctionDTO 를 AI API 로 전송하고 처리된 파일을 반환 받는다.
//        FileEntity processedFile = send.processFakeOneVideo(fileObjectDTO);
//        if (processedFile == null) {
//            log.error("deepFakeOneVideo process failed");
//            return null;
//        }
//        // 처리된 파일을 DB에 저장한다.
//        if(storeFileDataService.storeFile(processedFile).equals("500")){
//            log.error("deepFakeOneVideo storeFile failed");
//            return null;
//        }
//        storeFileDataService.updateProcessedFileData(processedFile.getOriginal_File_ID());
//        log.info("deepFakeOneVideo successful");
//        return processedFile;
//    }
//
//    public List<FileEntity> deepFakeALotImages(List<ProcessingFileObjectDTO> fileObjectList) {
//        // 하나의 이미지 처리를 반복
//        List<FileEntity> processedList = new ArrayList<>();
//        int cnt = 0;
//        for(ProcessingFileObjectDTO fileObject : fileObjectList){
//            FileEntity fileEntity = deepFakeOneImage(fileObject);
//            if (fileEntity == null) {
//                log.error("deepFakeALotImages failed : index "+cnt);
//            }
//            processedList.add(fileEntity);
//            cnt++;
//        }
//        log.info("deepFakeALotImages successful");
//        return processedList;
//    }


    public FileEntity deepFakeOneImage(DeepFakeDTO deepFakeDTO) {
        // 데이터베이스에서 FileEntity 채우기
        log.info("deepFakeOneImage deepFakeDTO : "+deepFakeDTO.toString());
        DeepFakeFileEntityDTO deepFakeFileEntityDTO =
                objectService.getDeepFakeFileEntity(deepFakeDTO);

        // AI api 에 요청
        FileEntity processedFile = send.processFakeOneImage(deepFakeFileEntityDTO);
        log.info("deepFakeOneImage processedFile : "+processedFile.toString());

        // DB에 처리된 파일 정보 저장
        storeFileDataService.storeFile(processedFile);
        storeFileDataService.updateProcessedFileData(processedFile.getOriginal_File_ID());

        return processedFile;
    }

    public FileEntity deepFakeOneVideo(DeepFakeDTO deepFakeDTO) {
        // 데이터베이스에서 FileEntity 채우기
        log.info("deepFakeOneVideo deepFakeDTO : "+ deepFakeDTO.toString());
        DeepFakeFileEntityDTO deepFakeFileEntityDTO =
                objectService.getDeepFakeFileEntity(deepFakeDTO);

        // AI api 에 요청
        FileEntity processedFile = send.processFakeOneVideo(deepFakeFileEntityDTO);
        log.info("deepFakeOneVideo processedFile : "+processedFile.toString());

        // DB에 처리된 파일 정보 저장
        storeFileDataService.storeFile(processedFile);
        storeFileDataService.updateProcessedFileData(processedFile.getOriginal_File_ID());

        return processedFile;
    }

    public List<FileEntity> deepFakeALotImages(DeepFakeDTO deepFakeDTO) {
        log.info("deepFakeALotImages");
        // group_ID 로 file_ID 가져오기
        List<FileEntity> fileEntityList = fileMapper.getFileDataByGroup_ID(deepFakeDTO.getSource_file_ID());
        // AI api 에 요청
        List<FileEntity> res = (List<FileEntity>) send.processFakeGroupImage(DeepFakeGroupEntityDTO.builder()
                .sourceFileEntityList(fileEntityList)
                .targetFileEntity(objectMapper.getObjectByObjectID(deepFakeDTO.getTarget_file_ID()))
                .build());
        // 클래스 에러 해결
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        List<FileEntity> processedFileList = new ArrayList<FileEntity>();
        for(Object object: res){
            FileEntity file = mapper.convertValue(object, FileEntity.class);
            processedFileList.add(file);
        }
        // DB에 저장
        for(int i=0;i<fileEntityList.size();i++) {
            storeFileDataService.storeFile(processedFileList.get(i));
            storeFileDataService.updateProcessedFileData(fileEntityList.get(i).getFile_ID());
        }
        log.info("deepFakeALotImages successful");
        return res;
    }

    public String uploadTargetImage(FileID_FileEntityDTO fileIDFileEntityDTO) {
        log.info("Uploading target image");
        log.info("===============");
        log.info(fileIDFileEntityDTO.getFileEntity().getPath());
        // 원본 이미지가 사람이 탐지된 이미지인지 검사
        if(checkPersonInFile(fileIDFileEntityDTO.getFile_ID()) == 0){
            log.error("no person in file");
            return "500";
        }
        log.info("person in file");
        // 타겟 이미지에 사람이 탐지되는 이미지인지 검사
        if(checkPersonInTarget(fileIDFileEntityDTO.getFileEntity()) == 0){
            log.error("no person in target");
            return "505";
        }
        log.info("person in target");
        // 모두 성공 시 타겟 이미지를 객체 테이블에 저장
        // FileEntity 를 ObjectEntity 로 변환 필요
        ObjectEntity objectEntity = convertFileEntityToObjectEntity(fileIDFileEntityDTO);
        if(objectService.storeObject(objectEntity) == 0){
            log.error("store object failed");
            return "555";
        }
        log.info("store deepFake object success");
        return "200";
    }

    public String uploadGroupTargetImage(FileID_FileEntityDTO fileIDFileEntityDTO) {
        log.info("uploadGroupTargetImage");
        // target image 에 사람이 있는지 검사
        log.info("group_ID : "+fileIDFileEntityDTO.getFile_ID());
        if(checkPersonInTarget(fileIDFileEntityDTO.getFileEntity()) == 0){
            log.error("no person in target");
            return "505";
        }
        log.info("person in target");
        // 그룹 Id를 통해 file들 가져오기
        List<FileEntity> fileEntityList =
                fileMapper.getFileDataByGroup_ID(fileIDFileEntityDTO.getFile_ID());
        log.info("group: "+fileEntityList.toString());
        // 파일별로 사람이 있는지 검사 후 딥페이크 할 이미지만 모아두기
        List<FileEntity> processFileList = new ArrayList<>();
        for(FileEntity fileEntity : fileEntityList){
            if(checkPersonInFile(fileEntity.getFile_ID()) == 0){
                log.error("no person in file : " + fileEntity.getFile_ID());
                continue;
            }
            log.info("person in file : " + fileEntity.getFile_ID());
            processFileList.add(fileEntity);
        }
        if(processFileList.size() == 0){
            log.error("no person in source file");
            return "500";
        }
        log.info("person check complete");
        // DB에 저장
        ObjectEntity objectEntity = convertFileEntityToObjectEntity(fileIDFileEntityDTO);
        if(objectService.storeObject(objectEntity) == 0){
            log.error("store object failed");
            return "555";
        }
        log.info("store deepFake object success");
        return "200";
    }

    private ObjectEntity convertFileEntityToObjectEntity(FileID_FileEntityDTO fileIDFileEntityDTO) {
        // TODO FileEntity 를 ObjectEntity 로 변환시켜줌
        return ObjectEntity.builder()
                .object_ID(fileIDFileEntityDTO.getFileEntity().getFile_ID())
                .file_ID(fileIDFileEntityDTO.getFile_ID())
                .user_ID(fileIDFileEntityDTO.getFileEntity().getUser_ID())
                .object_Name("deepFake")
                .file_Extension(fileIDFileEntityDTO.getFileEntity().getFile_Extension())
                .path(fileIDFileEntityDTO.getFileEntity().getPath())
                .build();
    }

    public int checkPersonInFile(String file_ID){
        // TODO 원본 이미지가 사람이 탐지된 이미지인지 검사
        return objectService.getPersonCntByFileID(file_ID);
    }

    public int checkPersonInTarget(FileEntity target){
        // TODO 타겟 이미지에 사람이 탐지되는 이미지인지 검사
        List<ObjectEntity> objectEntityList = send.detectObjects(target);
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        int cnt = 0;
        for(Object obj : objectEntityList){
            ObjectEntity object = mapper.convertValue(obj, ObjectEntity.class);
            if(object.getObject_Name().equals("person")) cnt = cnt+1;
        }
        return cnt;
    }



}
