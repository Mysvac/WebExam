package com.asaki0019.advertising.controller;

import com.asaki0019.advertising.model.User;
import com.asaki0019.advertising.service.AdvertisingService;
import com.asaki0019.advertising.serviceMeta.data.AdChartData;
import com.asaki0019.advertising.serviceMeta.res.AdChartDataResponse;
import com.asaki0019.advertising.type.AdTagEnum;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")
public class AdvertisingChartDataController {

    private final AdvertisingService advertisingService;
    /**
     * 构造函数，使用构造函数注入依赖。
     *
     * @param advertisingService 广告服务
     */
    public AdvertisingChartDataController(AdvertisingService advertisingService) {
        this.advertisingService = advertisingService;
    }

    /**
     * 获取广告图表数据。
     *
     * @param session HTTP 会话对象
     * @return 包含广告图表数据的响应实体
     */
    @PostMapping("/advertising-chart-data")
    public ResponseEntity<AdChartDataResponse> getAdvertisingChartData(HttpSession session) {
        try {
            // 获取当前用户
            User nowUser = (User) session.getAttribute("user");
            if (nowUser == null) {
                return ResponseEntity.status(401).body(new AdChartDataResponse(401, "用户不存在", null));
            }

            // 获取每个标签的广告数量和分发数量
            List<AdChartData> adDataList = collectAdChartData();

            // 构建响应
            var response = new AdChartDataResponse(200, "获取广告表格数据成功", adDataList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AdChartDataResponse(500, "获取广告表格数据失败: " + e.getMessage(), null));
        }
    }

    /**
     * 收集每个标签的广告数量和分发数量。
     *
     * @return 包含广告数据的列表
     */
    private List<AdChartData> collectAdChartData() {
        List<AdChartData> adDataList = new ArrayList<>();
        for (AdTagEnum tag : AdTagEnum.values()) {
            Integer totalAds = advertisingService.getAdCountByTag(tag.getTagName());
            Integer distributedCount = advertisingService.getDistributedAdCountByTag(tag.getTagName());

            // 处理空值
            totalAds = totalAds != null ? totalAds : 0;
            distributedCount = distributedCount != null ? distributedCount : 0;

            adDataList.add(new AdChartData(tag.getTagName(), totalAds, distributedCount));
        }
        return adDataList;
    }
}