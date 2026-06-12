package com.power.analysis.controller;

import com.power.analysis.common.PageResult;
import com.power.analysis.common.Result;
import com.power.analysis.entity.PowerData;
import com.power.analysis.service.PowerDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "电力数据采集")
@RestController
@RequestMapping("/power-data")
@RequiredArgsConstructor
public class PowerDataController {

    private final PowerDataService powerDataService;

    @ApiOperation("录入采集数据")
    @PostMapping
    public Result<Void> save(@RequestBody PowerData powerData) {
        powerDataService.save(powerData);
        return Result.success();
    }

    @ApiOperation("批量录入")
    @PostMapping("/batch")
    public Result<Void> saveBatch(@RequestBody List<PowerData> dataList) {
        powerDataService.saveBatch(dataList);
        return Result.success();
    }

    @ApiOperation("分页查询采集数据")
    @GetMapping
    public Result<PageResult<PowerData>> pageQuery(
            @RequestParam(required = false) Long deviceId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.success(powerDataService.pageQuery(deviceId, startTime, endTime, page, size));
    }

    @ApiOperation("根据ID查询")
    @GetMapping("/{id}")
    public Result<PowerData> getById(@PathVariable Long id) {
        return Result.success(powerDataService.getById(id));
    }
}
