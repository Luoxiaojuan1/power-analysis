package com.power.analysis.controller;

import com.power.analysis.common.Result;
import com.power.analysis.dto.ForecastDTO;
import com.power.analysis.service.ForecastService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Api(tags = "负荷预测")
@RestController
@RequestMapping("/forecast")
@RequiredArgsConstructor
public class ForecastController {

    private final ForecastService forecastService;

    @ApiOperation("负荷预测")
    @GetMapping
    public Result<ForecastDTO> forecast(
            @RequestParam(required = false) Long deviceId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate targetDate) {
        return Result.success(forecastService.forecast(deviceId, targetDate.atStartOfDay()));
    }
}
