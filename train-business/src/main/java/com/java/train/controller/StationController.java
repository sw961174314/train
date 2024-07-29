package com.java.train.controller;

import com.java.train.req.StationQueryReq;
import com.java.train.req.StationSaveReq;
import com.java.train.resp.CommonResp;
import com.java.train.resp.PageResp;
import com.java.train.resp.StationQueryResp;
import com.java.train.service.StationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/station")
public class StationController {

    @Resource
    private StationService stationService;

    @GetMapping("/query-all")
    public CommonResp<List<StationQueryResp>> queryList() {
        List<StationQueryResp> list = stationService.queryAll();
        return new CommonResp<>(list);
    }
}