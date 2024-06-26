package com.java.train.controller;

import com.java.train.context.LoginMemberContext;
import com.java.train.req.PassengerQueryReq;
import com.java.train.req.PassengerSaveReq;
import com.java.train.resp.CommonResp;
import com.java.train.resp.PassengerQueryResp;
import com.java.train.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    /**
     * 乘车人新增
     * @param req
     * @return
     */
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody PassengerSaveReq req) {
        passengerService.save(req);
        return new CommonResp<>();
    }

    /**
     * 乘车人查询
     * @param req
     * @return
     */
    @GetMapping("/queryList")
    public CommonResp<List<PassengerQueryResp>> queryList(@Valid PassengerQueryReq req) {
        // 从本地线程中获取会员ID
        req.setMemberId(LoginMemberContext.getId());
        List<PassengerQueryResp> list = passengerService.queryList(req);
        return new CommonResp<>(list);
    }
}