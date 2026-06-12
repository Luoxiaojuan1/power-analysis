package com.power.analysis.service;

import com.power.analysis.common.PageResult;
import com.power.analysis.entity.PowerData;

import java.util.List;

public interface PowerDataService {

    /** 录入采集数据 */
    void save(PowerData powerData);

    /** 批量录入 */
    void saveBatch(List<PowerData> dataList);

    /** 分页查询采集数据 */
    PageResult<PowerData> pageQuery(Long deviceId, String startTime, String endTime, int page, int size);

    /** 根据ID查询 */
    PowerData getById(Long id);
}
