package com.power.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.power.analysis.common.PageResult;
import com.power.analysis.entity.Device;
import com.power.analysis.mapper.DeviceMapper;
import com.power.analysis.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceMapper deviceMapper;

    @Override
    public void save(Device device) {
        if (device.getStatus() == null) {
            device.setStatus("ONLINE");
        }
        deviceMapper.insert(device);
    }

    @Override
    public void update(Device device) {
        deviceMapper.updateById(device);
    }

    @Override
    public void deleteById(Long id) {
        deviceMapper.deleteById(id);
    }

    @Override
    public Device getById(Long id) {
        return deviceMapper.selectById(id);
    }

    @Override
    public PageResult<Device> pageQuery(String name, String type, String region, String status, int page, int size) {
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(name), Device::getName, name);
        wrapper.eq(StringUtils.hasText(type), Device::getType, type);
        wrapper.eq(StringUtils.hasText(region), Device::getRegion, region);
        wrapper.eq(StringUtils.hasText(status), Device::getStatus, status);
        wrapper.orderByDesc(Device::getCreateTime);

        Page<Device> result = deviceMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(result.getTotal(), result.getCurrent(), result.getSize(), result.getRecords());
    }
}
