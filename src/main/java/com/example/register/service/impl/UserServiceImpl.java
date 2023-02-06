package com.example.register.service.impl;

import com.example.register.common.AppUtil;
import com.example.register.entity.User;
import com.example.register.exception.AppException;
import com.example.register.repository.UserRepository;
import com.example.register.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.soap.SOAPBinding;
import java.io.File;
import java.util.HashMap;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    @Value("${path_media_file}")
    private String pathMediaFile;

    @Autowired
    private UserRepository userRepository;
    @Override
    public void registerUser(String userName, String passWord,Integer userRole, MultipartFile mediaFile) throws AppException {
        HashMap<String,String> mediaPathFile = new HashMap<>();
        if(mediaFile != null && mediaFile.getSize() > 0){
            File dir1 = new File(pathMediaFile);
            if (!dir1.exists()) {
                dir1.mkdirs();
            }
            mediaPathFile = AppUtil.saveFile(mediaFile, pathMediaFile);
            if(mediaPathFile.get("checkSaveFile").equals("false")){
                throw new AppException("03", "Error when save media files");
            }
        }else {
            throw new AppException("02", "Media files cannot be empty");
        }
        User user = User.builder()
                .name(userName)
                .pass(passWord)
                .userRole(userRole)
                .mediaName(mediaPathFile.get("fileName"))
                .mediaUrl(mediaPathFile.get("pathFile"))
                .build();
        userRepository.saveAndFlush(user);
    }

    @Override
    public String findUserByFileName(String fileName) {
        String pathFile = null;
        Optional<User> user = userRepository.findByMediaName(fileName);
        if(user.isPresent()){
            pathFile = user.get().getMediaUrl();
        }
        return pathFile;
    }
}
