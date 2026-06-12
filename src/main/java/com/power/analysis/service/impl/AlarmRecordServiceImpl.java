package com.power.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.power.analysis.common.PageResult;
import com.power.analysis.entity.AlarmRecord;
import com.power.analysis.mapper.AlarmRecordMapper;
import com.power.analysis.service.AlarmRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AlarmRecordServiceImpl implements AlarmRecordService {

    private final AlarmRecordMapper alarmRecordMapper;

    @Override
    public PageResult<AlarmRecord> pageQuery(Long deviceId, String alarmType, String alarmLevel, String status, int page, int size) {
        LambdaQueryWrapper<AlarmRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(deviceId != null, AlarmRecord::getDeviceId, deviceId);
        wrapper.eq(StringUtils.hasText(alarmType), AlarmRecord::getAlarmType, alarmType);
        wrapper.eq(StringUtils.hasText(alarmLevel), AlarmRecord::getAlarmLevel, alarmLevel);
        wrapper.eq(StringUtils.hasText(status), AlarmRecord::getStatus, status);
        wrapper.orderByDesc(AlarmRecord::getCreateTime);

        Page<AlarmRecord> result = alarmRecordMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(result.getTotal(), result.getCurrent(), result.getSize(), result.getRecords());
    }

    @Override
    public void updateStatus(Long id, String status) {
        AlarmRecord record = alarmRecordMapper.selectById(id);
        if (record != null) {
            record.setStatus(status);
            alarmRecordMapper.updateById(record);
        }
    }
}
