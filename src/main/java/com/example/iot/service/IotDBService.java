package com.example.iot.service;


import com.example.iot.bean.Computer;
import com.example.iot.bean.Iot;
import com.example.iot.bean.Phone;
import com.example.iot.config.IotDBConfig;
import com.example.iot.utils.ClassFileds;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
@Slf4j
public class IotDBService {

    @Autowired
    private IotDBConfig iotDBConfig;
    private Random random = new Random();
    public String deviceName = "root.ln.";

    @Autowired
    Map<String,DataHandler> iotHandlerMap;

    private String[] devices = {"phone","compute"};

    public void insertData(String value){
        int count = 0;
        try {
            count = Integer.parseInt(value);
        }catch (Exception e){
            log.error("error:{}",e.getMessage());
        }
        List<Iot> res = new ArrayList();
        String device = "";
        List<String> names;
        List<String> values;
        for (int i = 0; i < count; i++) {
            device = devices[random.nextInt(devices.length)];
            // 构造数据 -- 策略模式
            Iot iot = iotHandlerMap.get(device).createData();

            names = ClassFileds.getAllFields(iot.getClass());
            values = ClassFileds.getValues(iot,names);

//            System.out.println(names);
//            System.out.println(values);
            iotDBConfig.insertRecord(deviceName+device,System.currentTimeMillis(),
                    names,values);
        }


//  create  timeseries root.ln.phone.temperature with datatype=float,encoding=plain;
//   create  timeseries root.ln.phone.status with datatype=boolean,encoding=plain
//  create  timeseries root.ln.phone.eleNumber with datatype=int64,encoding=plain
//  create  timeseries root.ln.phone.phoneType with datatype=text,encoding=plain


//  create  timeseries root.ln.compute.temperature with datatype=float,encoding=plain;
//   create  timeseries root.ln.compute.status with datatype=boolean,encoding=plain
//  create  timeseries root.ln.compute.eleNumber with datatype=int64,encoding=plain
//  create  timeseries root.ln.compute.phoneType with datatype=text,encoding=plain
//            iotDBConfig.insertRecord(device,time,names,values);
//        String device = "root.ln.wf01";
//        long time = System.currentTimeMillis();
//        List<String> names = new ArrayList<>();
//        names.add("status");
//        names.add("temperature");
//        List<String> values = new ArrayList<>();
//        values.add("true");
//        values.add("37");

    }


    public List listIotData(String type){
        switch (type){
            case "phone":
                return iotDBConfig.select(Phone.class,type);
            case "compute":
                return iotDBConfig.select(Computer.class,type);
            default:break;
        }
        return null;
    }
}
