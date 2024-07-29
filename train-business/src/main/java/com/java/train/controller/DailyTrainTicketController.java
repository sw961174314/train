package com.java.train.controller;

import com.java.train.req.DailyTrainTicketQueryReq;
import com.java.train.req.DailyTrainTicketSaveReq;
import com.java.train.resp.CommonResp;
import com.java.train.resp.DailyTrainTicketQueryResp;
import com.java.train.resp.PageResp;
import com.java.train.service.DailyTrainTicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/daily-train-ticket")
public class DailyTrainTicketController {

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainTicketQueryResp>> queryList(@Valid DailyTrainTicketQueryReq req) {
        PageResp<DailyTrainTicketQueryResp> list = dailyTrainTicketService.queryList(req);
        return new CommonResp<>(list);
    }
}