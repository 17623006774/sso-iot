package com.example.iot.service;

import com.example.iot.bean.Computer;
import com.example.iot.bean.MyBean;
import com.example.iot.bean.Phone;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApiService {

    @Autowired
    Map<String,DataHandler> iotHandlerMap;


    public Object requestApi(MyBean bean,List<Phone> phones,List<Computer> computers){
        // 聚合
        String device = bean.getDevice();
        Object res = null;
        switch (device){
            case "phone":
                return iotHandlerMap.get(device).convert(bean,phones);
            case "compute":

                return iotHandlerMap.get(device).convert(bean,computers);
            default: break;
        }
        return res;
    }
}
