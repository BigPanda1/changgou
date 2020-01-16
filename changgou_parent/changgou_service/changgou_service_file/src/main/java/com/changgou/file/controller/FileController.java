package com.changgou.file.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.file.utils.FastDFSClient;
import com.changgou.file.utils.FastDFSFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/file")
public class FileController {

    @PostMapping("/upload")
    public Result fileUpload(MultipartFile file){
        try {
            if (file == null){
                throw new RuntimeException("文件不存在");
            }
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null){
                throw new RuntimeException("文件不存在");
            }

            String etxName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            byte[] fileBytes = file.getBytes();

            FastDFSFile fastDFSFile = new FastDFSFile(originalFilename,fileBytes,etxName);

            String[] strings = FastDFSClient.upload(fastDFSFile);
            String  url = FastDFSClient.getTrackerUrl() + strings[0] +"/"+ strings[1];
            return new Result(true,StatusCode.OK,"文件上传成功",url);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new Result(false,StatusCode.ERROR,"文件上传失败");
    }
}
