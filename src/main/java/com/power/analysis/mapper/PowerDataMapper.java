package com.power.analysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.power.analysis.entity.PowerData;
import com.power.analysis.dto.StatisticDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PowerDataMapper extends BaseMapper<PowerData> {

    /** 按日统计用电量 */
    List<StatisticDTO> statisticDailyEnergy(@Param("deviceId") Long deviceId,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);

    /** 按月统计用电量 */
    List<StatisticDTO> statisticMonthlyEnergy(@Param("deviceId") Long deviceId,
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);

    /** 统计日负荷曲线 */
    List<StatisticDTO> statisticDailyLoad(@Param("deviceId") Long deviceId,
                                          @Param("date") LocalDateTime date);
}
