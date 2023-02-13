package com.example.iot.bean;


import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@ToString
@Setter
@NoArgsConstructor
public class Iot {



     public boolean status;

     public Float temperature;


    // 电量
     public Float eleNumber;
}
