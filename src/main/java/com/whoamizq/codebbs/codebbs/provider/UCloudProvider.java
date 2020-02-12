package com.whoamizq.codebbs.codebbs.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import com.whoamizq.codebbs.codebbs.exception.CustomizeErrorCode;
import com.whoamizq.codebbs.codebbs.exception.CustomizeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
public class UCloudProvider {
    //公钥
    @Value("${ucloud.ufile.public-key}")
    private String publicKey;
    //密钥
    @Value("${ucloud.ufile.private-key}")
    private String privateKey;
    //存储桶名称
    @Value("${ucloud.ufile.bucket}")
    private String bucketName;
    //地域
    @Value("${ucloud.ufile.region}")
    private String region;
    //后缀
    @Value("${ucloud.ufile.suffix}")
    private String suffix;
    //过期时间
    @Value("${ucloud.ufile.expires}")
    private Integer expires;

    /**
     * 上传文件
     * @param fileStream 文件大小
     * @param mimeType 文件类型
     * @param fileName 文件名称
     * @return 返回
     */
    public String upload(InputStream fileStream,String mimeType,String fileName){
        String generatedFileName;
        //拼接地址
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length > 1){
            //设置图片名称
            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length-1];
        }else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        try {
            //开始上传
            ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(publicKey,privateKey);
            ObjectConfig config = new ObjectConfig(region,suffix);
            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(fileStream, mimeType)
                    .nameAs(generatedFileName)
                    .toBucket(bucketName)
                    .setOnProgressListener((bytesWritten, contentLength) -> {
                    })
                    .execute();
            //获取图片存储地址,设置过期时间
            if (response != null && response.getRetCode() == 0) {
                String url = UfileClient.object(objectAuthorization, config)
                        .getDownloadUrlFromPrivateBucket(generatedFileName, bucketName, expires)
                        .createUrl();
                return url;
            }else {
                log.error("upload error,{}",response);
                throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
            }
        } catch (UfileClientException e) {
            log.error("upload error,{}",fileName,e);
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } catch (UfileServerException e) {
            log.error("upload error,{}",fileName,e);
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
    }

}
