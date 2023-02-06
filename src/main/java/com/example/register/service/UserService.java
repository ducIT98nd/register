package com.example.register.service;

import com.example.register.exception.AppException;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void registerUser(String userName, String passWord,Integer userRole, MultipartFile mediaFile) throws AppException;

    String findUserByFileName(String fileName);
}
