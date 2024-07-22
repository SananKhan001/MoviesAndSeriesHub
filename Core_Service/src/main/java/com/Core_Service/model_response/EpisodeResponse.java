package com.Core_Service.model_response;

import lombok.Builder;

@Builder
public class EpisodeResponse {
    private Long episodeId;
    private String episodeName;
    private String idInStreamDB;
    private String posterURL;
    private String createdAt;
}
