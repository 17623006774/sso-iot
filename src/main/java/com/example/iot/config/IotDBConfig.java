package com.example.iot.config;


import com.example.iot.bean.Phone;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.iotdb.session.pool.SessionDataSetWrapper;
import org.apache.iotdb.session.pool.SessionPool;
import org.apache.iotdb.tsfile.file.metadata.enums.TSDataType;
import org.apache.iotdb.tsfile.read.common.Field;
import org.apache.iotdb.tsfile.read.common.RowRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class IotDBConfig {


    @Value("${spring.iotdb.username:root}")
    private String username;

    @Value("${spring.iotdb.password:root}")
    private String password;

    @Value("${spring.iotdb.ip:127.0.0.1}")
    private String ip;

    @Value("${spring.iotdb.port:6669}")
    private int port;

    @Value("${spring.iotdb.maxSize:10}")
    private int maxSize;

    private static SessionPool sessionPool;

    private SessionPool getSessionPool() {
        if (sessionPool == null) {
            sessionPool = new SessionPool(ip, port, username, password, maxSize);
        }

        return sessionPool;
    }

    public void insertRecord(String deviceId, long time, List<String> measurements, List<String> values) {
        getSessionPool();
        try {
            sessionPool.insertRecord(deviceId, time, measurements, values);
            log.info("iotdb数据入库：device_id:[{}], measurements:[{}], values:[{}]", deviceId, measurements, values);
        } catch (Exception e) {
            log.error("IotDBSession insertRecord失败: deviceId={}, time={}, measurements={}, values={}, error={}",
                    deviceId, time, measurements, values, e.getMessage());
        }
    }

    @SneakyThrows
    public <T> List select(Class<T> aclass,String type){
        getSessionPool();
        SessionDataSetWrapper sessionDataSetWrapper = sessionPool.executeQueryStatement("select * from root.ln." + type);
        ArrayList<Object> list = new ArrayList<>();
        List<String> columnNames = sessionDataSetWrapper.getColumnNames();
        List<TSDataType> columnTypes = sessionDataSetWrapper.getColumnTypes();
//        System.out.println(columnNames);
//        System.out.println(columnNames);
        while (sessionDataSetWrapper.hasNext()) {
            RowRecord next = sessionDataSetWrapper.next();
            List<Field> fields = next.getFields();
            HashMap<String, Object> map = new HashMap<>();
            for (int i = 0; i < columnNames.size()-1; i++) {
                if("time".equalsIgnoreCase(columnNames.get(i+1))) continue;
                Field field = fields.get(i);
                Object o = convertValue(field.getObjectValue(columnTypes.get(i + 1)));
                map.put(cleanColumn(columnNames.get(i+1)),o);
            }
            Class[] classes = listClass(columnTypes);
            Constructor<T> constructor = aclass.getDeclaredConstructor(classes);
            constructor.setAccessible(true);
//            System.out.println(map.values());
            Object[] objects = map.values().toArray();
            T t = constructor.newInstance(objects);
            list.add(t);
//            System.out.println(t);
//            break;
        }
        return list;
    }

    public static String cleanColumn(String column){
        return column.replace("root.ln.compute.","").replace("root.ln.phone.","");
    }


    public static Class[] listClass(List<TSDataType> columnTypes){
        Class[] classes = new Class[columnTypes.size()-1];
        for (int i = 1; i < columnTypes.size(); i++) {
            classes[i-1] = getClass(columnTypes.get(i).name());
        }
        return classes;
    }

    public static Class getClass(String type){
        switch (type){
            case "INT64":
                return Integer.class;
            case "TEXT":
                return String.class;
            case "FLOAT":
                return float.class;
            case "BOOLEAN":
                return Boolean.class;
        }
        return null;
    }

    public Object convertValue(Object obj){
        Class<?> aClass = obj.getClass();
        String name = aClass.getName();
        switch (name){
            case "org.apache.iotdb.tsfile.utils.Binary":
                return String.valueOf(obj);
        }

//        System.out.println(obj);
        return obj;
    }


}
