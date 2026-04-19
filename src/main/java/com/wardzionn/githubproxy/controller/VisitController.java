package com.wardzionn.githubproxy.controller;

import com.wardzionn.githubproxy.dto.responses.VisitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PatchMapping("{id}/book")
    public VisitDto bookVisit(@RequestBody BookVisitCommand command) {
        return visitService.bookVisit();
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VisitDto> getVisits(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) {
        return visitService.getVisits(specialization, from, to);
    }

    @GetMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    public List<VisitDto> getAvailableVisits(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) {
        return visitService.getAvailableVisits(specialization, date, from, to);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void cancelVisit(@PathVariable Long id) {
        visitService.cancelVisit(id);
    }
}