package com.golablur.server.user;

import com.golablur.server.user.domain.DuringWorkDTO;
import com.golablur.server.user.domain.LoginDTO;
import com.golablur.server.user.domain.UserEntity;
import com.golablur.server.user.service.LoginService;
import com.golablur.server.user.service.SignUpService;
import com.golablur.server.user.service.UserDeleteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@Slf4j
public class UserRestController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private SignUpService signUpService;
    @Autowired
    private UserDeleteService userDeleteService;



    @RequestMapping("/normal/login")
    public UserEntity normalLogin(LoginDTO loginDTO) {
        log.info("ID: " + loginDTO.getUser_ID() + "    PW: " + loginDTO.getUser_PW());
        return loginService.normalLogin(loginDTO);
    }

    @RequestMapping("/signup")
    public String normalSignup(UserEntity userEntity) {
        return signUpService.normalSignup(userEntity);
    }

    @RequestMapping("/social/signup")
    public String socialSignup(UserEntity userEntity) {
        if(signUpService.IDCheck(userEntity.getUser_ID()).equals("400")){
            return "400";
        }
        log.info("signup successful: "+ userEntity.getUser_ID());
        return signUpService.normalSignup(userEntity);
    }

    @RequestMapping("/id/check")
    public String idCheck(@RequestParam("User_ID") String User_ID) {
        log.info(User_ID);
        return signUpService.IDCheck(User_ID);
    }

    @RequestMapping("/delete")
    public int deleteUser(@RequestParam("User_ID") String User_ID){
        return userDeleteService.userDelete(User_ID);
    }

    // 작업 도중 로그인을 했을 경유
    // 작업하던 토큰값의 데이터를 로그인한 토큰값의 데이터와 합쳐줘야함.
    // 작업하던 토큰값의 DB 에서의 File 데이터를 로그인한 토큰값의 데이터로 리스트를 만들어 둔 뒤
    // 작업하던 토큰값의 데이터를 삭제하고 만들어둔 리스트를 로그인한 토큰값의 데이터로 저장해준다.
    // User_ID 속성값만 바꾸어 저장한다.

    @RequestMapping("/social/login/duringWork")
    public String socialLoginDuringWork(DuringWorkDTO duringWorkDTO){
        return loginService.duringWork(duringWorkDTO);
    }

    @RequestMapping("/normal/login/duringWork")
    public UserEntity normalLoginDuringWork(DuringWorkDTO duringWorkDTO) {
        return loginService.normalLoginDuringWork(duringWorkDTO);
    }

}
