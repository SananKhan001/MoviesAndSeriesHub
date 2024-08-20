package com.payment.service.request;

import com.payment.service.enums.ContentType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    @NotNull(message = "userId should not be null !!!")
    @Min(message = "userId can not go less than 1 !!!", value = 1)
    private Long userId;
    @NotNull(message = "contentType should not be null !!!")
    private ContentType contentType;
    @NotNull(message = "contentId should not be null !!!")
    @Min(message = "contentId can not go less than 1 !!!", value = 1)
    private Long contentId;
}
