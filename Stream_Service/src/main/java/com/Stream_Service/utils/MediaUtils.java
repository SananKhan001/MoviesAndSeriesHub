package com.Stream_Service.utils;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class MediaUtils {

    private static final int CHUNK_SIZE = 1024*1024; // 1MB

    public static Resource loadVideo(Path path, String range) throws IOException {
        Pair<Long, Long> parsedRange = parseRange(path, range);
        long rangeStart = parsedRange.getFirst(), rangeEnd = parsedRange.getSecond();

        InputStream inputStream = Files.newInputStream(path);
        inputStream.skip(rangeStart);
        long contentLength = rangeEnd - rangeStart + 1;

        byte[] data = new byte[(int) contentLength];
        int read = inputStream.read(data, 0, data.length);

        return new ByteArrayResource(data);
    }

    public static Pair<Long, Long> parseRange(Path path, String range) {
        long rangeEnd, rangeStart;
        long fileLength = path.toFile().length();

        if(range == null) {
            rangeStart = 0; rangeEnd = CHUNK_SIZE - 1;
        }
        else {
            String[] ranges = range.replace("bytes=", "").split("-");
            rangeStart = Long.parseLong(ranges[0]);
            rangeEnd = rangeStart + CHUNK_SIZE - 1;
            if (rangeEnd > fileLength - 1) {
                rangeEnd = fileLength - 1;
            }
        }

        return Pair.of(rangeStart, rangeEnd);
    }

    public static HttpHeaders generateHeader(Path path, String range) {
        Pair<Long, Long> parsedRange = parseRange(path, range);
        long rangeStart = parsedRange.getFirst(), rangeEnd = parsedRange.getSecond();
        long fileLength = path.toFile().length();
        long contentLength = rangeEnd - rangeStart + 1;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_RANGE, "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);
        headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");
        headers.add("X-Content-Type-Options", "nosniff");

        return headers;
    }

}
