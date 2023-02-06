package com.example.register.controller;

import com.example.register.exception.AppException;
import com.example.register.helper.AppHelper;
import com.example.register.response.BaseResponse;
import com.example.register.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class UserRegisterController {
    @Autowired
    private UserService userService;
    @PostMapping(value = "/register")
    public ResponseEntity<BaseResponse> registerUser(@RequestParam HashMap<String, String> reqParams,
                                                     @RequestParam(required = false, name = "mediaFile") MultipartFile mediaFile){
        log.info("Start process register user");
        BaseResponse rp = BaseResponse.builder()
                .code("00")
                .message("Success")
                .build();
        try {
            String userName = reqParams.get("userName");
            String passWord = reqParams.get("passWord");
            Integer userRole = Integer.valueOf(reqParams.get("userRole"));
            userService.registerUser(userName,passWord,userRole,mediaFile);
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            Throwable rootcause = AppHelper.getrootcause(ex);
            if (rootcause instanceof AppException) {
                AppException apex = (AppException) rootcause;
                rp.setCode(apex.getCode());
                rp.setMessage(apex.getMessage());
            } else {
                log.error("EX Error: {}", ex);
                rp.setCode("96");
                rp.setMessage("System error");
            }
        }
        return new ResponseEntity<>(rp, HttpStatus.OK);
    }

    @GetMapping(value = "search-file")
    public ResponseEntity<BaseResponse> processSearchFile(@RequestParam("fileName") String fileName){
        BaseResponse rp = BaseResponse.builder()
                .code("00")
                .message("Success")
                .build();
        try {
            String pathFile = userService.findUserByFileName(fileName);
            rp.setData(pathFile);
        }catch (Exception ex){
            log.error("EX Error: {}", ex);
            rp.setCode("96");
            rp.setMessage("System error");
        }
        return new ResponseEntity<>(rp, HttpStatus.OK);
    }

}
