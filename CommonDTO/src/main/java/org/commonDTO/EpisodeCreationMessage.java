package org.commonDTO;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeCreationMessage {
    private Long id;
    private String uniquePosterId;
    private Long movieId;
    private Long seriesId;
    private boolean isNew;
}
