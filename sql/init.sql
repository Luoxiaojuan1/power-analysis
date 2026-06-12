-- 电力数据分析系统 建表脚本
CREATE DATABASE IF NOT EXISTS power_analysis DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE power_analysis;

-- 设备表
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` VARCHAR(100) NOT NULL COMMENT '设备名称',
    `type` VARCHAR(30) NOT NULL COMMENT '设备类型: METER-电表, TRANSFORMER-变压器, SENSOR-传感器',
    `region` VARCHAR(100) DEFAULT NULL COMMENT '所属区域',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ONLINE' COMMENT '设备状态: ONLINE-在线, OFFLINE-离线, MAINTENANCE-维护',
    `install_date` DATETIME DEFAULT NULL COMMENT '安装日期',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_type` (`type`),
    KEY `idx_region` (`region`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备信息表';

-- 电力采集数据表
DROP TABLE IF EXISTS `power_data`;
CREATE TABLE `power_data` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `device_id` BIGINT NOT NULL COMMENT '设备ID',
    `voltage` DECIMAL(10,2) DEFAULT NULL COMMENT '电压(V)',
    `current` DECIMAL(10,2) DEFAULT NULL COMMENT '电流(A)',
    `power` DECIMAL(10,2) DEFAULT NULL COMMENT '有功功率(kW)',
    `energy` DECIMAL(10,4) DEFAULT NULL COMMENT '电量(kWh)',
    `power_factor` DECIMAL(5,3) DEFAULT NULL COMMENT '功率因数',
    `collect_time` DATETIME NOT NULL COMMENT '采集时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_device_id` (`device_id`),
    KEY `idx_collect_time` (`collect_time`),
    KEY `idx_device_time` (`device_id`, `collect_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电力采集数据表';

-- 告警记录表
DROP TABLE IF EXISTS `alarm_record`;
CREATE TABLE `alarm_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `device_id` BIGINT NOT NULL COMMENT '设备ID',
    `alarm_type` VARCHAR(30) NOT NULL COMMENT '告警类型: OVER_VOLTAGE-过压, UNDER_VOLTAGE-欠压, OVER_CURRENT-过流, OVER_LOAD-过载',
    `alarm_level` VARCHAR(20) NOT NULL DEFAULT 'WARN' COMMENT '告警级别: INFO-提示, WARN-警告, ERROR-严重',
    `content` VARCHAR(500) DEFAULT NULL COMMENT '告警内容',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '处理状态: PENDING-待处理, PROCESSING-处理中, RESOLVED-已解决',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_device_id` (`device_id`),
    KEY `idx_alarm_type` (`alarm_type`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='告警记录表';

-- 插入示例设备数据
INSERT INTO `device` (`name`, `type`, `region`, `status`, `install_date`) VALUES
('1号电表', 'METER', 'A区', 'ONLINE', '2024-01-15 00:00:00'),
('2号电表', 'METER', 'A区', 'ONLINE', '2024-02-20 00:00:00'),
('1号变压器', 'TRANSFORMER', 'B区', 'ONLINE', '2024-03-10 00:00:00'),
('温湿度传感器', 'SENSOR', 'A区', 'OFFLINE', '2024-04-05 00:00:00'),
('3号电表', 'METER', 'C区', 'MAINTENANCE', '2024-05-01 00:00:00');
