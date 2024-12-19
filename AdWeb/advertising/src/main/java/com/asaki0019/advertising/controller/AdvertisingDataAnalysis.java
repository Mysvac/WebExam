package com.asaki0019.advertising.controller;

import com.asaki0019.advertising.model.User;
import com.asaki0019.advertising.service.AdvertisingService;
import com.asaki0019.advertising.serviceMeta.data.AdChartData;
import com.asaki0019.advertising.serviceMeta.res.AdChartDataResponse;
import com.asaki0019.advertising.type.AdTagEnum;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")
public class AdvertisingDataAnalysis {

    @Autowired
    private AdvertisingService advertisingService;

    @PostMapping("/advertising-chart-data")
    public ResponseEntity<AdChartDataResponse> getAdvertisingChartData(HttpSession session) {
        try {
            User nowUser = (User) session.getAttribute("user");
            if (nowUser == null) {
                return ResponseEntity.status(401).body(new AdChartDataResponse(401, "用户不存在", null));
            }
            // 获取每个标签的广告数量和分发数量
            List<AdChartData> adDataList = new ArrayList<>();
            for (AdTagEnum tag : AdTagEnum.values()) {
                Integer totalAds = advertisingService.getAdCountByTag(tag.getTagName());
                Integer distributedCount = advertisingService.getDistributedAdCountByTag(tag.getTagName());
                if (distributedCount == null) {
                    distributedCount = 0;
                }
                if (totalAds == null) {
                    totalAds = 0;
                }

                adDataList.add(new AdChartData(tag.getTagName(), totalAds, distributedCount));
            }
            var response = new AdChartDataResponse(200, "获取广告表格数据成功", adDataList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AdChartDataResponse(500, "获取广告表格数据失败: " + e.getMessage(), null));
        }
    }
}