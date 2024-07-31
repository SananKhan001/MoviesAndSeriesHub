package com.Stream_Service.service;

import com.Stream_Service.config_jwt.AuthManager;
import com.Stream_Service.config_jwt.SecurityContext;
import com.Stream_Service.enums.Authority;
import com.Stream_Service.models.MediaFile;
import com.Stream_Service.models.User;
import com.Stream_Service.models.UserMovieMapping;
import com.Stream_Service.repository.EpisodeRepository;
import com.Stream_Service.repository.MediaFileRepository;
import com.Stream_Service.repository.UserMovieMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;


@Service
public class MediaFileService {

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private UserMovieMappingRepository userMovieMappingRepository;

    @Value("${media.file.posters.path}")
    private String postersPath;

    @Value("${media.file.profile_images.path}")
    private String profileImagesPath;

    @Value("${media.file.videos.path}")
    private String videosPath;

    @Value("${media.file.posters.prefix}")
    private String postersPrefix;

    @Value("${media.file.profile_images.prefix}")
    private String profileImagesPrefix;

    @Value("${media.file.videos.prefix}")
    private String videosPrefix;

    @Value("${stream.server.url}")
    private String streamServerURL;

    public Mono<URI> uploadPoster(FilePart poster, String uniquePosterId) throws IOException {
        MediaFile mediaFile = MediaFile.builder()
                .uniqueId(uniquePosterId)
                .isNew(true)
                .filePath(
                    postersPath + postersPrefix + uniquePosterId
                ).build();
        poster.transferTo(new File(mediaFile.getFilePath())).subscribe();
        return mediaFileRepository.save(mediaFile).flatMap(media -> {
            return Mono.just(URI.create(streamServerURL + "/poster/get/" + media.getUniqueId()));
        });
    }

    public Mono<byte[]> getPoster(String uniquePosterId) throws IOException {
        return mediaFileRepository.findByUniqueId(uniquePosterId)
                .hasElement()
                .flatMap(hasElement -> {
                    if(!hasElement) return Mono.error(new IllegalArgumentException("Invalid UniquePosterId"));
                    return Mono.fromSupplier(() -> {
                        try {
                            return resourceLoader.getResource("classpath:/static"+ "/posters/" + postersPrefix + uniquePosterId)
                                    .getContentAsByteArray();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                });
    }

    public Mono<URI> uploadProfile(FilePart profileImage, String uniqueProfileId) {
        MediaFile mediaFile = MediaFile.builder()
                .uniqueId(uniqueProfileId)
                .isNew(true)
                .filePath(
                        profileImagesPath + profileImagesPrefix + uniqueProfileId
                ).build();
        profileImage.transferTo(new File(mediaFile.getFilePath())).subscribe();
        return mediaFileRepository.save(mediaFile).flatMap(media -> {
            return Mono.just(URI.create(streamServerURL + "/profile/get/" + media.getUniqueId()));
        });
    }

    public Mono<byte[]> getProfile(String uniqueProfileId) {
        return mediaFileRepository.findByUniqueId(uniqueProfileId)
                .hasElement().flatMap(hasElement -> {
                    if(!hasElement) return Mono.error(new IllegalArgumentException("Invalid UniqueId"));
                    return Mono.fromSupplier(() -> {
                        try {
                            return resourceLoader.getResource("classpath:/static"+ "/profile_images/" + profileImagesPrefix + uniqueProfileId)
                                    .getContentAsByteArray();
                        } catch (IOException e) {
                            throw new RuntimeException("No poster were found with given id !!!");
                        }
                });
        });
    }

    public Mono<URI> uploadVideo(FilePart video, String uniqueId) {
        return episodeRepository.findByUniquePosterId(uniqueId)
                .hasElement().flatMap(hasElement -> {
                    if(!hasElement) return Mono.error(new IllegalArgumentException("Invalid UniqueId"));
                    MediaFile mediaFile = MediaFile.builder()
                            .uniqueId(uniqueId)
                            .isNew(true)
                            .filePath(
                                    videosPath + videosPrefix + uniqueId
                            ).build();
                    video.transferTo(new File(mediaFile.getFilePath())).subscribe();
                    return mediaFileRepository.save(mediaFile).flatMap(media -> {
                        return Mono.just(URI.create(streamServerURL + "/video/stream/" + media.getUniqueId()));
                    });
                });
    }

    public Mono<Resource> getMovieVideo(String uniqueId) {
        User user = (User) SecurityContext.principal();
        return episodeRepository.findByUniquePosterId(uniqueId)
                .flatMap(episode -> {
                    return userMovieMappingRepository.userMovieMappingByUserId(user.getId())
                            .collectList()
                            .flatMap(userMovieMappings -> {
                                if(userHas(episode.getMovieId(), userMovieMappings, user.getAuthority())){
                                    return Mono.fromSupplier(() -> {
                                        return resourceLoader.getResource("classpath:/static"+ "/videos/" + videosPrefix + episode.getUniquePosterId());
                                    });
                                }
                                return Mono.error(new IllegalArgumentException("Asked Movie is not bought by User !!!"));
                            });
                });
    }

    private boolean userHas(Long movieId, List<UserMovieMapping> userMovieMappings, String userAuthority) {
        long count = userMovieMappings.parallelStream()
                .filter(userMovieMapping -> userMovieMapping.getMovieId() == movieId)
                .count();
        return (count > 0 || userAuthority.equals(Authority.ADMIN.toString()));
    }
}
