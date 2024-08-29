package NotificationService.dto;

import NotificationService.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusMessage {
    private Long userId;
    private String username;
    private Status status;
}
