package com.Stream_Service.controller;

import com.Stream_Service.service.MediaFileService;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;

@RestController
@Slf4j
public class MediaController {

    @Autowired
    private MediaFileService mediaFileService;

    // http://localhost:9091/upload/poster
    @PostMapping("/poster/upload")
    public Mono<ResponseEntity<Mono<URI>>> uploadPoster(@RequestPart("poster") FilePart poster, @RequestParam("uniquePosterId")
                                                                                                  @NotEmpty(message = "PosterId should not be Empty !!!")
                                                                                                  String uniquePosterId) throws IOException {
        log.info("Request is reaching here");
        return Mono.just(
                ResponseEntity.ok(mediaFileService.uploadPoster(poster, uniquePosterId))
        );
    }

    @GetMapping("/poster/get/{posterId}")
    public Mono<ResponseEntity<Mono<byte[]>>> getPoster(@PathVariable("posterId")
                                            @NotEmpty(message = "PosterId should not be Empty !!!")
                                            String uniquePosterId) throws IOException {
        return Mono.just(
                ResponseEntity.ok(
                        mediaFileService.getPoster(uniquePosterId)
                )
        );
    }

}
