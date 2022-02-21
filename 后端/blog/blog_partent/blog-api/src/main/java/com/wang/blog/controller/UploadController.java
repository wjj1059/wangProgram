package com.wang.blog.controller;

import com.wang.blog.utils.QiniuUtils;
import com.wang.blog.vo.Result;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author 王家俊
 */
@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private QiniuUtils qiniuUtils;
    //Spring专门接收文件的类
    @PostMapping
    public Result upload(@RequestParam("image")MultipartFile file){
        //原始文件名称 比如aa.png
        String originalFilename = file.getOriginalFilename();
        //唯一文件名称
        String fileName = UUID.randomUUID().toString()+"."+ StringUtils.substringAfterLast(originalFilename,".");
        //上传文件 上传到七牛云服务器 按量付费速度快 把图片发到离用户最近的服务器上
        //降低自身应用服务武器的带宽消耗
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            return Result.success(qiniuUtils.url+fileName);
        }else {
            return Result.fail(20001,"上传失败");
        }
    }
}
