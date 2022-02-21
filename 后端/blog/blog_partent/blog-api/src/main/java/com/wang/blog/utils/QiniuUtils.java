package com.wang.blog.utils;

import com.alibaba.fastjson.JSON;
import com.qiniu.http.Response;

import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 王家俊
 */
@Component
public class QiniuUtils {
    //"r7lnzehi4.bkt.clouddn.com为七牛云的30天有效域名"
    public static  final String url = "http://r7lnzehi4.bkt.clouddn.com/";

    @Value("${qiniu.accessKey}")
    private  String accessKey;
    @Value("${qiniu.accessSecretKey}")
    private  String accessSecretKey;

    public  boolean upload(MultipartFile file, String fileName){

        //构造一个带指定 Region 对象的配置类
        //Configuration cfg = new Configuration(Region.region0());
        Configuration cfg = new Configuration(Region.autoRegion());
        //String region ="up-cn-east-2.qiniup.com";
        /*Region region = new Region.Builder()
                .region("up-cn-east-2")
                .accUpHost("http://upload.qiniup.com")
                .srcUpHost("http://upload.qiniup.com")
                .iovipHost("http://iovip.qbox.me")
                .rsHost("http://rs.qiniu.com")
                .rsfHost("http://rsf.qiniu.com")
                .apiHost("http://api.qiniu.com")
                .build();*/
        //Configuration cfg = new Configuration(region);
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String bucket = "xiaowangbolg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey, accessSecretKey);
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(uploadBytes, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
