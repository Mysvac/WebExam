package com.asaki0019.advertising.controller;

import com.asaki0019.advertising.model.Ad;
import com.asaki0019.advertising.model.User;
import com.asaki0019.advertising.service.AdvertisingApplicationService;
import com.asaki0019.advertising.service.AdvertisingService;
import com.asaki0019.advertising.service.UploadedFileService;
import com.asaki0019.advertising.serviceMeta.data.AdData;
import com.asaki0019.advertising.serviceMeta.data.AdMetaData;
import com.asaki0019.advertising.serviceMeta.data.AdReviewData;
import com.asaki0019.advertising.serviceMeta.res.AdListMetaResponse;
import com.asaki0019.advertising.serviceMeta.res.AdListResponse;
import com.asaki0019.advertising.serviceMeta.res.AdReviewResponse;
import com.asaki0019.advertising.type.AdStatusEnum;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class AdvertisingFetchData {
    @Autowired
    private AdvertisingService advertisingService;

    @Autowired
    private UploadedFileService uploadedFileService;

    @Autowired
    private AdvertisingApplicationService advertisingApplicationService;

    @PostMapping("/advertising-table-data")
    public ResponseEntity<AdListMetaResponse> getAdvertisingTableData(HttpSession session) {
        try {
            User nowUser = (User) session.getAttribute("user");
            if (nowUser == null) {
                return ResponseEntity.status(401).body(new AdListMetaResponse(401, "用户不存在", null));
            }

            List<Ad> reviewedAds = advertisingService.getAllReviewedAdsWithUserAppliedStatus(nowUser.getId());
            List<String> appliedAdIds = advertisingApplicationService.selectAdIdsByUserId(nowUser.getId());
             // 将广告数据转换为前端需要的格式
            List<AdMetaData> adDataList = reviewedAds.stream().map(ad -> {
                String status = getAdStatus(ad.getStatusId());
                boolean isRequest = appliedAdIds.contains(ad.getId()); // 判断广告是否已申请
                return new AdMetaData(
                        ad.getId(),
                        ad.getTitle(),
                        status,
                        ad.getTags(),
                        ad.getDescription(),
                        ad.getAdvertiserName(), // 假设广告中有 advertiserName 字段
                        ad.getPrice(),
                        ad.getFileId(),
                        isRequest ? "已申请" : "未申请",
                        ad.getDistributed()
                );
            }).toList();

            // 构建响应
            AdListMetaResponse response = new AdListMetaResponse(200, "获取广告表格数据成功", adDataList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AdListMetaResponse(500, "获取广告表格数据失败: " + e.getMessage(), null));
        }
    }

    @PostMapping("/advertising-id-table-data")
    public ResponseEntity<AdListResponse> getAdvertisingUseTableData(HttpSession session) {
        try {
            User nowUser = (User) session.getAttribute("user");
            if (nowUser == null) {
                return ResponseEntity.status(401).body(new AdListResponse(401, "用户不存在", null));
            }

            // 获取当前用户的广告列表
            List<Ad> ads = advertisingService.getAdsByUser(nowUser.getId());

            // 过滤广告状态，只保留 "审核中"、"已发布" 的广告
            List<Ad> filteredAds = ads.stream()
                    .filter(ad -> Objects.equals(ad.getStatusId(), AdStatusEnum.UNDER_REVIEW.getId())
                            || Objects.equals(ad.getStatusId(), AdStatusEnum.PUBLISHED.getId()))
                    .toList();

            // 将广告数据转换为前端需要的格式
            List<AdData> adDataList = filteredAds.stream().map(ad -> {
                String status = getAdStatus(ad.getStatusId());
                return new AdData(
                        ad.getId(),
                        ad.getTitle(),
                        status,
                        ad.getTags(),
                        ad.getDescription(),
                        ad.getAdvertiserName(), // 假设广告中有 advertiserName 字段
                        ad.getPrice(),
                        ad.getFileId()
                );
            }).toList();

            // 将 adDataList 封装到 AdDataList 中

            // 构建响应
            AdListResponse response = new AdListResponse(200, "获取广告表格数据成功", adDataList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AdListResponse(500, "获取广告表格数据失败: " + e.getMessage(), null));
        }
    }

    @PostMapping("/advertising-review-data")
    public ResponseEntity<AdReviewResponse> adReviewTableResponse(HttpSession session) {
        try {
            User nowUser = (User) session.getAttribute("user");
            if (nowUser == null) {
                return ResponseEntity.status(401).body(new AdReviewResponse(401, "用户不存在", null));
            }

            // 获取当前用户的广告列表
            List<Ad> ads = advertisingService.getAllAds();

            // 过滤广告状态，只保留 "审核中" 的广告
            List<Ad> filteredAds = ads.stream()
                    .filter(ad -> Objects.equals(ad.getStatusId(), AdStatusEnum.UNDER_REVIEW.getId()))
                    .toList();

            // 将广告数据转换为前端需要的格式
            List<AdReviewData> adReviewDataList = filteredAds.stream().map(ad -> {
                String url = uploadedFileService.getUploadedFileById(ad.getFileId()).getFileUrl();
                return new AdReviewData(
                        ad.getId(),
                        ad.getTags(),
                        ad.getTitle(),
                        url,
                        ad.getDescription(),
                        ad.getAdvertiserName(),
                        ad.getPrice()
                );
            }).toList();

            // 构建响应
            AdReviewResponse response = new AdReviewResponse(200, "获取需要审核广告表格数据成功", adReviewDataList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AdReviewResponse(500, "获取需要审核广告表格数据失败: " + e.getMessage(), null));
        }
    }

    @PostMapping("/fetch-request-ads")
    public ResponseEntity<AdListMetaResponse> getAdvertisingAppliedData(@RequestBody Map<String, String> body) {
        try {
            var userCookie = body.get("userCookie");
            if (userCookie == null) {
                return ResponseEntity.status(500).body(new AdListMetaResponse(500, "获取已申请广告数据失败: cookie 错误 ", null));
            }

            List<Ad> reviewedAds = advertisingService.getAllReviewedAdsWithUserAppliedStatus(userCookie);
            List<String> appliedAdIds = advertisingApplicationService.selectAdIdsByUserId(userCookie);

            // 只保留已申请的广告
            List<AdMetaData> appliedAdDataList = reviewedAds.stream()
                    .filter(ad -> appliedAdIds.contains(ad.getId())) // 过滤已申请的广告
                    .map(ad -> {
                        String status = getAdStatus(ad.getStatusId());
                        String url = uploadedFileService.getUploadedFileById(ad.getFileId()).getFileUrl();
                        return new AdMetaData(
                                ad.getId(),
                                ad.getTitle(),
                                status,
                                ad.getTags(),
                                ad.getDescription(),
                                ad.getAdvertiserName(), // 假设广告中有 advertiserName 字段
                                ad.getPrice(),
                                url,
                                "已申请",
                                ad.getDistributed()
                        );
                    })
                    .toList();

            // 构建响应
            AdListMetaResponse response = new AdListMetaResponse(200, "获取已申请广告数据成功", appliedAdDataList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AdListMetaResponse(500, "获取已申请广告数据失败: " + e.getMessage(), null));
        }
    }

    // 根据广告状态ID获取状态名称
    private String getAdStatus(int statusId) {
        return switch (statusId) {
            case 1 -> AdStatusEnum.UNDER_REVIEW.getStatusName();
            case 2 -> AdStatusEnum.UNPUBLISHED.getStatusName();
            case 3 -> AdStatusEnum.PUBLISHED.getStatusName();
            default -> "未知状态";
        };
    }
}