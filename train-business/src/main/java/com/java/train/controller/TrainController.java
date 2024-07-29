package com.java.train.controller;

import com.java.train.req.TrainQueryReq;
import com.java.train.req.TrainSaveReq;
import com.java.train.resp.CommonResp;
import com.java.train.resp.PageResp;
import com.java.train.resp.TrainQueryResp;
import com.java.train.service.TrainSeatService;
import com.java.train.service.TrainService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/train")
public class TrainController {

    @Resource
    private TrainService trainService;

    /**
     * 火车查询-全部
     * @return
     */
    @GetMapping("/query-all")
    public CommonResp<List<TrainQueryResp>> queryAll() {
        List<TrainQueryResp> list = trainService.queryAll();
        return new CommonResp<>(list);
    }
}