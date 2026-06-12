package com.power.analysis.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("device")
public class Device {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 设备名称 */
    private String name;

    /** 设备类型: METER-电表, TRANSFORMER-变压器, SENSOR-传感器 */
    private String type;

    /** 所属区域 */
    private String region;

    /** 设备状态: ONLINE-在线, OFFLINE-离线, MAINTENANCE-维护 */
    private String status;

    /** 安装日期 */
    private LocalDateTime installDate;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
