package com.power.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.power.analysis.common.PageResult;
import com.power.analysis.entity.PowerData;
import com.power.analysis.mapper.PowerDataMapper;
import com.power.analysis.service.PowerDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PowerDataServiceImpl implements PowerDataService {

    private final PowerDataMapper powerDataMapper;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void save(PowerData powerData) {
        if (powerData.getCollectTime() == null) {
            powerData.setCollectTime(LocalDateTime.now());
        }
        powerDataMapper.insert(powerData);
    }

    @Override
    public void saveBatch(List<PowerData> dataList) {
        dataList.forEach(d -> {
            if (d.getCollectTime() == null) {
                d.setCollectTime(LocalDateTime.now());
            }
            powerDataMapper.insert(d);
        });
    }

    @Override
    public PageResult<PowerData> pageQuery(Long deviceId, String startTime, String endTime, int page, int size) {
        LambdaQueryWrapper<PowerData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(deviceId != null, PowerData::getDeviceId, deviceId);
        if (StringUtils.hasText(startTime)) {
            wrapper.ge(PowerData::getCollectTime, LocalDateTime.parse(startTime, FMT));
        }
        if (StringUtils.hasText(endTime)) {
            wrapper.le(PowerData::getCollectTime, LocalDateTime.parse(endTime, FMT));
        }
        wrapper.orderByDesc(PowerData::getCollectTime);

        Page<PowerData> result = powerDataMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(result.getTotal(), result.getCurrent(), result.getSize(), result.getRecords());
    }

    @Override
    public PowerData getById(Long id) {
        return powerDataMapper.selectById(id);
    }
}
