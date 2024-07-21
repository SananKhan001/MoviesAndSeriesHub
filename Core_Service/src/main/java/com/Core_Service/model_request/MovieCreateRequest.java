package com.Core_Service.model_request;

import com.Core_Service.enums.Genre;
import jakarta.validation.Validation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovieCreateRequest {
    @NotNull(message = "Movie name should not be null")
    @NotEmpty(message = "Movie name should not be empty")
    private String name;
    @NotNull(message = "Genre should not be empty")
    private Genre genre;
    @NotNull(message = "Description should not be null")
    @NotEmpty(message = "Description should not be empty")
    private String description;
    @NotNull(message = "Price should not be null")
    @Min(value = 0, message = "Price can not go negative")
    private int price;
}
