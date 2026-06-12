package com.power.analysis.service;

import com.power.analysis.common.PageResult;
import com.power.analysis.entity.AlarmRecord;

public interface AlarmRecordService {

    /** 分页查询告警记录 */
    PageResult<AlarmRecord> pageQuery(Long deviceId, String alarmType, String alarmLevel, String status, int page, int size);

    /** 更新告警状态 */
    void updateStatus(Long id, String status);
}
