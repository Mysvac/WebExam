package com.asaki0019.advertising.serviceMeta.res;

import com.asaki0019.advertising.serviceMeta.data.AdChartData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdChartDataResponse {
    private int code;
    private String message;
    private List<AdChartData> data;

    public AdChartDataResponse(int code, String message, List<AdChartData> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}