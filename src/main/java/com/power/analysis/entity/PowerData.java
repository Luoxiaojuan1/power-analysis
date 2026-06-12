package com.power.analysis.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("power_data")
public class PowerData {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 设备ID */
    private Long deviceId;

    /** 电压(V) */
    private BigDecimal voltage;

    /** 电流(A) */
    private BigDecimal current;

    /** 有功功率(kW) */
    private BigDecimal power;

    /** 电量(kWh) */
    private BigDecimal energy;

    /** 功率因数 */
    private BigDecimal powerFactor;

    /** 采集时间 */
    private LocalDateTime collectTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
