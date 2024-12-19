package com.asaki0019.advertising.service.impl;

import com.asaki0019.advertising.mapper.AdApplicationMapper;
import com.asaki0019.advertising.mapper.AdMapper;
import com.asaki0019.advertising.model.AdApplication;
import com.asaki0019.advertising.service.AdvertisingApplicationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdvertisingApplicationServiceIImpl implements AdvertisingApplicationService {
    @Autowired
    private AdApplicationMapper adApplicationMapper;

    @Autowired
    private AdMapper adMapper;
    @Override
    public List<String> selectAdIdsByUserId(String userId) {
        return  adApplicationMapper.selectAdIdsByUserId(userId);
    }

    @Override
    public AdApplication applyForAd(String adId, String applicantId) {
        AdApplication application = new AdApplication();
        application.setAdId(adId);
        application.setApplicantId(applicantId);
        application.setApplicationTime(LocalDateTime.now());
        // 保存广告申请记录
        adApplicationMapper.insert(application);
        return application;
    }

    @Override
    public AdApplication unApplyForAd(String adId, String applicantId) {
        // 构建删除条件
        QueryWrapper<AdApplication> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ad_id", adId).eq("applicant_id", applicantId);

        // 删除广告申请记录
        int deleted = adApplicationMapper.delete(queryWrapper);

        // 如果删除成功，返回一个空的 AdApplication 对象
        if (deleted > 0) {
            return new AdApplication();
        } else {
            throw new RuntimeException("Failed to delete ad application for adId: " + adId + " and applicantId: " + applicantId);
        }
    }
}