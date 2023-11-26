package com.golablur.server.file.overall.restcontroller;

import com.golablur.server.file.overall.domain.*;
import com.golablur.server.file.overall.service.page.PageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/page")
@CrossOrigin("*")
@Slf4j
public class PageRestController {

    @Autowired
    private PageService pageService;

    @RequestMapping("/image/editor")
    public List<FileObjectDTO> imageEditor(@RequestParam("id") String User_ID){
        // 유저의 파일 중에서 완성되지 않은 ( 결과물이 없는 ) 파일들과 객체들의 리스트를 반환
        // jpg, png, jpeg
        return pageService.getNonProcessedImages(User_ID);
    }

    @RequestMapping("/video/editor")
    public List<FileObjectDTO> videoEditor(@RequestParam("id") String User_ID){
        // mp4
        log.info("videoEditor");
        return pageService.getNonProcessedVideos(User_ID);
    }

    @RequestMapping("/list/image/editor")
    public List<GroupFileObjectNameDTO> listImageEditor(@RequestParam("id") String User_ID){
        // 유저의 파일 중 그룹으로 구성된 파일들과 객체들을 그룹 별로 리스트로 묶어 보내준다.
        return pageService.getNonProcessedImageGroups(User_ID);
    }

    @RequestMapping("/result")
    public List<List<FileEntity>> resultPage(@RequestParam("id") String User_ID) {
        // 유저의 파일 중 원본 파일 아이디가 저장되어있는 파일들의 리스트를 반환
        return pageService.getResulFiletList(User_ID);
    }

    @RequestMapping("/deepfake/one")
    public DeepFakeSourceTargetDTO deepFakePage(@RequestParam("source_file_ID") String source_file_ID){
        // 딥페이크 페이지를 위한 엔드 포인트
        log.info("deepFakePage");
        log.info("source_file_ID : "+source_file_ID);
        return pageService.getDeepFakeFileEntity(source_file_ID);
    }

    @RequestMapping("/deepfake/group")
    public DeepFakeSourceTargetGroupDTO deepFakeGroupPage(@RequestParam("source_file_group_ID") String source_file_group_ID){
        log.info("deepFakeGroupPage");
        log.info("source_file_group_ID : " + source_file_group_ID);
        return pageService.getDeepFakeFileGroupEntity(source_file_group_ID);
    }

}
