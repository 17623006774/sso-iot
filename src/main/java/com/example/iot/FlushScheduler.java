package com.example.iot;


import com.example.iot.controller.ViewController;
import com.example.iot.service.IotDBService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;


    @Scheduled(cron = "0/120 * * * * ?")
    public void flushData(){
        long start = System.currentTimeMillis();
        ViewController.phones = iotDBService.listIotData("phone");
        ViewController.computers = iotDBService.listIotData("compute");
        long end = System.currentTimeMillis();
        log.info("Data flush success: {}ms",end-start);
    }


    @SneakyThrows
    @Scheduled(cron = "0/100 * * * * ?")
    public void insertData(){
        long start = System.currentTimeMillis();
        iotDBService.insertData("20");
        long end = System.currentTimeMillis();
        log.info("Data insert success: {}ms",end-start);
        restTemplate.getForEntity("http://localhost:8082/view",null);
    }
}
