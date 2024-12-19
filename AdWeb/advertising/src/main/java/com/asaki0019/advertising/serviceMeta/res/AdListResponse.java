package com.asaki0019.advertising.serviceMeta.res;

import com.asaki0019.advertising.serviceMeta.data.AdData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdListResponse {
    private int code;
    private String message;
    private List<AdData> data; // 修改为 List<AdData>

    public AdListResponse(int code, String message, List<AdData> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}