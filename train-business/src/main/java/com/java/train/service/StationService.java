package com.java.train.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.java.train.domain.Train;
import com.java.train.domain.TrainExample;
import com.java.train.exception.BusinessException;
import com.java.train.exception.BusinessExceptionEnum;
import com.java.train.resp.PageResp;
import com.java.train.resp.TrainQueryResp;
import com.java.train.util.SnowUtil;
import com.java.train.domain.Station;
import com.java.train.domain.StationExample;
import com.java.train.mapper.StationMapper;
import com.java.train.req.StationQueryReq;
import com.java.train.req.StationSaveReq;
import com.java.train.resp.StationQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {

    private static final Logger LOG = LoggerFactory.getLogger(StationService.class);

    @Resource
    private StationMapper stationMapper;

    public void save(StationSaveReq req) {
        DateTime now = DateTime.now();
        Station station = BeanUtil.copyProperties(req, Station.class);
        if (ObjectUtil.isNull(station.getId())) {
            // 保存之前 先校验唯一键是否存在
            Station stationDB = selectByUnique(req.getName());
            if (ObjectUtil.isNotEmpty(stationDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_STATION_NAME_UNIQUE_ERROR);
            }

            station.setId(SnowUtil.getSnowflakeNextId());
            station.setCreateTime(now);
            station.setUpdateTime(now);
            stationMapper.insert(station);
        } else {
            station.setUpdateTime(now);
            stationMapper.updateByPrimaryKey(station);
        }
    }

    private Station selectByUnique(String name) {
        StationExample stationExample = new StationExample();
        stationExample.createCriteria().andNameEqualTo(name);
        List<Station> list = stationMapper.selectByExample(stationExample);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public PageResp<StationQueryResp> queryList(StationQueryReq req) {
        StationExample stationExample = new StationExample();
        stationExample.setOrderByClause("id desc");
        StationExample.Criteria criteria = stationExample.createCriteria();

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Station> stationList = stationMapper.selectByExample(stationExample);

        PageInfo<Station> pageInfo = new PageInfo<>(stationList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<StationQueryResp> list = BeanUtil.copyToList(stationList, StationQueryResp.class);

        PageResp<StationQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        stationMapper.deleteByPrimaryKey(id);
    }

    public List<StationQueryResp> queryAll() {
        StationExample stationExample = new StationExample();
        stationExample.setOrderByClause("name_pinyin asc");
        List<Station> stationList = stationMapper.selectByExample(stationExample);
        return BeanUtil.copyToList(stationList, StationQueryResp.class);
    }
}
