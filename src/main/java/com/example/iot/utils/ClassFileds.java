package com.example.iot.utils;

import com.example.iot.bean.Iot;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClassFileds {

    public static List<String> getAllFields(Class<?> clazz) {
        List<String> fieldList = new ArrayList<>();
        while (clazz != null){
            fieldList.addAll(Arrays.stream(clazz.getDeclaredFields()).map(x->x.getName()).collect(Collectors.toList()));
            clazz = clazz.getSuperclass();
        }

        return fieldList;

    }

    public static <T> List<String> getValues(T iot, List<String> names){
        ArrayList<String> values = new ArrayList<>();
        for (String name : names) {
            values.add(getFieldValueByClass(iot,name));
        }
        return values;
    }

    @SneakyThrows
    public static  <T> String getFieldValueByClass(T t, String field) {
        Class<?> aClass = t.getClass();
        try {
            Field f = aClass.getDeclaredField(field);

            f.setAccessible(true);
            return String.valueOf(f.get(t));
        } catch (Exception e) {
            Field f = aClass.getSuperclass().getDeclaredField(field);
            f.setAccessible(true);
            return String.valueOf(f.get(t));
        }
    }



}
