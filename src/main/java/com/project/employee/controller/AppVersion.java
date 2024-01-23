package com.project.employee.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AppVersion {

    @Value("${app.version}")
    private String appVersion;


    @GetMapping("/appVersion")
    public Map getVersion(){
        Map map = new HashMap<String, String>();
        map.put("app-version", appVersion);
        return map;
    }
}
