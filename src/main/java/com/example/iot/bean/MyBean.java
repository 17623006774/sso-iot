package com.example.iot.bean;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MyBean {

    // 手机,电脑
    @NotBlank(message = "不能为空")
    private String device;

    // 属性
    @NotBlank(message = "不能为空")
    private String field;

    // 需要聚合的字段
    @NotBlank(message = "不能为空")
    private String agg;


}
