package com.asaki0019.advertising.serviceMeta.data;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AdChartData {
    private String name;
    private int value;
    private int distributed;

    public AdChartData(String name, int value, int distributed) {
        this.name = name;
        this.value = value;
        this.distributed = distributed;
    }
}