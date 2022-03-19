CREATE TABLE `t_problems` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
      `c_create_time` datetime DEFAULT NULL COMMENT '创建时间',
      `c_update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      `state` varchar(5) DEFAULT NULL COMMENT '状态：0-待解决  1-部分解决  2-已解决',
      `summary` varchar(512) DEFAULT NULL COMMENT '问题描述摘要',
      `describe` text COMMENT '问题具体描述',
      `extend` text COMMENT '扩展信息-代码块',
      PRIMARY KEY (`id`),
      KEY `idx_summary` (`summary`) USING BTREE COMMENT '描述索引',
      KEY `idx_create_time` (`c_create_time`) USING BTREE COMMENT '创建时间索引',
      KEY `idx_update_time` (`c_update_time`) USING BTREE COMMENT '更新时间索引'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='问题记录主表';