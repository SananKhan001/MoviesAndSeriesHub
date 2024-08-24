package NotificationService.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrivateMessageRequest {
    @NotNull(message = "Content should not be null !!!")
    @NotEmpty(message = "Content should not be empty !!!")
    private String content;

    @NotNull(message = "UserIdList should not null !!!")
    private List<Long> userIdList;
}
