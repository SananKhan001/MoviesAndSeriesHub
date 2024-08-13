package com.Search_Service.Search_Service.controller;

import co.elastic.clients.elasticsearch.nodes.Http;
import com.Search_Service.Search_Service.enums.IndexReference;
import com.Search_Service.Search_Service.request.SearchRequest;
import com.Search_Service.Search_Service.service.SearchService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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

    @GetMapping(path = "/autoSuggest/{partialName}")
    public ResponseEntity autoSuggest(@PathVariable(name = "partialName")
                                          @NotNull(message = "Name should not be null !!!")
                                            @NotEmpty(message = "Name should not be empty !!!")
                                          String partialName, @RequestParam(name = "index") IndexReference indexReference) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(searchService.autoSuggest(partialName, indexReference));
    }

}
