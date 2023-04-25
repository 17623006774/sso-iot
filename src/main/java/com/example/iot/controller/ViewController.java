package com.example.iot.controller;


import com.example.iot.bean.Computer;
import com.example.iot.bean.Iot;
import com.example.iot.bean.MyBean;
import com.example.iot.bean.Phone;
import com.example.iot.exception.ParamaErrorException;
import com.example.iot.service.ApiService;
import com.example.iot.service.IotDBService;
import com.example.iot.vo.ResponseEntity;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.stream.Collectors;

@Api(tags = "xx")
@Controller
@Slf4j
public class ViewController {

    @Autowired
    private IotDBService iotDBService;

    @Autowired
    private ApiService apiService;

    public static  List<Phone> phones;

    public static List<Computer> computers;

    public static LinkedList<Iot> queue = new LinkedList();

    // 展示的数据
    public static List<String> scollData = new ArrayList<>();

    @PostConstruct
    public void init(){
        phones = iotDBService.listIotData("phone");
        computers = iotDBService.listIotData("compute");
        scollData.addAll(phones.subList(0,5).stream().map(x->((Iot)x).convert()).collect(Collectors.toList()));
    }

    // 可视化
    @RequestMapping("/view")
//    @ResponseBody
    public String view(HttpSession session){
        log.info("phone:{}",phones.size());
        log.info("compute:{}",computers.size());
        session.setAttribute("phones",phones);
        session.setAttribute("computes",computers);

        // status  开机关机
        long startPhoneCount = phones.stream().filter(p -> p.status).count();
        long startComputerCount = computers.stream().filter(c -> c.status).count();
        ArrayList<Long> statusCount = new ArrayList<>();
        statusCount.add(startPhoneCount);
        statusCount.add(startComputerCount);
        session.setAttribute("statusCount",statusCount);
        DecimalFormat format = new DecimalFormat("#.00");
        // echarts3-4 电量
        TreeMap<String, Double> phoneEle = phones.stream().collect(Collectors.groupingBy(o -> o.getPhoneType(),
                TreeMap::new,
                Collectors.averagingDouble(o -> Double.parseDouble(format.format(o.getEleNumber())))));
        System.out.println(phoneEle);
        Set<Map.Entry<String, Double>> entrySet = phoneEle.entrySet();
        List<Map.Entry<String,Double>> list = new ArrayList<Map.Entry<String,Double>>(entrySet);
        Collections.sort(list, (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        System.out.println(list);
        ArrayList<Object> PhoneEList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : list) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name",entry.getKey());
            map.put("value",format.format(entry.getValue()));
            PhoneEList.add(map);
        }
        session.setAttribute("phoneE",PhoneEList.subList(0,6));

        TreeMap<String, Double> ComputerEle = computers.stream().collect(Collectors.groupingBy(o -> o.getComputerType(),
                TreeMap::new,
                Collectors.averagingDouble(o -> Double.parseDouble(format.format(o.getEleNumber())))));
        Set<Map.Entry<String, Double>> entrySet1 = ComputerEle.entrySet();
        List<Map.Entry<String,Double>> list1 = new ArrayList<Map.Entry<String,Double>>(entrySet1);
        Collections.sort(list1, (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        System.out.println(list1.subList(0,6));
        ArrayList<Object> ComputerList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : list1) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name",entry.getKey());
            map.put("value",format.format(entry.getValue()));
            ComputerList.add(map);
        }
        System.out.println();
        session.setAttribute("computerE",ComputerList.subList(0,6));

        // 温度
        TreeMap<String, Double> phoneT = phones.stream().collect(Collectors.groupingBy(o -> o.getPhoneType(),
                TreeMap::new,
                Collectors.averagingDouble(o -> Double.parseDouble(format.format(o.getTemperature())))));
        Set<Map.Entry<String, Double>> P1 = phoneT.entrySet();
        List<Map.Entry<String,Double>> L1 = new ArrayList<Map.Entry<String,Double>>(P1);
        Collections.sort(L1, (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        List Tem = new ArrayList();
        Tem.add(L1.stream().map(x->format.format(x.getValue())).collect(Collectors.toList()));

        TreeMap<String, Double> ComputerT = computers.stream().collect(Collectors.groupingBy(o -> o.getComputerType(),
                TreeMap::new,
                Collectors.averagingDouble(o -> Double.parseDouble(format.format(o.getTemperature())))));

        List<Map.Entry<String,Double>> L2 = new ArrayList<Map.Entry<String,Double>>(ComputerT.entrySet());
        Collections.sort(L2, (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        Tem.add(L2.stream().map(x->format.format(x.getValue())).collect(Collectors.toList()));
        List names = new ArrayList();
        for (int i = 0; i < 6; i++) {
            names.add(L1.get(i).getKey() + "/" + L2.get(i).getKey());
        }
        updataData();
        Tem.add(names);
        // 滑动数据
        session.setAttribute("scroll",scollData);
        // 温度数据
        session.setAttribute("t",Tem);



        // 取数据


//        List<Float> collect = phones.stream().map(x -> x.eleNumber).collect(Collectors.toList());
//        log.info("a:{}",collect);
        return "index";
    }


    public static void updataData(){
        int count = 0;
        if(queue.size() >= 5){
            scollData.clear();
            while (count < 5){
                count++;
                scollData.add(Objects.requireNonNull(queue.poll()).convert());
            }
        }
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
