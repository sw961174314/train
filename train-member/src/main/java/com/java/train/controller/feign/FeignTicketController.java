package com.java.train.controller.feign;

import com.java.train.req.MemberTicketReq;
import com.java.train.resp.CommonResp;
import com.java.train.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 提供给train-business模块使用
 */
@RestController
@RequestMapping("/feign/ticket")
public class FeignTicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody MemberTicketReq req) {
        ticketService.save(req);
        return new CommonResp<>();
    }
}