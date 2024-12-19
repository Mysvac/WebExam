package com.asaki0019.advertising.serviceMeta.res;

import com.asaki0019.advertising.serviceMeta.data.AdMetaData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdListMetaResponse {
    private int code;
    private String message;
    private List<AdMetaData> data; // 修改为 List<AdMetaData>

    public AdListMetaResponse(int code, String message, List<AdMetaData> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}