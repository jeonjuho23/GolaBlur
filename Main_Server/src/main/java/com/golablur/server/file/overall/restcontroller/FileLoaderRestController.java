package com.golablur.server.file.overall.restcontroller;

import com.golablur.server.file.ai.divider.ObjectDetectionDivider;
import com.golablur.server.file.loader.divider.LoaderDivider;
import com.golablur.server.file.overall.domain.FileEntity;
import com.golablur.server.file.overall.domain.ObjectEntity;
import com.golablur.server.file.overall.service.delete.FileDeleteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/file/loader")
@CrossOrigin("*")
@Slf4j
public class FileLoaderRestController {

    @Autowired
    private LoaderDivider loaderDivider;
    @Autowired
    private ObjectDetectionDivider objectDetectionDivider;
    @Autowired
    private FileDeleteService fileDeleteService;

    // 하나의 파일 업로드 및 객체 탐지 후 반환
    @PostMapping("/upload/one")
    public String uploadOne(FileEntity fileEntity){
        // 업로드
        log.info("uploadOne"+ fileEntity);
        loaderDivider.uploadOne(fileEntity);
        return "200";
    }

    @RequestMapping("/detect/objects")
    public String detectObjects(@RequestParam("file_ID") String file_ID){
        FileEntity fileEntity = loaderDivider.getOneFileData(file_ID);
        // 객체 탐지
        objectDetectionDivider.getObjects(fileEntity);
        return fileEntity.getFile_Extension();
    }

    // 결과물 다운로드를 위한 파일 데이터 반환
    @RequestMapping("/download/one")
    public FileEntity downloadFileOne(@RequestParam("file_ID") String File_ID){
        return loaderDivider.getOneFileData(File_ID);
    }

    // 그룹 결과물 다운로드를 위한 파일 데이터 반환
    @RequestMapping("/download/list")
    public List<FileEntity> downloadFileList(@RequestParam("group_ID") String Group_ID){
        return loaderDivider.getFileListData(Group_ID);
    }

    // 이미지 커스텀 편집기를 통해 편집된 이미지를 DB에 저장
    @RequestMapping("/save/custom")
    public String saveCustomImage(FileEntity result){
        return loaderDivider.saveCustomImage(result);
    }

    @RequestMapping("/save/custom/delete/object")
    public String saveCustomDeleteObject(@RequestBody ObjectEntity objectEntity){
        return loaderDivider.saveCustomDeleteObject(objectEntity);
    }

    @RequestMapping("/delete/one")
    public String deleteOne(@RequestParam("file_ID") String file_ID){
        return fileDeleteService.deleteAFileByFileID(file_ID);
    }

    @RequestMapping("/delete/group")
    public String deleteGroup(@RequestParam("group_ID") String group_ID){
        return fileDeleteService.deleteFileGroupByFileID(group_ID);
    }


}
