package com.codelikealexito.articles.api.util;

import com.codelikealexito.articles.api.exceptions.CustomResponseStatusException;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

public class Helper {
    
    public static String convertFromStringArrayToString(String[] strArr) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < strArr.length; i++) {
            sb.append(strArr[i]);
        }
        String resStr = Arrays.toString(strArr);
        String newStr = resStr.replace("[", "");
        String newestStr = newStr.replace("]", "");
        return newestStr;
    }

    public static String convertFromByteArrayToBase64(byte[] image) {
        byte[] enteredString = Base64.getEncoder().encode(image);
        byte[] decodedString = new byte[0];
        try {
            decodedString = Base64.getDecoder().decode(new String(enteredString).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
//            log.error("UnsupportedEncodingException occurred while trying to convert base64 string to byte array.");
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "ERRXXX", "UnsupportedEncodingException occurred while trying to convert base64 string to byte array.");
        }
        return new String(decodedString);
    }

    public static byte[] convertFromBase64StringToByteArray(String base64String) {
        byte[] enteredString = Base64.getEncoder().encode(base64String.getBytes());
        byte[] decodedString = new byte[0];
        try {
            decodedString = Base64.getDecoder().decode(new String(enteredString).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
//            log.error("UnsupportedEncodingException occurred while trying to convert base64 string to byte array.");
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "ERRXXX", "UnsupportedEncodingException occurred while trying to convert base64 string to byte array.");
        }
        return decodedString;
    }

    public static byte[] saveArticleAsByteArray(String articlePath) {
        try{
            byte[] bytes = Files.readAllBytes(Paths.get(articlePath));
            return bytes;
        } catch (IOException ioex) {
            System.out.println("IO Exception occurred while writing image in byte array.");
        }
        System.out.println("Here");
        return new byte[]{};
    }
    
}
