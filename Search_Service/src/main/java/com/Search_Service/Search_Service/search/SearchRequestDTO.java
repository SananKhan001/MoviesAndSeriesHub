package com.Search_Service.Search_Service.search;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestDTO {
    @NotNull(message = "Search Term should not be null !!!")
    @NotEmpty(message = "Search Term should not be empty !!!")
    private String searchTerm;
}
