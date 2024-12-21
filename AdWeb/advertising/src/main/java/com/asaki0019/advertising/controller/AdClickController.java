package com.asaki0019.advertising.controller;

import com.asaki0019.advertising.model.Ad;
import com.asaki0019.advertising.model.AdClick;
import com.asaki0019.advertising.service.AdClickService;
import com.asaki0019.advertising.service.AdvertisingApplicationService;
import com.asaki0019.advertising.service.AdvertisingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AdClickController {

    private final AdvertisingService advertisingService;
    private final AdClickService adClickService;
    private final AdvertisingApplicationService advertisingApplicationService;

    private static final String AD_URL_PREFIX = "http://localhost:5173/#/SAD/";
    public AdClickController(AdvertisingService advertisingService,
                             AdClickService adClickService,
                             AdvertisingApplicationService advertisingApplicationService) {
        this.advertisingService = advertisingService;
        this.adClickService = adClickService;
        this.advertisingApplicationService = advertisingApplicationService;
    }


    /**
     * 处理广告点击请求。
     *
     * @param request 包含广告点击信息的请求体
     * @return 包含匹配广告 URL 的响应实体
     */
    @PostMapping("/ad-click")
    public ResponseEntity<Map<String, Object>> handleAdClick(@RequestBody Map<String, Object> request) {
        try {
            // 获取请求参数
            String clientId = (String) request.get("client_id");
            String userId = (String) request.get("user_id");
            String adId = (String) request.get("ad_id");
            String tag = (String) request.get("tag");

            // 验证请求参数
            if (clientId == null || adId == null || userId == null || tag == null) {
                return ResponseEntity.status(500).body(Map.of("Error", "Invalid"));
            }

            // 获取用户已申请的广告
            List<Ad> reviewedAds = advertisingService.getAllReviewedAdsWithUserAppliedStatus(userId);
            List<String> appliedAdIds = advertisingApplicationService.selectAdIdsByUserId(userId);
            List<Ad> appliedAds = reviewedAds.stream()
                    .filter(ad -> appliedAdIds.contains(ad.getId()))
                    .toList();

            // 记录广告点击
            recordAdClick(userId, clientId, adId, tag);

            // 获取用户兴趣标签
            String userInterestTag = adClickService.getAdClickInfo(userId, clientId).getNewInterestTags();

            // 匹配用户兴趣标签的广告
            List<Map<String, Object>> matchedAds = new ArrayList<>(appliedAds.stream()
                    .filter(ad -> ad.getTags().contains(userInterestTag))
                    .map(this::createAdWithUrl)
                    .toList());

            // 保留一些不相关的广告
            List<Map<String, Object>> otherAds = appliedAds.stream()
                    .filter(ad -> !ad.getTags().contains(userInterestTag))
                    .map(this::createAdWithUrl)
                    .limit(3)
                    .toList();

            matchedAds.addAll(otherAds);

            // 构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("ad_urls", matchedAds);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 记录广告点击信息。
     *
     * @param userId   用户ID
     * @param clientId 客户端ID
     * @param adId     广告ID
     * @param tag      标签
     */
    private void recordAdClick(String userId, String clientId, String adId, String tag) {
        AdClick adClick = new AdClick();
        adClick.setUserId(userId);
        adClick.setClientId(clientId);
        adClick.setAdId(adId);
        adClick.setClickTime(new Timestamp(System.currentTimeMillis()));
        adClick.setNewInterestTags(tag);
        adClickService.updateAdClick(adClick);
    }

    /**
     * 创建包含广告 URL 的广告信息。
     *
     * @param ad 广告对象
     * @return 包含广告 URL 的 Map
     */
    private Map<String, Object> createAdWithUrl(Ad ad) {
        Map<String, Object> adWithUrl = new HashMap<>();
        adWithUrl.put("adId", ad.getId());
        adWithUrl.put("adUrl", AD_URL_PREFIX + ad.getId());
        return adWithUrl;
    }
}