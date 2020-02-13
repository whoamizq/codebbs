package com.whoamizq.codebbs.codebbs.controller;

import com.whoamizq.codebbs.codebbs.dto.FileDTO;
import com.whoamizq.codebbs.codebbs.provider.UCloudProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class FileController {
    @Autowired
    private UCloudProvider uCloudProvider;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request, HttpServletResponse response){
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        response.setHeader( "Content-Type" , "text/html" );
        MultipartFile file = multipartHttpServletRequest.getFile("editormd-image-file");
        try {
            String filename = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl(filename);
            return fileDTO;
        } catch (Exception e) {
            log.error("upload error",e);
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(0);
            fileDTO.setMessage("图片上传失败");
            return fileDTO;
        }

    }
}
