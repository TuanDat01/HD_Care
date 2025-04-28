package com.doctorcare.PD_project.configure;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.Executor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final Executor mvcTaskExecutor;

    public WebMvcConfig(@Qualifier("mvcTaskExecutor") Executor mvcTaskExecutor) {
        this.mvcTaskExecutor = mvcTaskExecutor;
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor((AsyncTaskExecutor) mvcTaskExecutor);
        configurer.setDefaultTimeout(30000); // timeout 30s
    }
}

