package com.asaki0019.advertising.controller;

import com.asaki0019.advertising.model.Ad;
import com.asaki0019.advertising.model.AdApplication;
import com.asaki0019.advertising.model.User;
import com.asaki0019.advertising.service.AdvertisingApplicationService;
import com.asaki0019.advertising.service.AdvertisingService;
import com.asaki0019.advertising.service.UploadedFileService;
import com.asaki0019.advertising.type.AdStatusEnum;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AdvertisingHandle {
    @Autowired
    private AdvertisingService advertisingService;

    @Autowired
    private AdvertisingApplicationService advertisingApplicationService;

    @Autowired
    private UploadedFileService uploadedFileService;
    @PostMapping("/advertising-review-data-ok")
    public ResponseEntity<Map<String, Object>> approveAd(@RequestBody Map<String, String> body,
                                                         HttpSession session) {
        try {
            if (checkUser(session)) {
                return ResponseEntity.status(401).body(Map.of("code", 401, "message", "用户未登录"));
            }
            var id = body.get("id");
            Ad ad = advertisingService.reviewAd(id, AdStatusEnum.PUBLISHED.getId());
            return ResponseEntity.ok(Map.of("code", 200, "message", "审核广告数据成功", "id", ad.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("code", 401, "message", e.getMessage()));
        }
    }

    private Boolean checkUser(HttpSession session) {
        return (session.getAttribute("user") == null);
    }

    @PostMapping("/request-advertising")
    public ResponseEntity<Map<String, Object>> requestAd(@RequestBody Map<String, String> body,
                                                         HttpSession session) {
        try {
            if (checkUser(session)) {
                return ResponseEntity.status(401).body(Map.of("code", 401, "message", "用户未登录"));
            }
            var id = body.get("id");
            var user = (User)session.getAttribute("user");
            AdApplication application = advertisingApplicationService.applyForAd(id,user.getId());
            return ResponseEntity.ok(Map.of("code", 200, "message", "申请广告成功", "id", application.getApplicantId()));
        }catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("code", 401, "message", e.getMessage()));
        }
    }

    @PostMapping("/unRequest-advertising")
    public ResponseEntity<Map<String, Object>> unRequestAd(@RequestBody Map<String, String> body,
                                                         HttpSession session) {
        try {
            if (checkUser(session)) {
                return ResponseEntity.status(401).body(Map.of("code", 401, "message", "用户未登录"));
            }
            var id = body.get("id");
            var user = (User)session.getAttribute("user");
            AdApplication application = advertisingApplicationService.unApplyForAd(id,user.getId());
            return ResponseEntity.ok(Map.of("code", 200, "message", "解除广告成功"));
        }catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("code", 401, "message", e.getMessage()));
        }
    }

    @PostMapping("/delete-advertising")
    public ResponseEntity<Map<String, Object>> deleteAd(@RequestBody Map<String, String> body,
                                                           HttpSession session) {
        try {
            if (checkUser(session)) {
                return ResponseEntity.status(401).body(Map.of("code", 401, "message", "用户未登录"));
            }
            var adId = body.get("id");
            var user = (User)session.getAttribute("user");
            Ad ad = advertisingService.getAdByAdId(adId);
            String filedId = ad.getFileId();
            if(filedId == null){
                return ResponseEntity.status(401).body(Map.of("code", 401, "message", "错误的资源链接"));
            }
            uploadedFileService.deleteFileFromFileSystem(filedId);
            advertisingService.deleteAd(adId,user.getId());
            return ResponseEntity.ok(Map.of("code", 200, "message", "解除广告成功"));
        }catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("code", 401, "message", e.getMessage()));
        }
    }
}
