package com.power.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.power.analysis.dto.ForecastDTO;
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

        // 加权平均预测: 最近3天权重0.5, 4-7天权重0.5
        LocalDateTime threeDaysAgo = targetDate.minusDays(3);
        List<PowerData> recentData = historyData.stream()
                .filter(d -> d.getCollectTime().isAfter(threeDaysAgo))
                .collect(Collectors.toList());
        List<PowerData> olderData = historyData.stream()
                .filter(d -> !d.getCollectTime().isAfter(threeDaysAgo))
                .collect(Collectors.toList());

        List<ForecastDTO.HourlyLoad> hourlyLoads = new ArrayList<>();
        for (int h = 0; h < 24; h++) {
            final int hour = h;
            ForecastDTO.HourlyLoad hl = new ForecastDTO.HourlyLoad();
            hl.setHour(hour);

            BigDecimal recentAvg = calcHourAvg(recentData, hour);
            BigDecimal olderAvg = calcHourAvg(olderData, hour);
            BigDecimal trendFactor = BigDecimal.ONE;

            // 如果近期和远期都有数据，计算趋势因子
            if (recentAvg.compareTo(BigDecimal.ZERO) > 0 && olderAvg.compareTo(BigDecimal.ZERO) > 0) {
                trendFactor = recentAvg.divide(olderAvg, 4, RoundingMode.HALF_UP);
                // 限制趋势因子在0.8-1.2之间，避免异常波动
                if (trendFactor.compareTo(new BigDecimal("1.2")) > 0) trendFactor = new BigDecimal("1.2");
                if (trendFactor.compareTo(new BigDecimal("0.8")) < 0) trendFactor = new BigDecimal("0.8");
            }

            if (recentAvg.compareTo(BigDecimal.ZERO) > 0 && olderAvg.compareTo(BigDecimal.ZERO) > 0) {
                // 加权: 近期0.6 * 近期均值 + 远期0.4 * 远期均值 * 趋势因子
                BigDecimal forecast = recentAvg.multiply(new BigDecimal("0.6"))
                        .add(olderAvg.multiply(new BigDecimal("0.4")).multiply(trendFactor))
                        .setScale(2, RoundingMode.HALF_UP);
                hl.setLoad(forecast);
            } else if (recentAvg.compareTo(BigDecimal.ZERO) > 0) {
                hl.setLoad(recentAvg);
            } else if (olderAvg.compareTo(BigDecimal.ZERO) > 0) {
                hl.setLoad(olderAvg);
            } else {
                // 无数据的小时，使用前后有数据小时的线性插值
                hl.setLoad(BigDecimal.ZERO);
            }
            hourlyLoads.add(hl);
        }

        // 对零值小时做线性插值填充
        fillZeroWithInterpolation(hourlyLoads);

        dto.setHourlyLoads(hourlyLoads);

        BigDecimal maxLoad = hourlyLoads.stream()
                .map(ForecastDTO.HourlyLoad::getLoad)
                .reduce(BigDecimal::max).orElse(BigDecimal.ZERO);
        BigDecimal minLoad = hourlyLoads.stream()
                .map(ForecastDTO.HourlyLoad::getLoad)
                .filter(l -> l.compareTo(BigDecimal.ZERO) > 0)
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

    private BigDecimal calcHourAvg(List<PowerData> data, int hour) {
        List<PowerData> hourData = data.stream()
                .filter(d -> d.getCollectTime().getHour() == hour)
                .collect(Collectors.toList());
        if (hourData.isEmpty()) return BigDecimal.ZERO;
        return hourData.stream()
                .map(PowerData::getPower)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(hourData.size()), 2, RoundingMode.HALF_UP);
    }

    private void fillZeroWithInterpolation(List<ForecastDTO.HourlyLoad> hourlyLoads) {
        // 找到第一个和最后一个非零值
        int first = -1, last = -1;
        for (int i = 0; i < hourlyLoads.size(); i++) {
            if (hourlyLoads.get(i).getLoad().compareTo(BigDecimal.ZERO) > 0) {
                if (first == -1) first = i;
                last = i;
            }
        }
        if (first == -1) return;

        // 在非零区间内做线性插值
        int prev = first;
        for (int i = first + 1; i <= last; i++) {
            if (hourlyLoads.get(i).getLoad().compareTo(BigDecimal.ZERO) > 0) {
                // 对prev和i之间的零值做插值
                if (i - prev > 1) {
                    BigDecimal start = hourlyLoads.get(prev).getLoad();
                    BigDecimal end = hourlyLoads.get(i).getLoad();
                    for (int j = prev + 1; j < i; j++) {
                        BigDecimal ratio = BigDecimal.valueOf(j - prev).divide(BigDecimal.valueOf(i - prev), 4, RoundingMode.HALF_UP);
                        BigDecimal val = start.add(end.subtract(start).multiply(ratio)).setScale(2, RoundingMode.HALF_UP);
                        if (val.compareTo(BigDecimal.ZERO) < 0) val = BigDecimal.ZERO;
                        hourlyLoads.get(j).setLoad(val);
                    }
                }
                prev = i;
            }
        }

        // 首尾边界：用最近的非零值填充
        for (int i = 0; i < first; i++) {
            hourlyLoads.get(i).setLoad(hourlyLoads.get(first).getLoad()
                    .multiply(BigDecimal.valueOf(0.3 + 0.7 * i / first)).setScale(2, RoundingMode.HALF_UP));
        }
        for (int i = last + 1; i < 24; i++) {
            hourlyLoads.get(i).setLoad(hourlyLoads.get(last).getLoad()
                    .multiply(BigDecimal.valueOf(0.3 + 0.7 * (24 - i) / (24 - last))).setScale(2, RoundingMode.HALF_UP));
        }
    }
}
