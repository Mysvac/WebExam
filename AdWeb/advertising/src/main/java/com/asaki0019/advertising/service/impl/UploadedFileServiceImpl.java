package com.asaki0019.advertising.service.impl;

import com.asaki0019.advertising.mapper.UploadedFileMapper;
import com.asaki0019.advertising.model.UploadedFile;
import com.asaki0019.advertising.service.UploadedFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}