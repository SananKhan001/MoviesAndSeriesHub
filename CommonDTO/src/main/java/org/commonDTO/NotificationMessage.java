package org.commonDTO;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NotificationMessage {
    private String content;
    private List<Long> userIdList;
}
