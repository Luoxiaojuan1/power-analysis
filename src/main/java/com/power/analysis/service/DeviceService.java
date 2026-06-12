package com.power.analysis.service;

import com.power.analysis.common.PageResult;
import com.power.analysis.entity.Device;

public interface DeviceService {

    /** 新增设备 */
    void save(Device device);

    /** 修改设备 */
    void update(Device device);

    /** 删除设备 */
    void deleteById(Long id);

    /** 根据ID查询 */
    Device getById(Long id);

    /** 分页查询设备 */
    PageResult<Device> pageQuery(String name, String type, String region, String status, int page, int size);
}
