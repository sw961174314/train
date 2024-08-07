package com.java.train.controller.admin;

import com.java.train.resp.CommonResp;
import com.java.train.resp.PageResp;
import com.java.train.req.TicketQueryReq;
import com.java.train.resp.TicketQueryResp;
import com.java.train.service.TicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/ticket")
public class TicketAdminController {

    @Resource
    private TicketService ticketService;

    @GetMapping("/query-list")
    public CommonResp<PageResp<TicketQueryResp>> queryList(@Valid TicketQueryReq req) {
        PageResp<TicketQueryResp> list = ticketService.queryList(req);
        return new CommonResp<>(list);
    }
}