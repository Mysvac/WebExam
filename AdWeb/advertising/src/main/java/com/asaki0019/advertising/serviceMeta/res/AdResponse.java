package com.asaki0019.advertising.serviceMeta.res;
import com.asaki0019.advertising.serviceMeta.data.AdData;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AdResponse {
    private int code;
    private String message;
    private AdData data;

    public AdResponse(int code, String message, AdData data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}