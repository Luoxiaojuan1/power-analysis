package com.power.analysis.service.impl;

import com.power.analysis.dto.StatisticDTO;
import com.power.analysis.mapper.PowerDataMapper;
import com.power.analysis.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final PowerDataMapper powerDataMapper;

    @Override
    public List<StatisticDTO> dailyEnergy(Long deviceId, LocalDateTime startTime, LocalDateTime endTime) {
        return powerDataMapper.statisticDailyEnergy(deviceId, startTime, endTime);
    }

    @Override
    public List<StatisticDTO> monthlyEnergy(Long deviceId, LocalDateTime startTime, LocalDateTime endTime) {
        return powerDataMapper.statisticMonthlyEnergy(deviceId, startTime, endTime);
    }

    @Override
    public List<StatisticDTO> dailyLoad(Long deviceId, LocalDateTime date) {
        return powerDataMapper.statisticDailyLoad(deviceId, date);
    }
}
