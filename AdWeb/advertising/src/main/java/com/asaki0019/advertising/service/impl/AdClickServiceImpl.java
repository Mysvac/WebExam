package com.asaki0019.advertising.service.impl;

import com.asaki0019.advertising.mapper.AdClickMapper;
import com.asaki0019.advertising.model.AdClick;
import com.asaki0019.advertising.service.AdClickService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdClickServiceImpl implements AdClickService {
    @Autowired
    private AdClickMapper adClickMapper;

    @Override
    public void updateAdClick(AdClick adClick) {
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