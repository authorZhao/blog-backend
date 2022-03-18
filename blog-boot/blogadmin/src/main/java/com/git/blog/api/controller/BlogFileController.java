package com.git.blog.api.controller;

import com.git.blog.commmon.ApiResponse;
import com.git.blog.service.BlogFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author authorZhao
 */
@RestController
@RequestMapping("/api/file")
public class BlogFileController {

    @Autowired
    private BlogFileService fileService;

    @PostMapping("/imgUpLoad")
    public ApiResponse<String> imgUpLoad(@RequestParam(value = "imgFile", required = false) MultipartFile file){
        String url = fileService.upLoadFile(file,null);
        return ApiResponse.ok(url);
    }

}
