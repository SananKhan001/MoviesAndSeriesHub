package com.Stream_Service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("media_files")
public class MediaFile implements Persistable<Long> {
    @Id
    @Column("id")
    private Long id;

    @Column("unique_id")
    private String uniqueId;

    @Column("file_path")
    private String filePath;

    @Transient
    private boolean isNew;
}
