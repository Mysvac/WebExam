package com.asaki0019.advertising.serviceMeta.res;

import com.asaki0019.advertising.serviceMeta.data.ShowAdData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowAdResponse {
    private int code;
    private String message;
    private ShowAdData data;

    public ShowAdResponse(int code, String message, ShowAdData data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}