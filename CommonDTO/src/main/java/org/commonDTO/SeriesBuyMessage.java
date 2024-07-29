package org.commonDTO;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SeriesBuyMessage {
    private Long userId;
    private Long seriesId;
    private boolean isNew;
}
