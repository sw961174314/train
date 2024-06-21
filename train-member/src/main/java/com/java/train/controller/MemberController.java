package com.java.train.controller;

import com.java.train.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/count")
    public int count() {
        return memberService.count();
    }

    /**
     * 用户注册接口
     * @param mobile
     * @return
     */
    @PostMapping("/register")
    public long register(String mobile) {
        return memberService.register(mobile);
    }
}