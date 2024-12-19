package com.asaki0019.advertising.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdStatusEnum {
    UNDER_REVIEW(1, "审核中"),
    UNPUBLISHED(2, "未发布"),
    PUBLISHED(3, "已发布");

    private final Integer id; // 状态ID
    private final String statusName; // 状态名称

    // 根据 ID 获取枚举
    public static AdStatusEnum getById(Integer id) {
        for (AdStatusEnum status : values()) {
            if (status.getId().equals(id)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid AdStatusEnum ID: " + id);
    }

    // 根据名称获取枚举
    public static AdStatusEnum getByName(String statusName) {
        for (AdStatusEnum status : values()) {
            if (status.getStatusName().equals(statusName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid AdStatusEnum name: " + statusName);
    }
}
