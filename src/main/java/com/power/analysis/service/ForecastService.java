package com.power.analysis.service;

import com.power.analysis.dto.ForecastDTO;
import java.time.LocalDateTime;

public interface ForecastService {

    /** 基于历史数据的负荷预测 */
    ForecastDTO forecast(Long deviceId, LocalDateTime targetDate);
}
