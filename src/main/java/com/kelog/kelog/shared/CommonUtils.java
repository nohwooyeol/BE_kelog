package com.kelog.kelog.shared;


import java.util.UUID;

public class CommonUtils {

    private static final String Sever_name = "kelog";
    private static final String FILE_EXTENSION_SEPARATOR = ".";

    public static String buildFileName(String originalFileName) {
//        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
//        String fileExtension = originalFileName.substring(fileExtensionIndex);
//        String fileName = originalFileName.substring(0, fileExtensionIndex);
//        String now = String.valueOf(System.currentTimeMillis());

//        return  Sever_name  + now + FILE_EXTENSION_SEPARATOR + fileExtension;
        return UUID.randomUUID().toString();
    }
}