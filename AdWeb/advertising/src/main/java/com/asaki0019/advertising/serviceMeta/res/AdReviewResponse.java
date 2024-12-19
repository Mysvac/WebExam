package com.asaki0019.advertising.serviceMeta.res;

import com.asaki0019.advertising.serviceMeta.data.AdReviewData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdReviewResponse {
    private int code;
    private String message;
    private List<AdReviewData> data;

    // 构造函数
    public AdReviewResponse(int code, String message, List<AdReviewData> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}