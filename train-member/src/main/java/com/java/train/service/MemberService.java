package com.java.train.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.java.train.domain.Member;
import com.java.train.domain.MemberExample;
import com.java.train.exception.BusinessException;
import com.java.train.exception.BusinessExceptionEnum;
import com.java.train.mapper.MemberMapper;
import com.java.train.req.MemberLoginReq;
import com.java.train.req.MemberRegisterReq;
import com.java.train.req.MemberSendCodeReq;
import com.java.train.resp.MemberLoginResp;
import com.java.train.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MemberMapper memberMapper;

    public int count() {
        return Math.toIntExact(memberMapper.countByExample(null));
    }

    /**
     * 用户注册
     * @param req
     * @return
     */
    public long register(MemberRegisterReq req) {
        String mobile = req.getMobile();
        Member memberDB = selectByMobile(mobile);
        if (ObjectUtil.isNotNull(memberDB)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }

        Member member = new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }

    /**
     * 验证码发送
     * @param req
     */
    public void sendCode(MemberSendCodeReq req) {
        String mobile = req.getMobile();
        Member memberDB = selectByMobile(mobile);

        // 如果手机号不存在则往数据库中写入用户数据
        if (ObjectUtil.isNull(memberDB)) {
            LOG.info("手机号不存在，插入一条记录");
            Member member = new Member();
            member.setId(SnowUtil.getSnowflakeNextId());
            member.setMobile(mobile);
            memberMapper.insert(member);
        }else {
            LOG.info("手机号存在，不插入记录");
        }

        // 生成验证码
        String code = RandomUtil.randomString(4);
        LOG.info("生成短信验证码：{}", code);

        // todo 保存短信记录表：手机号、短信验证码、有效期、是否已使用，业务类型，发送时间，使用时间
        LOG.info("保存短信记录表");

        // todo 对接短信通道 发送短信
        LOG.info("对接短信通道");
    }

    /**
     * 登录
     * @param req
     */
    public MemberLoginResp login(MemberLoginReq req) {
        String mobile = req.getMobile();
        String code = req.getCode();
        Member memberDB = selectByMobile(mobile);

        // 如果手机号不存在则抛出异常
        if (ObjectUtil.isNull(memberDB)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }

        // 校验短信验证码
        if (!"8888".equals(code)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_CODE_EXIST);
        }

        return BeanUtil.copyProperties(memberDB, MemberLoginResp.class);
    }

    private Member selectByMobile(String mobile) {
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);
        if (CollUtil.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }
}