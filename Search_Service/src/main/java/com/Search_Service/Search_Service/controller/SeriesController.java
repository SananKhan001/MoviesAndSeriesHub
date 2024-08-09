package com.Search_Service.Search_Service.controller;

import com.Search_Service.Search_Service.repository.SeriesRepository;
import com.Search_Service.Search_Service.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/series")
public class SeriesController {

    @Autowired
    private SeriesService seriesService;

    @GetMapping(path = "/findAll")
    public ResponseEntity findAll() {
        return ResponseEntity.ok()
                .body(seriesService.findAll());
    }

}
