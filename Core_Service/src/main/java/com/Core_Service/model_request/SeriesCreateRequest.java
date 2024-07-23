package com.Core_Service.model_request;

import com.Core_Service.enums.Genre;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SeriesCreateRequest {
    @NotNull(message = "Series name should not be null")
    @NotEmpty(message = "Series name should not be empty")
    private String name;
    @NotNull(message = "Genre should not be empty")
    private Genre genre;
    @NotNull(message = "Description should not be null")
    @NotEmpty(message = "Description should not be empty")
    private String description;
    @NotNull(message = "Price should not be null")
    @Min(value = 0, message = "Price can not go negative") // TODO:: @Min not working properly
    private int price;
}
