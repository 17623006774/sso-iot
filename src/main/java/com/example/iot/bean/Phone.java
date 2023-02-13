package com.example.iot.bean;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.List;

@Getter
@ToString(callSuper = true)
@Setter
@NoArgsConstructor
public class Phone extends Iot{

    private String phoneType;


    @Builder(toBuilder = true)
    public Phone(String phoneType ,float eleNumber,float temperature,Boolean status){
        super(status,temperature,eleNumber);
        this.phoneType = phoneType;
    }

    public Phone(String phoneType){
        this.phoneType = phoneType;
    }


    @SneakyThrows
    public static void main(String[] args) {
        Constructor<Phone> constructor = Phone.class.getConstructor();
        System.out.println(constructor.newInstance());
        doWork(Phone.class);
    }

    @SneakyThrows
    public static <T> void doWork(Class<T> ac){
        Constructor<T> constructor = ac.getConstructor();
        System.out.println(constructor.newInstance());
        System.out.println(ac.getConstructor().newInstance());

        Constructor<T> constructor1 = ac.getConstructor(String.class,float.class,float.class,Boolean.class);
        T t = constructor1.newInstance("1",0,0,true);
        System.out.println(t);


    }

    public static Phone convert(Object obj){
        Phone phone = new Phone();
        BeanUtils.copyProperties(obj,phone);
        return phone;
    }

}
