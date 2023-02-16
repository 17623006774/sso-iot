package com.example.iot;

import java.text.DecimalFormat;

public class T {

    public static void main(String[] args) {
        DecimalFormat format = new DecimalFormat("#.00");
        System.out.println(Double.parseDouble(format.format(54.614999999999995)));
    }
}
