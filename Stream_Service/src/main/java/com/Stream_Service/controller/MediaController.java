package com.Stream_Service.controller;

import com.Stream_Service.service.MediaFileService;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;

@RestController
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MediaController {

    @Autowired
    private MediaFileService mediaFileService;

    // http://localhost:9091/upload/poster
    @PostMapping("/poster/upload")
    public Mono<ResponseEntity<Mono<URI>>> uploadPoster(@RequestPart("poster") FilePart poster, @RequestParam("uniquePosterId")
                                                                                                  @NotEmpty(message = "PosterId should not be Empty !!!")
                                                                                                  String uniquePosterId) throws IOException {
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

    @PostMapping("/profile/upload")
    public Mono<ResponseEntity<Mono<URI>>> uploadProfile(@RequestPart("profileImage") FilePart profileImage,
                                                         @RequestParam("uniqueProfileId")
                                                         @NotEmpty(message = "ProfileId should not be Empty !!!")
                                                         String uniqueProfileId){
        return Mono.just(
                ResponseEntity.ok(mediaFileService.uploadProfile(profileImage, uniqueProfileId))
        );
    }

    @GetMapping("/profile/get/{profileId}")
    public Mono<ResponseEntity<Mono<byte[]>>> getProfile(@PathVariable("profileId")
                                                        @NotEmpty(message = "ProfileId should not be Empty !!!")
                                                        String uniqueProfileId) throws IOException {
        return Mono.just(
                ResponseEntity.ok(
                        mediaFileService.getProfile(uniqueProfileId)
                )
        );
    }

    @PostMapping("/video/upload")
    public Mono<ResponseEntity<Mono<URI>>> uploadVideo(@RequestPart("video") FilePart video,
                                                         @RequestParam("uniqueId")
                                                         @NotEmpty(message = "UniqueId should not be Empty !!!")
                                                         String uniqueId){
        return Mono.just(
                ResponseEntity.ok(mediaFileService.uploadVideo(video, uniqueId))
        );
    }

    @GetMapping("/movie/stream/{uniqueId}")
    public Mono<Resource> streamVideo(@PathVariable("uniqueId") String uniqueId, @RequestHeader(name = "Range", required = false) String range) throws IOException {
        return mediaFileService.getMovieVideo(uniqueId);
    }
}
