package com.java.train.controller;

import com.java.train.req.ConfirmOrderDoReq;
import com.java.train.req.ConfirmOrderQueryReq;
import com.java.train.resp.CommonResp;
import com.java.train.resp.ConfirmOrderQueryResp;
import com.java.train.resp.PageResp;
import com.java.train.service.ConfirmOrderService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/confirm-order")
public class ConfirmOrderController {

    @Resource
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/do")
    public CommonResp<Object> doConfirm(@Valid @RequestBody ConfirmOrderDoReq req) {
        confirmOrderService.doConfirm(req);
        return new CommonResp<>();
    }
}