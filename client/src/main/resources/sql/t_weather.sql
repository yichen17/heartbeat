CREATE TABLE `t_weather` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
     `county` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '县(具体天气地址)',
     `country` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国家(码值)',
     `max_temperature` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '当天最高温度',
     `max_weather` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '最高温度对应的天气',
     `max_wind_direction` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '最高温度对应风向',
     `max_wind_power` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '最高温度对应风力',
     `min_temperature` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '当天最低温度',
     `min_weather` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '最低温度对应的天气',
     `min_wind_direction` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '最低温度对应的风向',
     `min_wind_power` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '最低温度对应的风力',
     `latitude` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '纬度',
     `longitude` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '经度',
     `code` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '码值，前几位能找到对应归属的省或市',
     `time` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '对应查询的日期',
     PRIMARY KEY (`id`),
     KEY `idx_time_city` (`time`,`county`) USING HASH COMMENT '时间、地点查询',
     KEY `idx_time_city_code` (`time`,`code`,`county`) USING HASH COMMENT '时间、地点查询。 重复地方名 code区分',
     KEY `idx_code_city` (`code`,`county`) USING HASH COMMENT '地区 各天的天气',
     KEY `idx_time` (`time`) USING BTREE COMMENT '时间索引'
) ENGINE=InnoDB AUTO_INCREMENT=346061 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='各地天气情况记录';