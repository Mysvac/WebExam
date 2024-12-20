package com.asaki0019.advertising.service.impl;

import com.asaki0019.advertising.mapper.UploadedFileMapper;
import com.asaki0019.advertising.model.UploadedFile;
import com.asaki0019.advertising.service.UploadedFileService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadedFileServiceImpl implements UploadedFileService {
    @Autowired
    private UploadedFileMapper uploadedFileMapper;

    @Override
    public UploadedFile saveUploadedFile(UploadedFile uploadedFile) {
        // 插入数据，MyBatis-Plus 会自动返回自增主键
        uploadedFileMapper.insert(uploadedFile);
        return uploadedFile; // 返回包含自增主键的 UploadedFile 对象
    }

    @Override
    public UploadedFile getUploadedFileById(String fileId) {
       return uploadedFileMapper.selectById(fileId);
    }

    @Override
    public void deleteUploadedFileById(String id) {
        uploadedFileMapper.deleteById(id);
    }

    @Override
    public void deleteFileFromFileSystem(String fileId) throws FileNotFoundException {
        if (StringUtils.isEmpty(fileId)) {
            throw new IllegalArgumentException("文件 ID 不能为空");
        }
        UploadedFile uploadedFile = uploadedFileMapper.selectById(fileId);
        if (uploadedFile == null) {
            throw new RuntimeException("文件不存在");
        }
        String filePath = uploadedFile.getFileUrl();
        if (StringUtils.isEmpty(filePath)) {
            throw new RuntimeException("文件路径为空");
        }
        String uploadDir = ResourceUtils.getURL("classpath:").getPath() + "static/uploads/";
        String baseUrl = "http://localhost:8080/uploads/";
        String fileName = filePath.substring(baseUrl.length());
        filePath = uploadDir + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("文件删除成功: " + filePath);
            } else {
                throw new RuntimeException("文件删除失败: " + filePath);
            }
        } else {
            throw new RuntimeException("文件不存在: " + filePath);
        }
    }
}