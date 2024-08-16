package com.java.train.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.java.train.exception.BusinessException;
import com.java.train.exception.BusinessExceptionEnum;
import com.java.train.mapper.cust.SkTokenMapperCust;
import com.java.train.resp.PageResp;
import com.java.train.util.SnowUtil;
import com.java.train.domain.SkToken;
import com.java.train.domain.SkTokenExample;
import com.java.train.mapper.SkTokenMapper;
import com.java.train.req.SkTokenQueryReq;
import com.java.train.req.SkTokenSaveReq;
import com.java.train.resp.SkTokenQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SkTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(SkTokenService.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private DailyTrainSeatService dailyTrainSeatService;

    @Autowired
    private DailyTrainStationService dailyTrainStationService;

    @Resource
    private SkTokenMapper skTokenMapper;

    @Resource
    private SkTokenMapperCust skTokenMapperCust;

    /**
     * 令牌初始化
     * @param date
     * @param trainCode
     */
    public void genDaily(Date date, String trainCode) {
        LOG.info("删除日期【{}】车次【{}】的令牌信息", DateUtil.formatDate(date), trainCode);
        SkTokenExample skTokenExample = new SkTokenExample();
        skTokenExample.createCriteria().andDateEqualTo(date).andTrainCodeEqualTo(trainCode);
        skTokenMapper.deleteByExample(skTokenExample);

        DateTime now = DateTime.now();
        SkToken skToken = new SkToken();
        skToken.setId(SnowUtil.getSnowflakeNextId());
        skToken.setDate(date);
        skToken.setTrainCode(trainCode);
        skToken.setCreateTime(now);
        skToken.setUpdateTime(now);

        int seatCount = dailyTrainSeatService.countSeat(date, trainCode);
        LOG.info("车次[{}]座位数:{}", trainCode, seatCount);

        long stationCount = dailyTrainStationService.countByTrainCode(date, trainCode);
        LOG.info("车次[{}]到站数:{}", trainCode, seatCount);

        // 3/4需要根据实际卖票比例来定 一趟火车最多可以卖(seatCount * stationCount)张火车票
        int count = (int) (seatCount * stationCount);
        LOG.info("车次[{}]初始生成令牌数:{}", trainCode, count);
        skToken.setCount(count);

        skTokenMapper.insert(skToken);
    }

    public void save(SkTokenSaveReq req) {
        DateTime now = DateTime.now();
        SkToken skToken = BeanUtil.copyProperties(req, SkToken.class);
        if (ObjectUtil.isNull(skToken.getId())) {
            skToken.setId(SnowUtil.getSnowflakeNextId());
            skToken.setCreateTime(now);
            skToken.setUpdateTime(now);
            skTokenMapper.insert(skToken);
        } else {
            skToken.setUpdateTime(now);
            skTokenMapper.updateByPrimaryKey(skToken);
        }
    }

    public PageResp<SkTokenQueryResp> queryList(SkTokenQueryReq req) {
        SkTokenExample skTokenExample = new SkTokenExample();
        skTokenExample.setOrderByClause("id desc");
        SkTokenExample.Criteria criteria = skTokenExample.createCriteria();

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<SkToken> skTokenList = skTokenMapper.selectByExample(skTokenExample);

        PageInfo<SkToken> pageInfo = new PageInfo<>(skTokenList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<SkTokenQueryResp> list = BeanUtil.copyToList(skTokenList, SkTokenQueryResp.class);

        PageResp<SkTokenQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        skTokenMapper.deleteByPrimaryKey(id);
    }

    /**
     * 令牌生成
     * @param date
     * @param trainCode
     * @return
     */
    public boolean validSkToken(Date date, String trainCode,Long memberId) {
        LOG.info("会员[{}]获取日期[{}]车次[{}]的令牌开始", memberId, DateUtil.formatDate(date), trainCode);

        // 先获取令牌锁 再校验令牌余量 防止机器人抢票 lockKey就是令牌 用来表示【谁能做什么】的一个凭证
        String lockKey = DateUtil.formatDate(date) + "-" + trainCode + "-" + memberId;
        Boolean setIfAbsent = redisTemplate.opsForValue().setIfAbsent(lockKey, lockKey, 1, TimeUnit.SECONDS);
        if (setIfAbsent) {
            LOG.info("恭喜，抢到锁了！lockKey：" + lockKey);
        } else {
            // 只是没抢到锁 并不知道票抢完了没 所以提示请稍后重试
            LOG.info("很遗憾，没抢到锁！lockKey：" + lockKey);
            return false;
        }

        // 令牌约等于库存 令牌没有了 就不再卖票 不需要再进入购票主流程去判断库存 判断令牌要比判断库存好
        int updateCount = skTokenMapperCust.decrease(date, trainCode);
        if (updateCount > 0) {
            return true;
        } else {
            return false;
        }
    }
}
