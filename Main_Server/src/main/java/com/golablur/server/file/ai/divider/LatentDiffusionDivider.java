package com.golablur.server.file.ai.divider;

import com.golablur.server.file.ai.service.ObjectService;
import com.golablur.server.file.ai.service.SendToAPIService;
import com.golablur.server.file.loader.service.storeFileData.StoreFileDataService;
import com.golablur.server.file.overall.domain.FileEntity;
import com.golablur.server.file.overall.domain.FileObjectDTO;
import com.golablur.server.file.overall.domain.ProcessingFileObjectDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class LatentDiffusionDivider {

    @Autowired
    private ObjectService objectService;
    @Autowired
    private StoreFileDataService storeFileDataService;
    @Autowired
    private SendToAPIService send;

    // 객체 삭제 후 반환


    public FileEntity deleteOneImage(ProcessingFileObjectDTO fileObject) {
        // DB에 접근하여 AIFUnctionDTO 를 채운다.
        FileObjectDTO fileObjectDTO = objectService.getFileObjectDTO(fileObject);
        // AIFUnctionDTO 를 AI API 로 전송하고 처리된 파일을 반환 받는다.
        log.info("deleteOneImage - before send : " + fileObjectDTO.toString());
        FileEntity processedFile = send.processDeleteOneImage(fileObjectDTO);
        if(processedFile == null){
            log.error("deleteOneImage process failed");
            return null;
        }
        // 처리된 파일을 DB에 저장한다.
        if(storeFileDataService.storeFile(processedFile).equals("500")){
            log.error("deleteOneImage storeFile failed");
            return null;
        }
        storeFileDataService.updateProcessedFileData(processedFile.getOriginal_File_ID());
        log.info("DeleteOneImage successful");
        return processedFile;
    }


    public List<FileEntity> deleteALotImages(List<ProcessingFileObjectDTO> fileObjectList) {
        // 하나의 이미지 처리를 반복
        List<FileEntity> processedList = new ArrayList<>();
        int cnt = 0;
        for(ProcessingFileObjectDTO fileObject : fileObjectList){
            FileEntity fileEntity = deleteOneImage(fileObject);
            if (fileEntity == null) {
                log.error("deleteALotImages failed : index "+cnt);
            }
            processedList.add(fileEntity);
            cnt++;
        }
        log.info("DeleteALotImages successful");
        return processedList;
    }

}
