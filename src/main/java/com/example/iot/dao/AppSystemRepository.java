package com.example.iot.dao;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AppSystemRepository{

    public Optional<AppSystem> getAppSystemByAppKey(String appKey){
        if (Objects.nonNull(appKey))

        {
            AppSystem appSystem = AppSystem.builder()
                    .appKey("root")
                    .appName("iot")
                    .appSecret("root")
                    .id("1")
                    .createTime("1999").companyId(1L).build();
            return Optional.of(appSystem);
        }
        return null;
    }

}