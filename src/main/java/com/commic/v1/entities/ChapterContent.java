package com.commic.v1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Data
@NoArgsConstructor
@Entity
@Table(name = "chapter_contents")
public class ChapterContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
    @Column(name = "link_image")
    private String linkImage;
    @ColumnDefault("false")
    private Boolean isDeleted = false;
}
