package com.Search_Service.Search_Service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(indexName = "movies")
@Setting(settingPath = "static/es-config/elastic-analyzer.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {
    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "autocomplete_index")
    private String name;
    private String genre;
    private String description;
    private String searchableDescription;
    private String posterURL;
    private int price;
    private double rating;
    private Date createdAt;
}
