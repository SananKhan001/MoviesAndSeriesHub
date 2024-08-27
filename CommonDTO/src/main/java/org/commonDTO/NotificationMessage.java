package org.commonDTO;

import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
    private String content;
    private List<Long> userIdList;
}
