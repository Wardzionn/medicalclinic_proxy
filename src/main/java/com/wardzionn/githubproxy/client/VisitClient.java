package com.wardzionn.githubproxy.client;

import com.wardzionn.githubproxy.configuration.FeignConfig;
import com.wardzionn.githubproxy.dto.PageDto;
import com.wardzionn.githubproxy.dto.VisitDto;
import com.wardzionn.githubproxy.dto.VisitSearchCriteria;
import com.wardzionn.githubproxy.fallback.VisitClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
        value = "visitClient",
        name = "visitClient",
        configuration = FeignConfig.class,
        fallbackFactory = VisitClientFallbackFactory.class
)
public interface VisitClient {

    @RequestMapping(method = RequestMethod.GET, value = "/visits", produces = "application/json")
    PageDto<VisitDto> getVisits(@SpringQueryMap VisitSearchCriteria criteria);

    @RequestMapping(method = RequestMethod.PATCH, value = "/visits/{id}/patient/{patientId}", produces = "application/json")
    VisitDto bookVisit(@PathVariable("id") Long visitId, @PathVariable("patientId") Long patientId);

    @RequestMapping(method = RequestMethod.DELETE, value = "/visits/{id}", produces = "application/json")
    PageDto<VisitDto> cancelVisit(@PathVariable("id") Long visitId);
}
