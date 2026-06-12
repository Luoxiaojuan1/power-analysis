package com.power.analysis.service;

import com.power.analysis.dto.StatisticDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {

    /** 按日统计用电量 */
    List<StatisticDTO> dailyEnergy(Long deviceId, LocalDateTime startTime, LocalDateTime endTime);

    /** 按月统计用电量 */
    List<StatisticDTO> monthlyEnergy(Long deviceId, LocalDateTime startTime, LocalDateTime endTime);

    /** 日负荷曲线 */
    List<StatisticDTO> dailyLoad(Long deviceId, LocalDateTime date);
}
