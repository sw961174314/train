package com.java.train.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.java.train.context.LoginMemberContext;
import com.java.train.domain.Passenger;
import com.java.train.mapper.PassengerMapper;
import com.java.train.req.PassengerSaveReq;
import com.java.train.util.SnowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassengerService {

    @Autowired
    private PassengerMapper passengerMapper;

    /**
     * 乘车人新增
     * @param req
     */
    public void save(PassengerSaveReq req) {
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);
        passenger.setMemberId(LoginMemberContext.getId());
        passenger.setId(SnowUtil.getSnowflakeNextId());
        passenger.setCreateTime(now);
        passenger.setUpdateTime(now);
        passengerMapper.insert(passenger);
    }
}