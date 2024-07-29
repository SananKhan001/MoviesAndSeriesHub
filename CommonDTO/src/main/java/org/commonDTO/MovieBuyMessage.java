package org.commonDTO;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MovieBuyMessage {
    private Long userId;
    private Long movieId;
    private boolean isNew;
}
