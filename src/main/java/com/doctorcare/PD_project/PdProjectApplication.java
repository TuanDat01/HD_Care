package com.doctorcare.PD_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


import java.util.Map;
@SpringBootApplication
@EnableFeignClients
public class PdProjectApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(PdProjectApplication.class, args
        );
    }

}