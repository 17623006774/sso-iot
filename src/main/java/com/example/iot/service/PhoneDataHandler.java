package com.example.iot.service;

import com.example.iot.bean.Iot;
import com.example.iot.bean.MyBean;
import com.example.iot.bean.Phone;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service("phone")
public class PhoneDataHandler implements DataHandler{
    Random random = new Random();

    public String[] phones = {"xiaomi","oppo","vivo","redmi","huawei","other"};



    @Override
    public Iot createData() {
//        Phone phone1 = new Phone(random.nextBoolean(), random.nextFloat() * 10, random.nextInt(101),
//                phones[random.nextInt(phones.length)]);
        Phone phone = Phone.builder()
                .phoneType(phones[random.nextInt(phones.length)])
                .temperature(random.nextFloat() * 10)
                .status(random.nextBoolean())
                .eleNumber(random.nextFloat()*100)
                .build();
        System.out.println(phone);
//        System.out.println(phone1);
        return phone;
    }

    @Override
    public Object convert(MyBean bean, List phones) {
        Object res = null;
        String agg = bean.getAgg();
        if(agg == null || StringUtils.isBlank(agg)){
            res = phones.stream().collect(
                    Collectors.groupingBy(Phone::getPhoneType, Collectors.counting()));
        }else if("eleNumber".equalsIgnoreCase(agg)) {
            res = phones.stream().collect(
                    Collectors.groupingBy(Phone::getPhoneType, Collectors.summarizingDouble(Phone::getEleNumber)));
        }else if("temperature".equalsIgnoreCase(agg)){
            res = phones.stream().collect(
                    Collectors.groupingBy(Phone::getPhoneType, Collectors.summarizingDouble(Phone::getTemperature)));
        }
        return res;
    }


}
