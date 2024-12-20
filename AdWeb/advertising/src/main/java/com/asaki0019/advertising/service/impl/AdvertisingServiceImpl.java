package com.asaki0019.advertising.service.impl;

import com.asaki0019.advertising.mapper.AdApplicationMapper;
import com.asaki0019.advertising.mapper.AdMapper;
import com.asaki0019.advertising.mapper.AdStatusMapper;
import com.asaki0019.advertising.model.Ad;
import com.asaki0019.advertising.model.AdApplication;
import com.asaki0019.advertising.service.AdvertisingService;
import com.asaki0019.advertising.service.UploadedFileService;
import com.asaki0019.advertising.type.AdStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdvertisingServiceImpl implements AdvertisingService {
    private final AdMapper adMapper;
    private final AdApplicationMapper adApplicationMapper;
    private final AdStatusMapper adStatusMapper;
    private final UploadedFileService uploadedFileService;

    @Autowired
    public AdvertisingServiceImpl(
            AdMapper adMapper,
            AdApplicationMapper adApplicationMapper,
            AdStatusMapper adStatusMapper,
            UploadedFileService uploadedFileService) {
        this.adMapper = adMapper;
        this.adApplicationMapper = adApplicationMapper;
        this.adStatusMapper = adStatusMapper;
        this.uploadedFileService = uploadedFileService;
    }


    @Override
    @Transactional
    public Ad createAd(Ad ad, String advertiserId, String fileId) {
        // 验证 fileId 是否存在
        if (uploadedFileService.getUploadedFileById(fileId) == null) {
            throw new IllegalArgumentException("File with ID " + fileId + " does not exist.");
        }

        // 设置广告的初始状态为“审核中”
        ad.setStatusId(AdStatusEnum.UNDER_REVIEW.getId());
        ad.setAdvertiserId(advertiserId);
        ad.setFileId(fileId);

        // 保存广告到数据库
        adMapper.insert(ad);
        return ad;
    }

    @Override
    @Transactional
    public Ad reviewAd(String adId, int statusId) {
        Ad ad = adMapper.selectById(adId);
        if (ad == null) {
            throw new IllegalArgumentException("Ad with ID " + adId + " does not exist.");
        }
        ad.setStatusId(statusId);
        adMapper.updateById(ad);
        return ad;
    }

    @Override
    public Ad getAdByAdId(String adId) {
        Ad ad = adMapper.selectById(adId);
        if (ad == null) {
            throw new IllegalArgumentException("Ad with ID " + adId + " does not exist.");
        }
        return ad;
    }

    @Override
    public void deleteAd(String adId, String advertiserId) {
        Ad ad = adMapper.selectById(adId);
        if (ad == null) {
            throw new IllegalArgumentException("Ad with ID " + adId + " does not exist.");
        }
        if (!ad.getAdvertiserId().equals(advertiserId)) {
            throw new IllegalArgumentException("You are not authorized to delete this ad.");
        }

        // 删除广告
        adMapper.deleteById(adId);
    }

    @Override
    public List<Ad> getAdsByUser(String userId) {
        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("advertiser_id", userId); // 根据 advertiser_id 查询
        return adMapper.selectList(queryWrapper);
    }

    @Override
    public List<AdApplication> getAdApplicationsByUser(String userId) {
        return List.of();
    }

    @Override
    public List<Ad> getAllReviewedAdsWithUserAppliedStatus(String userId) {
        return adMapper.selectList(new QueryWrapper<Ad>()
                .ne("status_id", AdStatusEnum.UNDER_REVIEW.getId()));
    }


    @Override
    public List<Ad> getAllAds() {
        return adMapper.selectList(null); // 查询所有广告
    }

    @Override
    public Integer getAdCountByTag(String tag) {
        return adMapper.countByTag(tag);
    }

    @Override
    public Integer getDistributedAdCountByTag(String tag) {
        return adMapper.getDistributedCountByTag(tag);
    }
}