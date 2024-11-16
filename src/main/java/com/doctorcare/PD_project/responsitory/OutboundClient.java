package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.dto.request.ExchangeTokenRequest;
import com.doctorcare.PD_project.dto.response.ExchangeTokenResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "outbound", url = "https://oauth2.googleapis.com")
public interface OutboundClient {
    @PostMapping("/token")
    ExchangeTokenResponse exchangeToken(@QueryMap ExchangeTokenRequest request);
}
