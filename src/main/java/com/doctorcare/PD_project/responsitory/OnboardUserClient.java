package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.dto.response.UserGoogleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user",url = "https://www.googleapis.com/oauth2/v1")
public interface OnboardUserClient {
    @GetMapping("/userinfo")
    UserGoogleResponse getUserInfo(@RequestParam("alt") String alt,
                                   @RequestParam("access_token") String accessToken);
}
