package com.example.iot.controller;


import com.example.iot.bean.Computer;
import com.example.iot.bean.Iot;
import com.example.iot.bean.MyBean;
import com.example.iot.bean.Phone;
import com.example.iot.exception.ParamaErrorException;
import com.example.iot.service.ApiService;
import com.example.iot.service.IotDBService;
import com.example.iot.vo.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class ViewController {

    @Autowired
    private IotDBService iotDBService;

    @Autowired
    private ApiService apiService;

    public static  List<Phone> phones;

    public static List<Computer> computers;

    @PostConstruct
    public void init(){
        phones = iotDBService.listIotData("phone");
        computers = iotDBService.listIotData("compute");
    }

    @RequestMapping("/view")
    @ResponseBody
    public List<Iot> view(){
        log.info("phone:{}",phones.size());
        log.info("compute:{}",computers.size());
//        List<Float> collect = phones.stream().map(x -> x.eleNumber).collect(Collectors.toList());
//        log.info("a:{}",collect);
        return null;
    }

    // 对外提供功能 , 聚合
    @ResponseBody
    @RequestMapping("/api")
    public Object api(@Valid MyBean bean, BindingResult bindingResult){

        if(bean.getDevice() == null || bean.getField() == null || bean.getAgg() == null) {
            throw new ParamaErrorException();
        }
        if(!bean.getDevice().equals("device") && !bean.getDevice().equals("compute")){
            throw new ParamaErrorException();
        }
        // 聚合
        Object res = null;
        res = apiService.requestApi(bean,phones,computers);
        if(Objects.isNull(res)) return ResponseEntity.error("参数有误");
//        List<Phone> phones = (List<Phone>) iots.stream().map(Phone::convert).collect(Collectors.toList());


        return ResponseEntity.ok(res);

    }

    @GetMapping("orders")
    @ResponseBody
    public String order(){
        return "1";
    }


    @GetMapping("/api/logout")
    public String doLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {//清除认证
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        // 请求认证中心/logout
        return "redirect:http://localhost:8080/logout?" + request.getQueryString();
    }
}
