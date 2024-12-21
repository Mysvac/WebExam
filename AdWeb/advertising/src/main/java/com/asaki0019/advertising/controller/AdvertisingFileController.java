package com.asaki0019.advertising.controller;

import com.asaki0019.advertising.model.Ad;
import com.asaki0019.advertising.model.UploadedFile;
import com.asaki0019.advertising.model.User;
import com.asaki0019.advertising.service.AdvertisingService;
import com.asaki0019.advertising.service.UploadedFileService;
import com.asaki0019.advertising.serviceMeta.data.AdData;
import com.asaki0019.advertising.serviceMeta.data.UploadData;
import com.asaki0019.advertising.serviceMeta.req.AdRequest;
import com.asaki0019.advertising.serviceMeta.res.AdResponse;
import com.asaki0019.advertising.serviceMeta.res.UploadResponse;
import com.asaki0019.advertising.type.AdStatusEnum;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 处理广告文件上传和广告创建请求的控制器。
 */
@RestController
@RequestMapping("/api")
public class AdvertisingFileController {

    private final UploadedFileService uploadedFileService;
    private final AdvertisingService advertisingService;

    /**
     * 构造函数，使用构造函数注入依赖。
     *
     * @param uploadedFileService 文件上传服务
     * @param advertisingService  广告服务
     */
    public AdvertisingFileController(UploadedFileService uploadedFileService, AdvertisingService advertisingService) {
        this.uploadedFileService = uploadedFileService;
        this.advertisingService = advertisingService;
    }

    /**
     * 创建广告。
     *
     * @param adRequest 广告请求对象
     * @param session   HTTP 会话对象
     * @return 包含广告创建结果的响应实体
     */
    @PostMapping("/upload-advertising")
    public ResponseEntity<AdResponse> createAd(@RequestBody AdRequest adRequest, HttpSession session) {
        try {
            User nowUser = (User) session.getAttribute("user");
            if (nowUser == null) {
                return ResponseEntity.status(401).body(new AdResponse(401, "用户不存在", null));
            }

            // 解析请求体中的数据
            String title = adRequest.getTitle();
            String description = adRequest.getDescription();
            String distributor = adRequest.getDistributor();
            BigDecimal cost = adRequest.getCost();
            String fileId = adRequest.getFileId(); // 从嵌套对象中提取 fileId

            // 创建广告对象
            Ad ad = new Ad();
            ad.setAdvertiserName(distributor);
            ad.setTitle(title);
            ad.setDescription(description);
            ad.setPrice(cost);
            ad.setTags(adRequest.getTag());
            ad.setDistributed(0);

            // 调用广告服务创建广告
            Ad createdAd = advertisingService.createAd(ad, nowUser.getId(), fileId);

            // 构建响应
            AdResponse response = new AdResponse(200, "广告上传成功", new AdData(
                    createdAd.getId(),
                    createdAd.getTitle(),
                    AdStatusEnum.UNDER_REVIEW.getStatusName(), // 初始状态为“审核中”
                    createdAd.getTags(),
                    createdAd.getDescription(),
                    createdAd.getAdvertiserName(),
                    cost,
                    fileId
            ));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AdResponse(500, "广告上传失败: " + e.getMessage(), null));
        }
    }

    /**
     * 上传文件。
     *
     * @param file 上传的文件
     * @return 包含文件上传结果的响应实体
     */
    @PostMapping("/advertising-file-upload")
    public ResponseEntity<UploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(new UploadResponse(400, "文件不能为空", null));
        }

        try {
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String fileName = UUID.randomUUID() + fileExtension;

            // 获取项目的静态资源目录路径
            String uploadDir = ResourceUtils.getURL("classpath:").getPath() + "static/uploads/";

            // 处理文件上传
            UploadResponse response = handleFileUpload(file, uploadDir, fileName);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new UploadResponse(500, "文件上传失败: " + e.getMessage(), null));
        }
    }

    /**
     * 删除文件。
     *
     * @param fileId 文件 ID
     * @return 包含文件删除结果的响应实体
     */
    @DeleteMapping("/delete-file/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileId) {
        try {
            // 根据 fileId 查询文件记录
            UploadedFile uploadedFile = uploadedFileService.getUploadedFileById(fileId);
            if (uploadedFile == null) {
                return ResponseEntity.status(404).body("文件不存在");
            }

            // 获取文件的 URL
            String fileUrl = uploadedFile.getFileUrl();

            // 删除文件系统中的文件
            uploadedFileService.deleteFileFromFileSystem(fileUrl);

            // 删除数据库中的文件记录
            uploadedFileService.deleteUploadedFileById(fileId);

            return ResponseEntity.ok("文件删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("文件删除失败: " + e.getMessage());
        }
    }

    /**
     * 处理文件上传逻辑。
     *
     * @param file      上传的文件
     * @param uploadDir 上传目录
     * @param fileName  文件名
     * @return 包含文件上传结果的响应对象
     * @throws IOException 如果文件操作失败
     */
    private UploadResponse handleFileUpload(MultipartFile file, String uploadDir, String fileName) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("创建目录失败");
        }

        // 使用流保存文件
        File destFile = new File(dir, fileName);
        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream = new FileOutputStream(destFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        // 构建响应
        String fileUrl = "http://localhost:8080/uploads/" + fileName; // 文件的访问 URL

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFileName(fileName);
        uploadedFile.setFileType(file.getContentType());
        uploadedFile.setFileUrl(fileUrl);
        uploadedFile.setUploadTime(LocalDateTime.now());

        var saveFile = uploadedFileService.saveUploadedFile(uploadedFile);
        return new UploadResponse(200, "文件上传成功", new UploadData(fileName, file.getContentType(), saveFile.getId()));
    }
}