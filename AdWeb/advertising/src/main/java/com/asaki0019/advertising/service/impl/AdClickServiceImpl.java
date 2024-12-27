package com.asaki0019.advertising.service.impl;

import com.asaki0019.advertising.mapper.AdClickMapper;
import com.asaki0019.advertising.model.AdClick;
import com.asaki0019.advertising.service.AdClickService;
import com.asaki0019.advertising.utils.Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdClickServiceImpl implements AdClickService {
    private final AdClickMapper adClickMapper;

    public AdClickServiceImpl(AdClickMapper adClickMapper) {
        this.adClickMapper = adClickMapper;
    }

    /**
     * 更新广告点击记录
     * 如果数据库中不存在对应的广告点击记录，则插入新记录
     * 如果存在，则更新记录中的用户ID、广告ID、新兴趣标签和点击时间
     *
     * @param adClick 广告点击对象，包含需要更新的数据
     */
    @Override
    @Transactional
    public void updateAdClick(AdClick adClick) {
        try {
            // 检查数据库中是否已存在该客户端ID的广告点击记录
            AdClick existingAdClick = adClickMapper.selectOne(
                    new QueryWrapper<AdClick>().eq("client_id", adClick.getClientId())
            );
            if (existingAdClick == null) {
                // 如果不存在，则插入新记录
                adClickMapper.insert(adClick);
            } else {
                // 如果存在，则更新记录
                existingAdClick.setUserId(adClick.getUserId());
                existingAdClick.setAdId(adClick.getAdId());
                existingAdClick.setNewInterestTags(adClick.getNewInterestTags());
                existingAdClick.setClickTime(adClick.getClickTime());
                adClickMapper.updateById(existingAdClick);
            }
        } catch (RuntimeException e) {
            // 记录更新广告点击记录时发生的错误
            Utils.logError("更新广告点击记录失败: " + adClick.toString(), e, "AdClickServiceImpl.updateAdClick");
            throw e;
        }
    }

    @Override
    public AdClick getAdClickInfo(String userId, String clientId) {
        return adClickMapper.selectOne(
                new QueryWrapper<AdClick>()
                        .eq("client_id", clientId)
                        .eq("user_id", userId)
        );
    }
}