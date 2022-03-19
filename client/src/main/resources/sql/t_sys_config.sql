CREATE TABLE `t_sys_config` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `option_key` varchar(255) DEFAULT NULL COMMENT '查询key',
    `option_value` varchar(8096) DEFAULT NULL COMMENT 'value值',
    `enable` tinyint(1) DEFAULT NULL COMMENT '0-启用 1-禁用',
    `remark` varchar(255) DEFAULT NULL COMMENT '描述 key-value 用途',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_key` (`option_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='系统配置表';