package com.Core_Service.model_response;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class EpisodeResponse implements Serializable {
    private Long episodeId;
    private String episodeName;
    private String uniqueEpisodeId;
    private String episodeURL;
    private String createdAt;
}
