package com.Core_Service.model_request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateRequest {
    @NotNull(message = "Comment should not be null !!!")
    @NotEmpty(message = "Comment should not be empty !!!")
    private String comment;

    @NotNull(message = "Rating should not be null !!!")
    @Min(message = "Minimum value for rating is 0", value = 0)
    @Max(message = "Maximum value for rating is 5", value = 5)
    private int rating;
}
