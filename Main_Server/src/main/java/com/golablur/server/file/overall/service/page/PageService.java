package com.golablur.server.file.overall.service.page;

import com.golablur.server.file.ai.divider.LatentDiffusionDivider;
import com.golablur.server.file.overall.domain.*;
import com.golablur.server.file.overall.mapper.FileMapper;
import com.golablur.server.file.overall.mapper.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class PageService {

    @Autowired
    FileMapper fileMapper;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    LatentDiffusionDivider latentDiffusionDivider;


    public List<FileObjectDTO> getNonProcessedImages(String user_id) {
        List<FileObjectDTO> list = new ArrayList<>();

        // 해당하는 File 을 가져와서 FileObjectDTO 생성한 후 리스트에 add
        List<FileEntity> fileList = fileMapper.getNonProcessedImageDataByUser_ID(user_id);
        log.info("FileList: " + fileList);
        for(FileEntity file : fileList){
            list.add(FileObjectDTO.builder()
                    .file(file)
                    .objectList(objectMapper.getDetectionObjectListByFile(file))
                    .build());
        }

        return list;
    }

    public List<FileObjectDTO> getNonProcessedVideos(String user_id){
        log.info("getNonProcessedVideos");
        List<FileObjectDTO> list = new ArrayList<>();
        // 해당하는 File 을 가져와서 FileObjectDTO 생성한 후 리스트에 add
        List<FileEntity> fileList = fileMapper.getNonProcessedVideoByUser_ID(user_id);
        log.info("FileList: " + fileList.toString());
        for(FileEntity file : fileList){
            list.add(FileObjectDTO.builder()
                    .file(file)
                    .objectList(objectMapper.getDetectionObjectListByFile(file))
                    .build());
        }
        log.info("list : " + list.toString());
        log.info("getNonProcessedVideos Success");
        return list;
    }

    public List<GroupFileObjectNameDTO> getNonProcessedImageGroups(String user_id) {
        List<GroupFileObjectNameDTO> list = new ArrayList<>();

        // Group_ID 가 있는 File 들 중에서 Original_File_ID 가 없는 그룹만을 가져옴
        List<String> groupList = fileMapper.getNonProcessedGroupByUser_ID(user_id);
        log.info("getNonProcessedImageGroups "+groupList.toString());
        for(String group : groupList){
            List<FileEntity> fileList = fileMapper.getFileDataByGroup_ID(group);
            list.add(
                    GroupFileObjectNameDTO.builder()
                            .groupFileEntity(fileList)
                            .objectNameList(getObjectNameList(fileList))
                            .build()
            );
        }

        log.info("response :  "+list.toString());

        return list;
    }

    private List<String> getObjectNameList(List<FileEntity> fileList) {
        // fileList 의 object 이름들을 중복을 제거하여 반환
        List<String> list = new ArrayList<String>();
        Set<String> set = new HashSet<>();
        for(FileEntity file : fileList){
            List<String> nameList = objectMapper.getObjectNameByFile(file);
            for(String name : nameList){
                set.add(name);
            }
        }
        // 세트를 리스트로 변환
        for(String name : set){
            list.add(name);
        }
        return list;
    }


    public List<List<FileEntity>> getResulFiletList(String user_id) {
        List<List<FileEntity>> list = new ArrayList<>();

        // Original_File_ID 가 있는 FileEntity 를 가져옴 ( Sysdate 로 정렬 됨 )
        List<FileEntity> processedFileList = fileMapper.getProcessedFileDataByUser_ID(user_id);

        // TODO 그룹별로 나누어 저장
        List<FileEntity> group = new ArrayList<>();
        int cnt = 0;
        FileEntity lastFile = new FileEntity();
        for(FileEntity file: processedFileList){
            log.info("This file is : "+ file.getFile_ID());
            if(cnt == 0){
                // 첫 파일
                group.add(file);
                lastFile = group.get(group.size()-1);
            }
            else{
                if(file.getGroup_ID() == null || !file.getGroup_ID().equals(lastFile.getGroup_ID())){
                    // 지난 파일과 다른 그룹일 때
                    list.add(group);
                    group = new ArrayList<>();
                }
                group.add(file);
                lastFile = group.get(group.size()-1);
            }
            if(cnt+1 == processedFileList.size()){
                list.add(group);
                group = new ArrayList<>();
            }
            cnt = cnt+1;
        }
        log.info(list.toString());

        return list;
    }


    public DeepFakeSourceTargetDTO getDeepFakeFileEntity(String sourceFileId) {
        return DeepFakeSourceTargetDTO.builder()
                .source(fileMapper.getFileDataByFile_ID(sourceFileId))
                .target(objectMapper.getDeepFakeFileBySourceFile_ID(sourceFileId))
                .build();
    }

    public DeepFakeSourceTargetGroupDTO getDeepFakeFileGroupEntity(String sourceFileGroupId) {
        return DeepFakeSourceTargetGroupDTO.builder()
                .sourceGroup(fileMapper.getFileDataByGroup_ID(sourceFileGroupId))
                .target(objectMapper.getObjectByGroup_ID(sourceFileGroupId))
                .build();
    }

}
