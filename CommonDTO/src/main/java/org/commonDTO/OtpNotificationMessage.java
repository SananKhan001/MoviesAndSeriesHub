package org.commonDTO;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OtpNotificationMessage {
    private String username;
    private String message;
}
