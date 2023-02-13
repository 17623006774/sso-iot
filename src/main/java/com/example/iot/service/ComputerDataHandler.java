package com.example.iot.service;

import com.example.iot.bean.Computer;
import com.example.iot.bean.Iot;
import com.example.iot.bean.MyBean;
import com.example.iot.bean.Phone;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service("compute")
public class ComputerDataHandler implements DataHandler{
    Random random = new Random();

    public String[] computes = "联想、惠普、苹果、三星、华硕、索尼、宏碁、戴尔、海尔、长城、海尔、神舟、清华同方、方正、明基"
            .split("、");



    @Override
    public Iot createData() {
        Computer computer = Computer.builder()
                .computerType(computes[random.nextInt(computes.length)])
                .temperature(random.nextFloat() * 10)
                .status(random.nextBoolean())
                .eleNumber(random.nextInt(101))
                .build();
        return computer;
    }

    @Override
    public Object convert(MyBean bean, List computers) {
        Object res = null;
        String agg = bean.getAgg();
        if(agg == null || StringUtils.isBlank(agg)){
            res = computers.stream().collect(
                    Collectors.groupingBy(Computer::getComputerType, Collectors.counting()));
        }else if("eleNumber".equalsIgnoreCase(agg)) {
            res = computers.stream().collect(
                    Collectors.groupingBy(Computer::getComputerType, Collectors.summarizingDouble(Computer::getEleNumber)));
        }else if("temperature".equalsIgnoreCase(agg)){
            res = computers.stream().collect(
                    Collectors.groupingBy(Computer::getComputerType, Collectors.summarizingDouble(Computer::getTemperature)));
        }
        return res;
    }


}
