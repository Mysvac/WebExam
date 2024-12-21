package com.asaki0019.advertising.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("ad_clicks")
public class AdClick {
    @TableId
    private Long id;
    private String userId;
    private String clientId;
    private String adId;
    private Timestamp clickTime;
    private String newInterestTags;
}