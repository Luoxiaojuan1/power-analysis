package com.power.analysis.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class StatisticDTO {

    /** 统计周期(日期字符串或小时索引) */
    private String period;

    /** 小时索引(日负荷曲线用) */
    private Integer hourIndex;

    /** 总用电量(kWh) */
    private BigDecimal totalEnergy;

    /** 平均功率(kW) */
    private BigDecimal avgPower;

    /** 最大功率(kW) */
    private BigDecimal maxPower;

    /** 最小功率(kW) */
    private BigDecimal minPower;
}
