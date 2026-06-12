package com.power.analysis.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ForecastDTO {

    /** 预测日期 */
    private String forecastDate;

    /** 预测最大负荷(kW) */
    private BigDecimal maxLoad;

    /** 预测最小负荷(kW) */
    private BigDecimal minLoad;

    /** 预测平均负荷(kW) */
    private BigDecimal avgLoad;

    /** 预测用电量(kWh) */
    private BigDecimal forecastEnergy;

    /** 负荷曲线(24小时) */
    private List<HourlyLoad> hourlyLoads;

    @Data
    public static class HourlyLoad {
        private int hour;
        private BigDecimal load;
    }
}
