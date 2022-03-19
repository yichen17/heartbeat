CREATE TABLE `t_problems_record` (
     `problems_id` bigint(20) NOT NULL COMMENT '问题id',
     `method` text COMMENT '解决方法',
     `c_create_time` datetime DEFAULT NULL COMMENT '创建时间',
     `solver` varchar(255) DEFAULT NULL COMMENT '解决的人',
     `url` varchar(511) DEFAULT NULL COMMENT '博客地址',
     `extend` varchar(255) DEFAULT NULL,
     KEY `idx_problem_id` (`problems_id`) USING BTREE COMMENT '主问题索引',
     KEY `idx_time` (`c_create_time`) USING BTREE COMMENT '时间索引',
     KEY `idx_solver` (`solver`) USING BTREE COMMENT '问题解决人索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='解决问题记录表';