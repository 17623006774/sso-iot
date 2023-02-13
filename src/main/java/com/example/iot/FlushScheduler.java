package com.example.iot;


import com.example.iot.controller.ViewController;
import com.example.iot.service.IotDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.servlet.http.HttpServletRequest;

/**
 * 定时拉数据
 */
@Configuration
@EnableScheduling
@Slf4j
@ConditionalOnProperty(name = "data.flush",matchIfMissing = false)
public class FlushScheduler {

    @Autowired
    private IotDBService iotDBService;


    @Scheduled(cron = "0/300 * * * * ?")
    public void flushData(){
        long start = System.currentTimeMillis();
        ViewController.phones = iotDBService.listIotData("phone");
        ViewController.computers = iotDBService.listIotData("compute");
        long end = System.currentTimeMillis();
        log.info("Data flush success: {}ms",end-start);
    }


}
