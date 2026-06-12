package com.power.analysis.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("alarm_record")
public class AlarmRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 设备ID */
    private Long deviceId;

    /** 告警类型: OVER_VOLTAGE-过压, UNDER_VOLTAGE-欠压, OVER_CURRENT-过流, OVER_LOAD-过载 */
    private String alarmType;

    /** 告警级别: INFO-提示, WARN-警告, ERROR-严重 */
    private String alarmLevel;

    /** 告警内容 */
    private String content;

    /** 处理状态: PENDING-待处理, PROCESSING-处理中, RESOLVED-已解决 */
    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
