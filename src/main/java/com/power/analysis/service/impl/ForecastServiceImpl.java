package com.power.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.power.analysis.dto.ForecastDTO;
import com.power.analysis.dto.StatisticDTO;
import com.power.analysis.entity.PowerData;
import com.power.analysis.mapper.PowerDataMapper;
import com.power.analysis.service.ForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForecastServiceImpl implements ForecastService {

    private final PowerDataMapper powerDataMapper;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public ForecastDTO forecast(Long deviceId, LocalDateTime targetDate) {
        // 取最近7天同小时历史数据做简单均值预测
        LocalDateTime sevenDaysAgo = targetDate.minusDays(7);

        LambdaQueryWrapper<PowerData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(deviceId != null, PowerData::getDeviceId, deviceId);
        wrapper.between(PowerData::getCollectTime, sevenDaysAgo, targetDate);
        List<PowerData> historyData = powerDataMapper.selectList(wrapper);

        ForecastDTO dto = new ForecastDTO();
        dto.setForecastDate(targetDate.format(DATE_FMT));

        if (historyData.isEmpty()) {
            dto.setMaxLoad(BigDecimal.ZERO);
            dto.setMinLoad(BigDecimal.ZERO);
            dto.setAvgLoad(BigDecimal.ZERO);
            dto.setForecastEnergy(BigDecimal.ZERO);
            dto.setHourlyLoads(new ArrayList<>());
            return dto;
        }

        // 按小时分组求均值
        List<ForecastDTO.HourlyLoad> hourlyLoads = new ArrayList<>();
        for (int h = 0; h < 24; h++) {
            final int hour = h;
            List<PowerData> hourData = historyData.stream()
                    .filter(d -> d.getCollectTime().getHour() == hour)
                    .collect(Collectors.toList());

            ForecastDTO.HourlyLoad hl = new ForecastDTO.HourlyLoad();
            hl.setHour(hour);
            if (!hourData.isEmpty()) {
                BigDecimal avg = hourData.stream()
                        .map(PowerData::getPower)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(hourData.size()), 2, RoundingMode.HALF_UP);
                hl.setLoad(avg);
            } else {
                hl.setLoad(BigDecimal.ZERO);
            }
            hourlyLoads.add(hl);
        }
        dto.setHourlyLoads(hourlyLoads);

        // 统计预测值
        BigDecimal maxLoad = hourlyLoads.stream()
                .map(ForecastDTO.HourlyLoad::getLoad)
                .reduce(BigDecimal::max).orElse(BigDecimal.ZERO);
        BigDecimal minLoad = hourlyLoads.stream()
                .map(ForecastDTO.HourlyLoad::getLoad)
                .reduce(BigDecimal::min).orElse(BigDecimal.ZERO);
        BigDecimal avgLoad = hourlyLoads.stream()
                .map(ForecastDTO.HourlyLoad::getLoad)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(24), 2, RoundingMode.HALF_UP);
        BigDecimal forecastEnergy = avgLoad.multiply(BigDecimal.valueOf(24));

        dto.setMaxLoad(maxLoad);
        dto.setMinLoad(minLoad);
        dto.setAvgLoad(avgLoad);
        dto.setForecastEnergy(forecastEnergy.setScale(2, RoundingMode.HALF_UP));

        return dto;
    }
}
