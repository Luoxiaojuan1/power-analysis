package com.power.analysis.controller;

import com.power.analysis.common.PageResult;
import com.power.analysis.common.Result;
import com.power.analysis.entity.AlarmRecord;
import com.power.analysis.service.AlarmRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "告警管理")
@RestController
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
public class AlarmRecordController {

    private final AlarmRecordService alarmRecordService;

    @ApiOperation("分页查询告警记录")
    @GetMapping("/list")
    public Result<PageResult<AlarmRecord>> pageQuery(
            @RequestParam(required = false) Long deviceId,
            @RequestParam(required = false) String alarmType,
            @RequestParam(required = false) String alarmLevel,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.success(alarmRecordService.pageQuery(deviceId, alarmType, alarmLevel, status, page, size));
    }

    @ApiOperation("更新告警状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        alarmRecordService.updateStatus(id, status);
        return Result.success();
    }
}
