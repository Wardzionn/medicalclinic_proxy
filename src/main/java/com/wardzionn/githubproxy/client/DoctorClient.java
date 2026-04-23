package com.wardzionn.githubproxy.client;

import com.wardzionn.githubproxy.configuration.FeignConfig;
import com.wardzionn.githubproxy.dto.PageDto;
import com.wardzionn.githubproxy.dto.responses.DoctorResponseDto;
import com.wardzionn.githubproxy.fallback.DoctorClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        value = "doctorClient",
        name = "doctorClient",
        configuration = FeignConfig.class,
        fallbackFactory = DoctorClientFallbackFactory.class
)
public interface DoctorClient {

    @RequestMapping(method = RequestMethod.GET, value = "/doctors", produces = "application/json")
    PageDto<DoctorResponseDto> getDoctors(
            @RequestParam(value = "specialization", required = false) String specialization,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    );
}
