package com.example.iot.bean;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.BeanUtils;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
public class Iot {


     public boolean status;

     public Float temperature;


    // 电量
     public Float eleNumber;

    // 输出格式
    public String convert() {
        if(this.getClass() == Phone.class) {
            Phone phone = new Phone();
            BeanUtils.copyProperties(this,phone);
            return "手机端-" + phone.getPhoneType() + " 电量：" + phone.getEleNumber() + " 温度：" + phone.getTemperature();
        }else if(this.getClass() == Computer.class){
            Computer computer = new Computer();
            BeanUtils.copyProperties(this,computer);
            return "pc端-" + computer.getComputerType() + " 电量:" + computer.getEleNumber() + " 温度" + computer.getTemperature();
        }else{
            return "无";
        }
    }
}
