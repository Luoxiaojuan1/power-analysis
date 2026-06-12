package com.power.analysis.controller;

import com.power.analysis.common.Result;
import com.power.analysis.dto.StatisticDTO;
import com.power.analysis.service.StatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "统计分析")
@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @ApiOperation("按日统计用电量")
    @GetMapping("/daily-energy")
    public Result<List<StatisticDTO>> dailyEnergy(
            @RequestParam(required = false) Long deviceId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(statisticService.dailyEnergy(deviceId, startTime, endTime));
    }

    @ApiOperation("按月统计用电量")
    @GetMapping("/monthly-energy")
    public Result<List<StatisticDTO>> monthlyEnergy(
            @RequestParam(required = false) Long deviceId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(statisticService.monthlyEnergy(deviceId, startTime, endTime));
    }

    @ApiOperation("日负荷曲线")
    @GetMapping("/daily-load")
    public Result<List<StatisticDTO>> dailyLoad(
            @RequestParam(required = false) Long deviceId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime date) {
        return Result.success(statisticService.dailyLoad(deviceId, date));
    }
}
