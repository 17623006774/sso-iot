package com.example.iot.service;

import com.example.iot.bean.Iot;
import com.example.iot.bean.MyBean;

import java.util.List;

public interface DataHandler {

    Iot createData();

    Object convert(MyBean bean,List iot);

}
