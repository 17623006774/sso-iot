package com.example.iot.controller;

import com.example.iot.service.IotDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AcceptDataController {


    @Autowired
    private IotDBService service;

    // 接受数据
    @RequestMapping("/accept")
    public void accept(String value){
        System.out.println(value);
        service.insertData(value);

    }



}
