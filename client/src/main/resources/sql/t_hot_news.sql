CREATE TABLE `t_hot_news` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '热门新闻标题',
    `no` int(5) DEFAULT NULL COMMENT '当次搜索的排名',
    `visits_real` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '实际查询-访问量 带单位',
    `visits` int(11) DEFAULT NULL COMMENT '访问量-无单位',
    `time` datetime DEFAULT NULL COMMENT '新闻时间',
    `channel` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '渠道：0 百度，1知乎',
    `url` mediumtext COLLATE utf8mb4_unicode_ci COMMENT '新闻链接',
    `imageurl` mediumtext COLLATE utf8mb4_unicode_ci COMMENT '图片 url',
    `extra1` text COLLATE utf8mb4_unicode_ci COMMENT '额外字段',
    `extra2` text COLLATE utf8mb4_unicode_ci COMMENT '额外字段',
    PRIMARY KEY (`id`),
    KEY `idx_title` (`title`) USING BTREE COMMENT '新闻标题索引',
    KEY `idx_time` (`time`) USING BTREE COMMENT '时间索引'
) ENGINE=InnoDB AUTO_INCREMENT=7051 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='每日-热门新闻记录';