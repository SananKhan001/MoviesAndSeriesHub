package org.commonDTO;

import lombok.*;

import java.util.Date;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MovieCreationMessage {
    private Long id;

    private String name;
    private String genre;
    private String searchableDescription;
    private String description;
    private String posterURL;
    private int price;
    private double rating;
    private Date createdAt;
}