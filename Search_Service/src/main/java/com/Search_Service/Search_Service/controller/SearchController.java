package com.Search_Service.Search_Service.controller;

import com.Search_Service.Search_Service.enums.IndexReference;
import com.Search_Service.Search_Service.request.SearchRequest;
import com.Search_Service.Search_Service.service.SearchService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    public ResponseEntity search(@RequestBody @Valid SearchRequest searchRequest) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(searchService.search(searchRequest));
    }

}
