package com.power.analysis.controller;

import com.power.analysis.common.PageResult;
import com.power.analysis.common.Result;
import com.power.analysis.entity.Device;
import com.power.analysis.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "设备管理")
@RestController
@RequestMapping("/api/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @ApiOperation("新增设备")
    @PostMapping
    public Result<Void> save(@RequestBody Device device) {
        deviceService.save(device);
        return Result.success();
    }

    @ApiOperation("修改设备")
    @PutMapping
    public Result<Void> update(@RequestBody Device device) {
        deviceService.update(device);
        return Result.success();
    }

    @ApiOperation("删除设备")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        deviceService.deleteById(id);
        return Result.success();
    }

    @ApiOperation("根据ID查询")
    @GetMapping("/detail/{id}")
    public Result<Device> getById(@PathVariable Long id) {
        return Result.success(deviceService.getById(id));
    }

    @ApiOperation("分页查询设备")
    @GetMapping("/list")
    public Result<PageResult<Device>> pageQuery(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.success(deviceService.pageQuery(name, type, region, status, page, size));
    }
}
