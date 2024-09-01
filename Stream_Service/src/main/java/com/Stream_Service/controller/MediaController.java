package com.Stream_Service.controller;

import com.Stream_Service.enums.VideoType;
import com.Stream_Service.service.MediaFileService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    
    @PostMapping("/poster/upload")
    public Mono<ResponseEntity<Mono<URI>>> uploadPoster(@RequestPart("poster") FilePart poster, @RequestParam("uniquePosterId")
                                                                                                  @NotEmpty(message = "PosterId should not be Empty !!!")
                                                                                                  String uniquePosterId) throws IOException {
        return Mono.just(
                ResponseEntity.ok(mediaFileService.uploadPoster(poster, uniquePosterId))
        );
    }

    @GetMapping("/poster/get/{posterId}")
    public Mono<ResponseEntity<Mono<Resource>>> getPoster(@PathVariable("posterId")
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
    public Mono<ResponseEntity<Mono<Resource>>> getProfile(@PathVariable("profileId")
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
                                                         String uniqueId,
                                                       @RequestParam(value = "videoType")
                                                       @NotNull(message = "videoType should not be null !!!")
                                                       VideoType videoType){
        return Mono.just(
                ResponseEntity.ok(mediaFileService.uploadVideo(video, uniqueId, videoType))
        );
    }

    @GetMapping("/movie/stream/{uniqueId}")
    public Mono<ResponseEntity<Resource>> streamMovieVideo(@PathVariable("uniqueId") String uniqueId, @RequestHeader String range) throws IOException {
        return mediaFileService.getMovieVideo(uniqueId, range);
    }

    @GetMapping("/series/stream/{uniqueId}")
    public Mono<ResponseEntity<Resource>> streamSeriesVideo(@PathVariable("uniqueId") String uniqueId, @RequestHeader String range) throws IOException {
        return mediaFileService.getSeriesVideo(uniqueId, range);
    }

    @DeleteMapping("/delete/media/{uniqueId}")
    public Mono<Void> deleteMedia(@PathVariable("uniqueId") String uniqueId) {
        return mediaFileService.deleteMediaFileByUniqueId(uniqueId);
    }
}
