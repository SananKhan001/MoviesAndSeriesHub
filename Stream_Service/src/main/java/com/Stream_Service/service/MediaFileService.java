package com.Stream_Service.service;

import com.Stream_Service.models.MediaFile;
import com.Stream_Service.repository.MediaFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.net.URI;


@Service
public class MediaFileService {

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Autowired
    private ResourceLoader resourceLoader;

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
        return mediaFileRepository.save(mediaFile).map(media -> {
            return URI.create(streamServerURL + "/poster/get/" + media.getUniqueId());
        });
    }

    public Mono<byte[]> getPoster(String uniquePosterId) throws IOException {
        return mediaFileRepository.findByUniqueId(uniquePosterId)
                .map(media -> {
                    try {
                        return resourceLoader.getResource("classpath:/static"+ "/posters/" + postersPrefix + media.getUniqueId())
                                .getContentAsByteArray();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
