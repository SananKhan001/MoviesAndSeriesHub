package com.Search_Service.Search_Service.controller;

import com.Search_Service.Search_Service.search.SearchRequestDTO;
import com.Search_Service.Search_Service.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping(path = "/findAll")
    public ResponseEntity findAll(){
        return ResponseEntity.ok()
                .body(movieService.findAll());
    }

    @GetMapping
    public ResponseEntity search(@RequestBody @Valid SearchRequestDTO searchRequest) throws IOException {
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(movieService.search(searchRequest));
    }

}
