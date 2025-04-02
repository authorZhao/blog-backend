package com.git.blog.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.git.blog.dao.mapper.StatisticsDataMapper;
import com.git.blog.dao.service.StatisticsDaoService;
import com.git.blog.dto.model.entity.StatisticsData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author authorZhao
 * @since 2025-04-01
 */
@Service
@Slf4j
public class StatisticsDaoServiceImpl extends ServiceImpl<StatisticsDataMapper, StatisticsData> implements StatisticsDaoService {
}
