package com.example.iot.bean;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.BeanUtils;

@Getter
@ToString(callSuper = true)
@Setter
@NoArgsConstructor
public class Computer extends Iot{

    private String computerType;

    @Builder(toBuilder = true)
    public Computer(String computerType ,float eleNumber,float temperature,Boolean status){
        super(status,temperature,eleNumber);
        this.computerType = computerType;
    }

    public static Computer convert(Object obj){
        Computer computer = new Computer();
        BeanUtils.copyProperties(obj,computer);
        return computer;
    }


}
