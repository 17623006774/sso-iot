package com.example.iot;

import com.example.iot.bean.Iot;
import com.example.iot.bean.Phone;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class T {

    public static void main(String[] args) {
        DecimalFormat format = new DecimalFormat("#.00");
        System.out.println(Double.parseDouble(format.format(54.614999999999995)));

        Iot p = Phone.builder()
                .temperature(1)
                .phoneType("p")
                .eleNumber(2)
                .status(true)
                .build();
        System.out.println(p);
        System.out.println(p.toString());
        System.out.println(p.getClass());
        System.out.println(((Phone)p).getPhoneType());

        List<Iot> list = new ArrayList<>();

        Iot iot1 = new Iot();


        list.add(p);
        for (Iot iot : list) {
            System.out.println((Phone)iot);
        }
        System.out.println(list);
    }
}
