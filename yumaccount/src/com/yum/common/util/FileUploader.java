package com.yum.common.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;

public class FileUploader {


    public static String doFileUpload(FileItem fileItem, String empId) throws IOException{
        InputStream in = fileItem.getInputStream();
      
        String realFileName = fileItem.getName().substring(fileItem.getName().lastIndexOf("//") + 1);
      
        String fileExt = realFileName.substring(realFileName.lastIndexOf("."));
   
        String saveFileName = empId + fileExt;

        String uploadPath ="C:/Users/yuni5/WorkSpace/yumAccount/WebContent/Photos/empPhoto/";
       
        FileOutputStream fout = new FileOutputStream(uploadPath + saveFileName);
     
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = in.read(buffer, 0, 8192)) != -1)
            fout.write(buffer, 0, bytesRead);

        in.close();
        fout.close();

        return "/Photos/empPhoto/" + saveFileName;
       
    }
}
