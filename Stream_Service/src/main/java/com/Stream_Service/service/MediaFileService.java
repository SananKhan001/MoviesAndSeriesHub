package com.Stream_Service.service;

import com.Stream_Service.config_jwt.AuthManager;
import com.Stream_Service.config_jwt.SecurityContext;
import com.Stream_Service.enums.Authority;
import com.Stream_Service.enums.VideoType;
import com.Stream_Service.models.MediaFile;
import com.Stream_Service.models.User;
import com.Stream_Service.models.UserMovieMapping;
import com.Stream_Service.models.UserSeriesMapping;
import com.Stream_Service.repository.EpisodeRepository;
import com.Stream_Service.repository.MediaFileRepository;
import com.Stream_Service.repository.UserMovieMappingRepository;
import com.Stream_Service.repository.UserSeriesMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


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

    @Autowired
    private UserSeriesMappingRepository userSeriesMappingRepository;

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

    @Value("{media.uri.get.poster.path}")
    private String getPosterPath;

    @Value("${media.uri.get.profile.path}")
    private String getProfilePath;

    @Value("${media.uri.movie.stream.path}")
    private String movieStreamPath;

    @Value("${media.uri.series.stream.path}")
    private String seriesStreamPath;

    public Mono<URI> uploadPoster(FilePart poster, String uniquePosterId) throws IOException {
        return Mono.fromCallable(() -> {
                    MediaFile mediaFile = MediaFile.builder()
                            .uniqueId(uniquePosterId)
                            .isNew(true)
                            .filePath(
                                    postersPath + postersPrefix + uniquePosterId
                            ).build();
                    poster.transferTo(new File(mediaFile.getFilePath())).subscribe();
                    mediaFileRepository.save(mediaFile).subscribe();
                    return mediaFile.getUniqueId();
                }).flatMap(fileUUID -> Mono.just(URI.create(streamServerURL + getPosterPath + fileUUID)));
    }


    public Mono<byte[]> getPoster(String uniquePosterId) throws IOException {
        return mediaFileRepository.findByUniqueId(uniquePosterId)
                .flatMap(mediaFile -> {
                    return Mono.fromCallable(() -> {
                        Path path = Paths.get(mediaFile.getFilePath());
                        if(Files.exists(path)){
                            return Files.readAllBytes(path);
                        }
                        else throw new FileNotFoundException("No file present at give path !!!");
                    }).onErrorResume(FileNotFoundException.class, e -> Mono.error(new RuntimeException("Error Reading File", e)));
                });
    }

    public Mono<URI> uploadProfile(FilePart profileImage, String uniqueProfileId) {
        return Mono.fromCallable(() -> {
            MediaFile mediaFile = MediaFile.builder()
                .uniqueId(uniqueProfileId)
                .isNew(true)
                .filePath(
                        profileImagesPath + profileImagesPrefix + uniqueProfileId
                ).build();
            profileImage.transferTo(new File(mediaFile.getFilePath())).subscribe();
            mediaFileRepository.save(mediaFile).subscribe();
            return mediaFile.getUniqueId();
        }).flatMap(fileUUID -> Mono.just(URI.create(streamServerURL + getProfilePath + fileUUID)));
    }

    public Mono<byte[]> getProfile(String uniqueProfileId) {
        return mediaFileRepository.findByUniqueId(uniqueProfileId)
                .flatMap(mediaFile -> {
                    return Mono.fromCallable(() -> {
                        Path path = Paths.get(mediaFile.getFilePath());
                        if(Files.exists(path)){
                            return Files.readAllBytes(path);
                        }
                        else throw new FileNotFoundException("No file present at give path !!!");
                    }).onErrorResume(FileNotFoundException.class, e -> Mono.error(new RuntimeException("Error Reading File", e)));
                });
    }

    public Mono<URI> uploadVideo(FilePart video, String uniqueId, VideoType videoType) {
        return episodeRepository.findByUniquePosterId(uniqueId).hasElement()
                .flatMap(hashElement -> {
                    if(!hashElement) return Mono.error(new IllegalArgumentException("Invalid UniqueId"));
                    return Mono.fromCallable(() -> {
                        MediaFile mediaFile = MediaFile.builder()
                                .uniqueId(uniqueId)
                                .isNew(true)
                                .filePath(
                                        videosPath + videosPrefix + uniqueId
                                ).build();
                        video.transferTo(new File(mediaFile.getFilePath())).subscribe();
                        mediaFileRepository.save(mediaFile).subscribe();
                        return mediaFile.getUniqueId();
                    }).flatMap(fileUUID -> {
                        if(videoType.equals(VideoType.MOVIE_EPISODE)) return Mono.just(URI.create(streamServerURL + movieStreamPath + fileUUID));
                        else return Mono.just(URI.create(streamServerURL + seriesStreamPath + fileUUID));
                    });
                });
    }

    public Mono<byte[]> getMovieVideo(String uniqueId) {
        User user = (User) SecurityContext.principal();
        return episodeRepository.findByUniquePosterId(uniqueId)
            .flatMap(episode -> {
                return userMovieMappingRepository.userMovieMappingByUserId(user.getId())
                    .collectList()
                    .flatMap(userMovieMappings -> {
                        if(userHasMovie(episode.getMovieId(), userMovieMappings, user.getAuthority())) {
                            return mediaFileRepository.findByUniqueId(uniqueId)
                                .flatMap(mediaFile -> {
                                    return Mono.fromCallable(() -> {
                                        Path path = Paths.get(mediaFile.getFilePath());
                                        if(Files.exists(path)) return Files.readAllBytes(path);
                                        else throw new FileNotFoundException("No file present at give path !!!");
                                    }).onErrorResume(FileNotFoundException.class, e -> Mono.error(new RuntimeException("Error Reading File", e)));
                                });
                        }
                        return Mono.error(new IllegalArgumentException("Asked Movie is not bought by User !!!"));
                    });
            });
    }

    public Mono<byte[]> getSeriesVideo(String uniqueId){
        User user = (User) SecurityContext.principal();
        return episodeRepository.findByUniquePosterId(uniqueId)
            .flatMap(episode -> {
                return userSeriesMappingRepository.userSeriesMappingByUserId(user.getId())
                    .collectList()
                    .flatMap(userSeriesMappings -> {
                        if(userHasSeries(episode.getSeriesId(), userSeriesMappings, user.getAuthority())) {
                            return mediaFileRepository.findByUniqueId(uniqueId)
                                .flatMap(mediaFile -> {
                                    return Mono.fromCallable(() -> {
                                        Path path = Paths.get(mediaFile.getFilePath());
                                        if(Files.exists(path)) return Files.readAllBytes(path);
                                        else throw new FileNotFoundException("No file present at give path !!!");
                                    }).onErrorResume(FileNotFoundException.class, e -> Mono.error(new RuntimeException("Error Reading File", e)));
                                });
                        }
                        return Mono.error(new IllegalArgumentException("Asked Series is not bought by User !!!"));
                    });
            });
    }

    private boolean userHasMovie(Long movieId, List<UserMovieMapping> userMovieMappings, String userAuthority) {
        long count = userMovieMappings.parallelStream()
                .filter(userMovieMapping -> userMovieMapping.getMovieId() == movieId)
                .count();
        return (count > 0 || userAuthority.equals(Authority.ADMIN.toString()));
    }

    private boolean userHasSeries(Long seriesId, List<UserSeriesMapping> userMovieMappings, String userAuthority) {
        long count = userMovieMappings.parallelStream()
                .filter(userSeriesMapping -> userSeriesMapping.getSeriesId() == seriesId)
                .count();
        return (count > 0 || userAuthority.equals(Authority.ADMIN.toString()));
    }

    public Mono<Void> deleteMediaFileByUniqueId(String uniqueId){
        return mediaFileRepository.findByUniqueId(uniqueId)
                .flatMap(mediaFile -> {
                    return Mono.fromCallable(() -> {
                        Path path = Paths.get(mediaFile.getFilePath());
                        if(Files.exists(path)) {
                            Files.delete(path);
                        }
                        mediaFileRepository.deleteById(mediaFile.getId()).subscribe();
                        return Mono.empty();
                    });
                }).then();
    }
}
