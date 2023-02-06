package com.example.register.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Random;
@Slf4j
public class AppUtil {
    private static final String alpha = "abcdefghijklmnopqrstuvwxyz"; // a-z
    private static final String alphaUpperCase = alpha.toUpperCase(); // A-Z
    private static final String digits = "0123456789"; // 0-9
//    private static final String specials = "~=+%^*/()[]{}/!@#$?|";
    private static final String ALPHA_NUMERIC = alpha + alphaUpperCase + digits;
    private static Random generator = new Random();
    public static String getFileExtension(String file) {
        String extension = "";
        try {
            extension = file.substring(file.lastIndexOf("."));
        } catch (Exception e) {
            extension = "";
        }
        return extension;
    }

    public static String randomAlphaNumeric(int numberOfCharactor) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfCharactor; i++) {
            int number = randomNumber(0, ALPHA_NUMERIC.length() - 1);
            char ch = ALPHA_NUMERIC.charAt(number);
            sb.append(ch);
        }
        return sb.toString();
    }

    public static int randomNumber(int min, int max) {
        return generator.nextInt((max - min) + 1) + min;
    }

    public static HashMap<String, String> saveFile(MultipartFile file, String pathMediaFile) {
        HashMap<String,String> map = new HashMap<>();
        String fileName = "file_media_" + randomAlphaNumeric(8) + getFileExtension(file.getOriginalFilename());
        Path path = Paths.get(pathMediaFile + fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            map.put("checkSaveFile","true");
        } catch (IOException e) {
            log.error("Error while save file", e);
            map.put("checkSaveFile","false");
        }
        String originalFileName = file.getOriginalFilename();
        map.put("pathFile",path.toString());
        map.put("fileName",originalFileName.substring(0,originalFileName.lastIndexOf(".")));
        return map;
    }
}
