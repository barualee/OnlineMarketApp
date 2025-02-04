package com.onlinemarket.OnlinemarketProjectBackend;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
    //logger factory
	public static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);
    
    public static void saveFile(String uploadFileDir, String fileName, MultipartFile multipartFile) throws IOException{
        Path uploadPath = Paths.get(uploadFileDir);
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException e) {
        	throw new IOException("Could not save file: "+fileName, e);
        }
    } 

    public static void deleteFiles(String dir){
        Path dirPath = Paths.get(dir);

        try{
            Files.list(dirPath).forEach(file -> {
                if(!Files.isDirectory(file)) {
                    try{
                        Files.delete(file);
                    } catch (IOException e){
                        LOGGER.error("Could not delete file: "+file);
                    	//System.out.println("Could not delete file: "+file);
                    }
                }
            });
        } catch(IOException ex){
            LOGGER.error("Could not list directory: "+dirPath);
        	//System.out.println("Could not list directory: "+dirPath);
        }
    }

    public static void deleteDir(String dir) {
        deleteFiles(dir);
        try{
            Files.delete(Paths.get(dir));
        } catch (IOException ex){
            LOGGER.error("Could not remove Directory: "+dir);
        }
    }
}
