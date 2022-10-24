package com.fenglinhai.reggie.controller;

import com.fenglinhai.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Harley
 * @create 2022-10-20 16:38
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){//参数file名字不能乱写
        //file时一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString()+"临时文件");
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用uuid重新生成文件名，防止文件名称重复造成文件覆盖
        String uuid = UUID.randomUUID().toString();
        String fileName=uuid+suffix;

        //创建一个目录对象
        File dir=new File(basePath);
        if(!dir.exists()){
            //不存在
            dir.mkdir();//创建目录
        }
        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fis=new FileInputStream(new File(basePath+name));
            //输出流，通过输出流将文件写会浏览器
            ServletOutputStream ops = response.getOutputStream();

            response.setContentType("image/jpeg");//图片文件
            int len=0;
            byte[]bytes=new byte[1024];
            while ((len=fis.read(bytes))!=-1){
                ops.write(bytes,0,len);
                ops.flush();
            }
            fis.close();
            ops.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
