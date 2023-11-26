package com.golablur.server.file.overall.restcontroller;

import com.golablur.server.file.ai.divider.DeepFakeDivider;
import com.golablur.server.file.overall.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/file/process/deepfake")
@CrossOrigin("*")
@Slf4j
public class DeepFakeProcessRestController {

    @Autowired
    private DeepFakeDivider deepFakeDivider;

    // 하나의 이미지
    // input => target_file_ID, source_file_ID
    @RequestMapping("/one/image")
    public FileEntity deepFakeOneImage(DeepFakeDTO deepFakeDTO){
        return deepFakeDivider.deepFakeOneImage(deepFakeDTO);
    }

    // 하나의 비디오
    // input => target_file_ID, source_file_ID
    @RequestMapping("/one/video")
    public FileEntity deepFakeOneVideo(DeepFakeDTO deepFakeDTO){
        return deepFakeDivider.deepFakeOneVideo(deepFakeDTO);
    }


    // TODO 여러 장의 이미지
    // input => group_ID, source_file_ID
    @RequestMapping("/alot/images")
    public List<FileEntity> deepFakeALotImages(DeepFakeDTO deepFakeDTO){
        return deepFakeDivider.deepFakeALotImages(deepFakeDTO);
    }

    // TODO 적용할 이미지 업로드
    @RequestMapping("/upload/target/image")
    public String uploadTargetImage(@RequestParam("file_ID") String file_ID, @RequestParam("target_file_ID") String target_file_ID,
                                    @RequestParam("user_ID") String user_ID, @RequestParam("file_Extension") String file_Extension,
                                    @RequestParam("real_File_Name") String real_File_Name, @RequestParam("path") String path){
        log.info("uploadTargetImage");
        log.info("path : " + path);
        return deepFakeDivider.uploadTargetImage(
                FileID_FileEntityDTO.builder()
                        .file_ID(file_ID)
                        .fileEntity(FileEntity.builder()
                                .file_ID(target_file_ID)
                                .user_ID(user_ID)
                                .file_Extension(file_Extension)
                                .real_File_Name(real_File_Name)
                                .path(path)
                                .build())
                        .build());
    }

    @RequestMapping("/upload/group/target/image")
    public String uploadGroupTargetImage(@RequestParam("group_ID") String group_ID, @RequestParam("target_file_ID") String target_file_ID,
                                         @RequestParam("user_ID") String user_ID, @RequestParam("file_Extension") String file_Extension,
                                         @RequestParam("real_File_Name") String real_File_Name, @RequestParam("path") String path) {
        log.info("uploadGroupTargetImage");
        return deepFakeDivider.uploadGroupTargetImage(
            FileID_FileEntityDTO.builder()
                    .file_ID(group_ID)
                    .fileEntity(FileEntity.builder()
                            .file_ID(target_file_ID)
                            .user_ID(user_ID)
                            .file_Extension(file_Extension)
                            .real_File_Name(real_File_Name)
                            .path(path)
                            .build())
                    .build()
        );
    }

}
