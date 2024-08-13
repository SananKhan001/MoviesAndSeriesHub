package com.Search_Service.Search_Service.request;

import com.Search_Service.Search_Service.enums.IndexReference;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    @NotNull(message = "Description should not be null !!!")
    @NotEmpty(message = "Description should not be empty !!!")
    String description;

    @Min(value = 0, message = "Starting price should not be less than 0 !!!")
    Integer startingPrice;

    @Min(value = 0, message = "Ending price should not be less than 0 !!!")
    Integer endPrice;

    @Min(value = 0, message = "MinRating can't be less than 0 !!!")
    @Max(value = 5, message = "MinRating can't be more than 5 !!!")
    Integer minRating;

    @Min(value = 0, message = "MaxRating can't be less than 0 !!!")
    @Max(value = 5, message = "MaxRating can't be more than 5 !!!")
    Integer maxRating;

    @Min(value = 0, message = "PageNumber can not be less than 0 !!!")
    @NotNull(message = "PageNumber should not be null !!!")
    Integer page;

    @Min(value = 0, message = "PageSize can not be less than 0 !!!")
    @NotNull(message = "PageSize should not be null !!!")
    Integer size;

    @NotNull(message = "Index should not be null !!!")
    private IndexReference index;
}