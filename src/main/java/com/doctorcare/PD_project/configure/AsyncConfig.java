package com.doctorcare.PD_project.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // multi-threading
        executor.setMaxPoolSize(10); //11 email -> 1 đợi
        executor.setQueueCapacity(100); //async
        executor.setThreadNamePrefix("ASYNC_");
        executor.initialize();
        return executor;
    }
}
