package com.java.train.job;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.java.train.feign.BussinessFeign;
import com.java.train.resp.CommonResp;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

// 禁止跑批并发执行
@DisallowConcurrentExecution
public class DailyTrainJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainJob.class);

    @Autowired
    private BussinessFeign bussinessFeign;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 增加日志流水号
        MDC.put("LOG_ID", System.currentTimeMillis() + RandomUtil.randomString(3));
        LOG.info("生成每日车次数据开始");
        Date date = new Date();
        Date offsetDate = DateUtil.offsetDay(date, 15).toJdkDate();
        CommonResp<Object> commonResp = bussinessFeign.genDaily(offsetDate);
        LOG.info("生成每日车次数据结束，结果：{}",commonResp);
    }
}