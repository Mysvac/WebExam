package com.asaki0019.advertising.controller;

import com.asaki0019.advertising.model.Ad;
import com.asaki0019.advertising.model.AdApplication;
import com.asaki0019.advertising.model.UploadedFile;
import com.asaki0019.advertising.model.User;
import com.asaki0019.advertising.service.AdvertisingApplicationService;
import com.asaki0019.advertising.service.AdvertisingService;
import com.asaki0019.advertising.service.UploadedFileService;
import com.asaki0019.advertising.serviceMeta.data.ShowAdData;
import com.asaki0019.advertising.serviceMeta.res.ShowAdResponse;
import com.asaki0019.advertising.type.AdStatusEnum;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 处理广告操作请求的控制器。
 */
@RestController
@RequestMapping("/api")
public class AdvertisingOperationController {

    private final AdvertisingService advertisingService;
    private final AdvertisingApplicationService advertisingApplicationService;
    private final UploadedFileService uploadedFileService;

    /**
     * 构造函数，使用构造函数注入依赖。
     *
     * @param advertisingService            广告服务
     * @param advertisingApplicationService 广告申请服务
     * @param uploadedFileService           文件上传服务
     */
    public AdvertisingOperationController(AdvertisingService advertisingService,
                                          AdvertisingApplicationService advertisingApplicationService,
                                          UploadedFileService uploadedFileService) {
        this.advertisingService = advertisingService;
        this.advertisingApplicationService = advertisingApplicationService;
        this.uploadedFileService = uploadedFileService;
    }

    /**
     * 审核通过广告。
     *
     * @param body    包含广告 ID 的请求体
     * @param session HTTP 会话对象
     * @return 包含审核结果的响应实体
     */
    @PostMapping("/advertising-review-data-ok")
    public ResponseEntity<Map<String, Object>> approveAd(@RequestBody Map<String, String> body, HttpSession session) {
        try {
            if (checkUser(session)) {
                return ResponseEntity.status(401).body(Map.of("code", 401, "message", "用户未登录"));
            }
            var id = body.get("id");
            Ad ad = advertisingService.reviewAd(id, AdStatusEnum.PUBLISHED.getId());
            return ResponseEntity.ok(Map.of("code", 200, "message", "审核广告数据成功", "id", ad.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("code", 500, "message", e.getMessage()));
        }
    }

    /**
     * 申请广告。
     *
     * @param body    包含广告 ID 的请求体
     * @param session HTTP 会话对象
     * @return 包含申请结果的响应实体
     */
    @PostMapping("/request-advertising")
    public ResponseEntity<Map<String, Object>> requestAd(@RequestBody Map<String, String> body, HttpSession session) {
        try {
            if (checkUser(session)) {
                return ResponseEntity.status(401).body(Map.of("code", 401, "message", "用户未登录"));
            }
            var id = body.get("id");
            var user = (User) session.getAttribute("user");
            AdApplication application = advertisingApplicationService.applyForAd(id, user.getId());
            return ResponseEntity.ok(Map.of("code", 200, "message", "申请广告成功", "id", application.getApplicantId()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("code", 500, "message", e.getMessage()));
        }
    }

    /**
     * 取消申请广告。
     *
     * @param body    包含广告 ID 的请求体
     * @param session HTTP 会话对象
     * @return 包含取消申请结果的响应实体
     */
    @PostMapping("/unRequest-advertising")
    public ResponseEntity<Map<String, Object>> unRequestAd(@RequestBody Map<String, String> body, HttpSession session) {
        try {
            if (checkUser(session)) {
                return ResponseEntity.status(401).body(Map.of("code", 401, "message", "用户未登录"));
            }
            var id = body.get("id");
            var user = (User) session.getAttribute("user");
            advertisingApplicationService.unApplyForAd(id, user.getId());
            return ResponseEntity.ok(Map.of("code", 200, "message", "解除广告成功"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("code", 500, "message", e.getMessage()));
        }
    }

    /**
     * 删除广告。
     *
     * @param body    包含广告 ID 的请求体
     * @param session HTTP 会话对象
     * @return 包含删除结果的响应实体
     */
    @PostMapping("/delete-advertising")
    public ResponseEntity<Map<String, Object>> deleteAd(@RequestBody Map<String, String> body, HttpSession session) {
        try {
            if (checkUser(session)) {
                return ResponseEntity.status(401).body(Map.of("code", 401, "message", "用户未登录"));
            }
            var adId = body.get("id");
            var user = (User) session.getAttribute("user");
            Ad ad = advertisingService.getAdByAdId(adId);
            String fileId = ad.getFileId();
            if (fileId == null) {
                return ResponseEntity.status(400).body(Map.of("code", 400, "message", "错误的资源链接"));
            }
            uploadedFileService.deleteFileFromFileSystem(fileId);
            advertisingService.deleteAd(adId, user.getId());
            return ResponseEntity.ok(Map.of("code", 200, "message", "删除广告成功"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("code", 500, "message", e.getMessage()));
        }
    }

    /**
     * 展示广告详情。
     *
     * @param adId 广告 ID
     * @return 包含广告详情的响应实体
     */
    @GetMapping("/show-ad")
    public ResponseEntity<ShowAdResponse> showAd(@RequestParam("adId") String adId) {
        try {
            if (adId == null) {
                return ResponseEntity.status(400).body(new ShowAdResponse(400, "广告 ID 为空", null));
            }
            Ad ad = advertisingService.getAdByAdId(adId);
            UploadedFile file = uploadedFileService.getUploadedFileById(ad.getFileId());
            ShowAdResponse adResponse = new ShowAdResponse(
                    200,
                    "访问成功",
                    new ShowAdData(
                            ad.getTitle(),
                            ad.getTags(),
                            ad.getDescription(),
                            file.getFileType(),
                            file.getFileUrl()
                    )
            );
            return ResponseEntity.ok(adResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ShowAdResponse(500, "访问失败: " + e.getMessage(), null));
        }
    }

    /**
     * 检查用户是否登录。
     *
     * @param session HTTP 会话对象
     * @return 如果用户未登录返回 true，否则返回 false
     */
    private boolean checkUser(HttpSession session) {
        return session.getAttribute("user") == null;
    }
}