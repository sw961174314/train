package com.java.train.controller;

import com.java.train.req.MemberLoginReq;
import com.java.train.req.MemberRegisterReq;
import com.java.train.req.MemberSendCodeReq;
import com.java.train.resp.CommonResp;
import com.java.train.resp.MemberLoginResp;
import com.java.train.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/count")
    public CommonResp<Integer> count() {
        int count = memberService.count();
        return new CommonResp<>(count);
    }

    /**
     * 用户注册接口
     * @param req
     * @return
     */
    @PostMapping("/register")
    public CommonResp<Long> register(@Valid @RequestBody MemberRegisterReq req) {
        long register = memberService.register(req);
        return new CommonResp<>(register);
    }

    /**
     * 验证码发送接口
     * @param req
     * @return
     */
    @PostMapping("/sendCode")
    public CommonResp sendCode(@Valid @RequestBody MemberSendCodeReq req) {
        memberService.sendCode(req);
        return new CommonResp<>();
    }

    /**
     * 登录接口
     * @param req
     * @return
     */
    @PostMapping("/login")
    public CommonResp<MemberLoginResp> login(@Valid @RequestBody MemberLoginReq req) {
        MemberLoginResp resp = memberService.login(req);
        return new CommonResp<>(resp);
    }
}